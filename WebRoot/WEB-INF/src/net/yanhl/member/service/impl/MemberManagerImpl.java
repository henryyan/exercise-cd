package net.yanhl.member.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.member.dao.MemberDao;
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.member.service.MemberManager;
import net.yanhl.member.util.MemberUtil;
import net.yanhl.util.JSONUtil;
import net.yanhl.util.UserUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 会员卡业务管理实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090720
 */
@Service(value="memberManager")
@Transactional(rollbackFor=Exception.class)
public class MemberManagerImpl extends BaseManagerImpl implements MemberManager {

	@Resource
	protected MemberDao memberDao;

	@Transactional(readOnly = true)
	public List<MemberCard> memberCardList(Long venueId) throws MemberException {
		return memberDao.memberCardList(venueId);
	}

	@Transactional(readOnly = true)
	public List<MemberCard> memberCardList(Long venueId,String mobilePhone) throws MemberException {
		return memberDao.memberCardList(venueId,mobilePhone);
	}

	@Transactional(readOnly = true)
	public List<CardType> getCardTypes(Long venueId) throws MemberException {
		return memberDao.getCardTypes(venueId);
	}

	@Transactional(readOnly = true)
	public List<CardUsageRecord> cardUsageRecordList(long venueId,long cardId) throws MemberException {
		return memberDao.cardUsageRecordList(venueId,cardId);
	}

	/**
	 * 充值
	 */
	public double recharge(long venueId,Long memberCardId, double total)  throws MemberException{
		Object object = get(MemberCard.class, memberCardId);
		double finalBalance = 0;
		if(object != null) {
			if(total < 0) {
				throw new MemberException("充值金额必须大于0");
			}


			// 1、设置会员卡余额
			MemberCard memberCard = (MemberCard) object;
			double balance = memberCard.getBalance() + total;
			memberCard.setBalance(balance);

			// 2、插入一条会员卡使用记录
			CardUsageRecord record = new CardUsageRecord();
			record.setCardId(memberCard.getId());
			record.setCardNo(memberCard.getCardNumber());
			record.setVenueId(venueId);
			record.setUsageDate(new java.util.Date(System.currentTimeMillis()));
			record.setOptionTotal(total);
			record.setUsageType(MemberUtil.CARD_USAGE_TYPE_RECHARGE);
			// 使用时段
			Calendar ca = Calendar.getInstance();
			String timeSlice = ca.get(Calendar.HOUR_OF_DAY) + ":" + ca.get(Calendar.MINUTE);
			record.setUsageTimeSlice(timeSlice);

			// 3、增加会员卡有效期
			if (memberCard.getPeriodValidity() != null) {
				Calendar pca = Calendar.getInstance();
//				pca.setTime(memberCard.getPeriodValidity());
//				pca.add(Calendar.MONTH, MemberUtil.DEFAULT_MEMBER_CARD_PERIOD_VALIDITY);
				pca.add(Calendar.MONTH, memberCard.getCardType().getPeriodMonth());
				memberCard.setPeriodValidity(pca.getTime());
			}

			// 4、保存更改
			update(memberCard);
			save(record);

			// 5、设置返回值
			finalBalance = memberCard.getBalance();
		} else {
			throw new MemberException("没有找到ID为" + memberCardId + "的会员卡");
		}
		return finalBalance;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<MemberCard> memberCardList(ListQuery listQuery) throws MemberException {

		// 设置场馆ID
		String venueId = UserUtil.getCurrentVenueId(listQuery.getRequest());
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });

		// 设置查询条件
		listQuery.getQueryFilter().put("o.cardNumber", new String[] { BaseQuery.AND, BaseQuery.EQ });
		listQuery.getQueryFilter().put("o.name", new String[] { BaseQuery.AND, BaseQuery.LIKE });
		listQuery.getQueryFilter().put("o.mobilePhone", new String[] { BaseQuery.AND, BaseQuery.LIKE });

		return baseDao.find(listQuery);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<CardUsageRecord> cardUsageRecordList(ListQuery listQuery) throws MemberException {
		// 设置场馆ID
		String venueId = UserUtil.getCurrentVenueId(listQuery.getRequest());
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });

		// 设置查询条件
		listQuery.getQueryFilter().put("o.cardId", new String[] { BaseQuery.AND, BaseQuery.EQ });

		return baseDao.find(listQuery);
	}

	@Override
	public JSONObject loadCardUsageDetail(Long cardUsageId) throws MemberException {
		JsonConfig config = JSONUtil.getConfigWithDateTime();
		config.setExcludes(new String[] {"fieldOrder", "field"});
		JSONObject result = new JSONObject();
		List<Object> fieldOrders = baseDao.find("from FieldOrder o where o.cardUsageId=" + cardUsageId);
		if (fieldOrders != null && fieldOrders.size() > 0) {
			for (Object object : fieldOrders) {
				FieldOrder order = (FieldOrder) object;
				Long activityId = order.getActivityId();
				try {
					Class<? extends FieldActivity> activityPojoClass = FieldUtil.getFieldActivityPojoClass(order.getFieldType());
					Object activityObj = get(activityPojoClass, activityId);
					if (activityObj != null) {
						order.setFieldActivity((FieldActivity) activityObj);
					}
				} catch (ClassNotFoundException e) {
					throw new MemberException("根据" + order.getFieldType() + "查询不到对应的class", e);
				} catch (FieldException e) {
					throw new MemberException("根据" + order.getFieldType() + "查询不到对应的class", e);
				}
			}
		}
		result.accumulate("orders", fieldOrders, config);
		return result;
	}

}

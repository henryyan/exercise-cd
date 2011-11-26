package net.yanhl.retail.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.member.dao.MemberDao;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.member.util.MemberUtil;
import net.yanhl.retail.dao.RetailSellDao;
import net.yanhl.retail.pojo.RetailGoods;
import net.yanhl.retail.pojo.RetailSell;
import net.yanhl.retail.service.RetailSellManager;
import net.yanhl.util.UserUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 零售商品销售实现类
 * 
 * @author HenryYan
 *
 */
@Service(value = "retailSellManager")
@Transactional(rollbackFor = Exception.class)
public class RetailSellManagerImpl extends BaseManagerImpl implements RetailSellManager {

	@Resource
	protected RetailSellDao retailSellDao;

	@Resource
	protected MemberDao memberDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<RetailSell> retailSellList(ListQuery listQuery) {
		// 设置场馆ID
		String venueId = UserUtil.getCurrentVenueId(listQuery.getRequest());
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });

		// 设置查询条件
		listQuery.getQueryFilter().put("o.retailName", new String[] { BaseQuery.AND, BaseQuery.LIKE });

		return baseDao.find(listQuery);
	}

	@Override
	public void sell(Long venueId, Integer amount, Long retailGoodsId, String memberCardNumber) {
		Object retailGoodsObject = retailSellDao.getHibernateTemplate().get(RetailGoods.class, retailGoodsId);
		RetailGoods retailGoods = null;
		if (retailGoodsObject != null) {
			retailGoods = (RetailGoods) retailGoodsObject;
		} else {
			throw new RuntimeException("找不到商品：" + retailGoodsId);
		}
		MemberCard memberCard = memberDao.findMemberCard(memberCardNumber);
		if (memberCard == null) {
			throw new RuntimeException("找不到会员卡：" + memberCardNumber);
		}

		double totalPrice = amount * retailGoods.getPrice().doubleValue();
		
		if (memberCard.getBalance() < totalPrice) {
			throw new RuntimeException("卡内余额不足");
		}
		
		RetailSell retailSell = new RetailSell();
		retailSell.setAmount(amount);
		retailSell.setMemberCardNumber(memberCardNumber);
		retailSell.setMemberUserName(memberCard.getName());
		retailSell.setRetailGoodsId(retailGoodsId);
		retailSell.setRetailName(retailGoods.getRetailName());
		retailSell.setRetailPrice(retailGoods.getPrice());
		retailSell.setVenueId(venueId);
		retailSellDao.insertOrUpdate(retailSell);

		// 创建会员卡使用记录
		CardUsageRecord usage = new CardUsageRecord();
		usage.setCardId(memberCard.getId());
		usage.setCardNo(memberCard.getCardNumber());
		usage.setVenueId(venueId);
		usage.setSignature(memberCard.getName());
		usage.setUsageDate(new java.util.Date());
		usage.setRelatedId(retailSell.getId().toString());

		String userContent = "购买 " + amount + " 个" + retailSell.getRetailName() + "，单价：￥" + retailSell.getRetailPrice();
		usage.setUsageTimeSlice(userContent);
		
		usage.setOptionTotal(totalPrice);

		// 会员卡使用记录
		usage.setUsageType(MemberUtil.CARD_USAGE_TYPE_RETAIL);
		save(usage);
		
		Double balance = memberCard.getBalance();
		memberCard.setBalance(balance - totalPrice);
		save(memberCard);
	}

}

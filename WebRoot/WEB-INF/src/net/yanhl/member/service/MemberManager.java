package net.yanhl.member.service;

import java.util.List;

import net.sf.json.JSONObject;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.BaseManager;
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;

/**
 * <p><b>Title：</b> 会员卡业务接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public interface MemberManager extends BaseManager {

	/**
	 * 获取指定场馆的会员卡列表
	 * @param venueId	场馆ID
	 * @return
	 */
	List<MemberCard> memberCardList(Long venueId) throws MemberException;

	/**
	 * 获取指定场馆的会员卡列表
	 * @param listQuery
	 * @return
	 * @throws MemberException
	 */
	List<MemberCard> memberCardList(ListQuery listQuery) throws MemberException;

	/**
	 * 获取指定场馆的会员卡列表
	 * @param venueId		场馆ID
	 * @param mobilePhone	手机号码
	 * @return
	 * @throws MemberException
	 */
	List<MemberCard> memberCardList(Long venueId,String mobilePhone) throws MemberException;

	/**
	 * 获得所有会员卡类型
	 * @param venueId	场馆ID
	 * @return	会员卡类型集合
	 */
	List<CardType> getCardTypes(Long venueId) throws MemberException;

	/**
	 * 获得所有会员卡使用记录
	 * @param venueId 	场馆ID
	 * @param cardId 	会员卡ID
	 * @return	会员卡使用记录
	 */
	List<CardUsageRecord> cardUsageRecordList(long venueId, long cardId) throws MemberException;

	/**
	 * 获得所有会员卡使用记录
	 * @param listQuery		列表查询对象
	 * @return
	 * @throws MemberException
	 */
	List<CardUsageRecord> cardUsageRecordList(ListQuery listQuery) throws MemberException;

	/**
	 * 会员卡充值
	 * @param venueId	    场馆ID
	 * @param memberCardId	会员卡ID
	 * @param total			充值金额
	 * @throws	MemberException	充值失败时
	 */
	double recharge(long venueId,Long memberCardId, double total) throws MemberException;
	
	/**
	 * 获得会员卡使用记录详细信息，包含哪个场地的哪个时间段等信息
	 * @param cardUsageId	会员卡使用记录ID
	 * @return
	 * @throws MemberException
	 */
	JSONObject loadCardUsageDetail(Long cardUsageId) throws MemberException;
}

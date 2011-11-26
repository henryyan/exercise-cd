package net.yanhl.member.dao;

import java.util.List;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;

/**
 * <p><b>Title：</b> 会员卡管理DAO</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090628
 */
public interface MemberDao extends BaseDao {
	
	/**
	 * 根据会员卡号查询
	 * @param memberCardNumber	会员卡号
	 * @return
	 */
	MemberCard findMemberCard(String memberCardNumber);

	/**
	 * 获取指定场馆的会员卡列表
	 * @param venueId	场馆ID
	 * @return
	 */
	List<MemberCard> memberCardList(Long venueId);

	/**
	 * 获取指定场馆的会员卡列表
	 * @param listQuery
	 * @return
	 * @throws MemberException
	 */
	List<MemberCard> memberCardList(Long venueId,String mobilePhone);

	/**
	 * 获得所有会员卡类型
	 * @param venueId	场馆ID
	 * @return	会员卡类型集合
	 */
	List<CardType> getCardTypes(Long venueId);
	/**
	 * 获得所有会员卡使用记录
	 * @param venueId
	 * @return
	 */
	List<CardUsageRecord> cardUsageRecordList(long venueId,long cardId);

	/**
	 * 统计会员卡数量
	 * @param venueId	场馆ID
	 * @return	本场馆会员卡数量
	 */
	Long countMemberCard(Long venueId);

	/**
	 * 统计状态正常的会员卡
	 * @param venueId	场馆ID
	 * @return	本场馆状态正常的会员卡数量
	 */
	Long countEnableMemberCard(Long venueId);

}

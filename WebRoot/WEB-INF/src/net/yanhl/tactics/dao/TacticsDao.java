package net.yanhl.tactics.dao;

import java.sql.Date;
import java.util.List;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsPrice;

/**
 * <p><b>Title：</b> 价格策略DAO接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091218
 */
public interface TacticsDao extends BaseDao {

	/**
	 * 查询在策略日期范围内的所有活动ID
	 * @param venueId		场馆ID
	 * @param tacticsId		策略ID
	 * @param fieldType		场地类型
	 * @param tacticsDates	策略日期范围
	 * @return	在范围内的活动列表ID
	 */
	List<Integer> activityBetweenTacticsDate(Long venueId, Long tacticsId, String fieldType, List<Date> tacticsDates);

	/**
	 * 获得使用策略生成活动的日期段
	 * @param venueId		场馆ID
	 * @param tacticsId		策略ID
	 * @param fieldType		场地类型
	 * @return	日期段集合
	 * @deprecated
	 */
	List<java.sql.Date> getUsableDate(Long venueId, Long tacticsId, String fieldType);

	/**
	 * 查询策略
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @return	策略集合
	 */
	List<Tactics> findTactics(Long venueId, String fieldType);

	/**
	 * 查询指定策略的所有日期段
	 * @param tacticsId		策略ID
	 * @return	日期段集合[from_date, to_date]
	 */
	List<java.sql.Date[]> getTacticsDates(Long tacticsId);

	/**
	 * 复制在策略日期范围内并且已预定的记录到策略活动临时表中
	 * @param activityIdList	活动ID集合
	 * @param fieldType			场地类型
	 */
	Integer copyBookedActivityToTacticsActivity(List<Integer> activityIdList, Long tacticsId, String fieldType);

	/**
	 * 策略价格列表
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @return		策略价格列表
	 */
	List<TacticsPrice> getTacticsPriceList(Long venueId, String fieldType);

	/**
	 * 把备份的活动根据日期、时间匹配覆盖到原来的活动表中
	 * 自动匹配条件：<可用日期、时段、场馆ID、场地ID>全部匹配，订单不能为空
	 * @param venueId		场馆ID
	 * @param tacticsId		策略ID
	 * @param fieldType		场地类型
	 * @return	自动匹配成功活动ID
	 */
	List<Integer> repeatBookActivity(Long venueId, Long tacticsId, String fieldType);

	/**
	 * 删除策略活动备份的记录
	 * @param venueId		场馆ID
	 * @param tacticsId		策略ID
	 * @param fieldType		场地类型
	 * @return	删除活动条数
	 */
	Integer deleteTacticsActivity(Long venueId, Long tacticsId, String fieldType);

	/**
	 * 查询和策略相关并且未预定的活动
	 * @param venueId		场馆ID
	 * @param tacticsId		策略ID
	 * @param fieldType		场地类型
	 * @return	活动集合
	 * @throws FieldException	根据场地类型找不到对应表时
	 */
	List<? extends FieldActivity> findActivity(Long venueId, Long tacticsId, String fieldType) throws FieldException;

}

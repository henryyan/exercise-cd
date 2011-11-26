package net.yanhl.tactics.service;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONObject;
import net.yanhl.base.query.ListQuery;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.price.exception.PriceException;
import net.yanhl.tactics.exception.TacticsDateException;
import net.yanhl.tactics.exception.TacticsException;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsDate;
import net.yanhl.tactics.pojo.TacticsPrice;

/**
 * <p><b>Title：</b> 策略业务接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091205
 */
public interface TacticsManager {

	/**
	 * 获取价格策略
	 * @param listQuery		列表查询对象
	 * @return	策略列表
	 * @throws TacticsException
	 */
	List<Tactics> getTacticsList(ListQuery listQuery) throws TacticsException;

	/**
	 * 获取策略日期
	 * @param listQuery		列表查询对象
	 * @return	策略列表
	 * @throws TacticsException
	 */
	List<TacticsDate> getTacticsDateList(ListQuery listQuery) throws TacticsException;

	/**
	 * 保存价格集合，保存之前删除已设置价格信息
	 * @param tacticsId	特殊策略ID
	 * @param fieldType 场地类型
	 * @param prices 	价格集合
	 */
	void savePrices(Long tacticsId, Collection<TacticsPrice> prices);

	/**
	 * 策略价格列表
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @return		策略价格列表
	 */
	List<TacticsPrice> getTacticsPriceList(Long venueId, String fieldType);

	/**
	 * <p>功能说明：</p>
	 * <ul><li>如果不在范围内则不更新</li>
	 * <li>如果以生成的活动在此策略范围内，则更新已生成的活动</li></ul>
	 * <p>已生成的活动在策略日期范围内处理办法：</p>
	 * <ul><li>提取在策略日期范围内并且已经预定的活动记录保存到另外一张表中<br/>
	 * 供其他模块调用，数据表结构和当前处理的活动记录结构相同</li>
	 * <li>删除在策略日期范围内的所有活动记录</li>
	 * <li>根据删除活动的日期戳重新生成活动</li>
	 * </ul>
	 * @param venueId	场馆ID
	 * @param tacticsId	策略ID
	 * @param fieldType	场地类型
	 * @throws TacticsException
	 * @throws FieldException 生成活动时
	 * @throws FieldActivityException 生成活动时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void deleteAndBakActivity(Long venueId, Long tacticsId, String fieldType) throws TacticsException, FieldException, FieldActivityException, PriceException;

	/**
	 * 把指定策略下面&未预定的活动价格设置成默认策略的价格
	 * @param venueId	场馆ID
	 * @param tacticsId	策略ID
	 * @param fieldType	场地类型
	 * @throws TacticsException
	 * @throws FieldException	找不到类型对应的值
	 * @throws FieldActivityException 生成活动时
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	void applyDefaultTactics(Long venueId, Long tacticsId, String fieldType) throws TacticsException, FieldException, FieldActivityException, PriceException;

	/**
	 * 检查指定的策略日期是否和本场馆已存在的策略日期重叠
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param fromDate	开始日期
	 * @param toDate	结束日期
	 * @return	<strong>JSON</strong>格式的结果<br/>
	 * 设置重复标志<strong><em>repeat</em></strong>[true, false]，
	 * 为true时
	 * @throws TacticsDateException	日期为空时
	 */
	JSONObject checkRepeatDates(Long venueId, String fieldType, Date fromDate, Date toDate) throws TacticsDateException;

	/**
	 * 待处理策略活动列表
	 * @param venueId
	 * @param fieldType
	 * @return
	 * @throws TacticsException
	 * @throws ClassNotFoundException
	 * @throws FieldException 找不到类型对应的值
	 */
	List<Object> tacticsActivityList(Long venueId, String fieldType) throws TacticsException, ClassNotFoundException, FieldException;

}

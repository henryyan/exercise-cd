package net.yanhl.field.dao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;

/**
 * <p><b>Title：</b> 场地管理接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
public interface FieldDao extends BaseDao {

	/**
	 * 获得最大场地编号
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	Integer getMaxFieldNo(String fieldType, Long venueId) throws FieldException;

	/**
	 * 获得场地列表
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @return	List形式的场地对象
	 * @throws FieldException 找不到类型对应的值
	 */
	List<? extends BaseField> getFieldList(Long venueId, String fieldType) throws FieldException;

	/**
	 * 根类型、状态查询场地
	 * @param fieldType	场地类型
	 * @param status	场地启用状态
	 * @return	List形式的场地对象
	 * @throws FieldException 找不到类型对应的值
	 */
	@SuppressWarnings("rawtypes")
	List getFieldList(String fieldType, String status) throws FieldException;

	/**
	 * 根类型、状态查询场地
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param status	场地启用状态
	 * @return	List形式的场地对象
	 * @throws FieldException 找不到类型对应的值
	 */
	@SuppressWarnings("rawtypes")
	List getFieldList(Long venueId, String fieldType, String status) throws FieldException;

	/**
	 * 获取所有【启用】的场地
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型标示
	 * @return	场地类型列表
	 * @throws FieldException 找不到类型对应的值
	 */
	List<? extends BaseField> getEnableField(Long venueId, String fieldType) throws FieldException;

	/**
	 * 获取所有【启用】的场地
	 * @param fieldType	场地类型标示
	 * @return	场地类型列表
	 * @throws FieldException 找不到类型对应的值
	 */
	List<? extends BaseField> getEnableField(String fieldType) throws FieldException;
	
	/**
	 * 删除在制定范围内的活动
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型标示
	 * @param usableDates	日期集合
	 * @return	删除成功条数
	 */
	Integer deleteActivity(Long venueId, String fieldType, Object[] usableDates);

	/**
	 * 从今天开始查找可预订的场地状态
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param fromDate	从哪天开始
	 * @return 键值对：日期,是否可预订
	 */
	Map<String, String> getFieldActivityStatusForDate(String fieldType, Long venueId, String fromDate);

	/**
	 * 查找指定日期的各时段预订情况
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	预订日期
	 * @return 键值对：日期,是否可预订
	 */
	Map<String, String> getFieldActivityStatusForPeriod(String fieldType, Long venueId, String usableDate);

	/**
	 * 生成场地活动预订表格
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	预订日期
	 * @return	<时段, [ [订单ID, 场地ID, 场地名称, 活动状态] ]
	 * @throws FieldActivityException
	 * @throws FieldException 找不到类型对应的值
	 */
	Map<String, List<Object[]>> getActivityGrid(Long venueId, String fieldType, String usableDate)
			throws FieldActivityException, FieldException;

	/**
	 * 三点<场地、预订日期、时段>定位查询可用场地
	 * @param fieldType		场地类型
	 * @param venueId		场馆ID
	 * @param usableDate	可预订日期
	 * @param period		时段
	 * @return	键值对：场地ID，场地名称
	 */
	Map<String, String> getFieldActivityStatusForField(String fieldType, Long venueId, String usableDate, String period);

	/**
	 * 查询场地活动列表
	 * @param fieldId	场地ID
	 * @param nextStep	分页步长
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 通过场地类型获取对应POJO名称时
	 */
	List<FieldActivity> getFieldActivity(Long fieldId, int nextStep, String fieldType) throws FieldException;

	/**
	 * 删除和场地活动有关的订单信息
	 * @param fieldType			场地类型
	 * @param fieldActivityIds	可以是单个ID，也可以是用逗号分隔的ID
	 * @return
	 * @throws FieldException 通过场地类型获取对应场地活动POJO名称时
	 */
	boolean deleteOrderByActivity(String fieldType, String fieldActivityIds) throws FieldException;

	/**
	 * 改变ID为fieldActivityId的活动对象状态为status
	 * @param fieldType			场地类型
	 * @param fieldActivityIds	场地活动对象ID
	 * @param status			状态
	 * @return	是否修改成功
	 * @throws FieldException 找不到类型对应的值
	 */
	boolean changeActivityStatus(String fieldType, String fieldActivityIds, String status) throws FieldException;

	/**
	 * 把已支付的活动费用退回到会员卡上
	 * @param fieldType			场地类型
	 * @param fieldOrder		订单对象
	 * @param refundmentSum		退款总额
	 * @param deleteRecored		是否删除用卡记录
	 * @return	退款结果true|false
	 */
	boolean refundment(String fieldType, FieldOrder fieldOrder, Double refundmentSum, boolean deleteRecored);

	/**
	 * 统计验证状态（已验证、未验证）活动数量
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	活动日期
	 * @param verificationStatus	是否验证
	 * @return	待验证的活动数量
	 * @throws FieldException 找不到类型对应的值
	 */
	Long countVerificationStatusActivity(Long venueId, String fieldType, Date usableDate, boolean verificationStatus) throws FieldException;

	/**
	 * 统计作废状态活动
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	活动日期
	 * @return	作废状态活动数量
	 * @throws FieldException 找不到类型对应的值
	 */
	Long countInvalidActivity(Long venueId, String fieldType, Date usableDate) throws FieldException;

	/**
	 * 统计有预定活动的会员
	 * @param venueId		场馆ID
	 * @param usableDate	活动日期
	 * @return	有预定活动的会员数量
	 * @throws FieldException 找不到类型对应的值
	 */
	Long countMemberHasOrder(Long venueId, Date usableDate) throws FieldException;

	/**
	 * 统计场地的总量
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException	找不到类型对应的值
	 */
	Long countFields(Long venueId, String fieldType) throws FieldException;

}

package net.yanhl.field.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import mlink.esms.SmsException;
import net.sf.json.JSONObject;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

/**
 * <p><b>Title：</b> 场地管理接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public interface FieldManager {

	/**
	 * 获得最大场地编号
	 * @return
	 */
	Integer getMaxFieldNo(String fieldType, Long venueId) throws FieldException;

	/**
	 * 获得场地列表
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @return
	 */
	List<? extends BaseField> getFieldList(Long venueId, String fieldType) throws FieldException;

	/**
	 * 根类型、状态查询场地
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param status	场地启用状态
	 * @return	List形式的场地对象
	 */
	List<? extends BaseField> getFieldList(Long venueId, String fieldType, String status) throws FieldException;

	/**
	 * 从今天开始查找可预订的场地状态
	 * @param venudId	场馆ID
	 * @param fieldType	场地类型
	 * @param fromDate	从哪天开始
	 * @return 键值对：日期,是否可预订
	 */
	Map<String, String> getFieldActivityStatusForDate(String fieldType, Long venudId, String fromDate)
			throws FieldException;

	/**
	 * 查找指定日期的各时段预订情况
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param usableDate从哪天开始
	 * @return 键值对：日期,是否可预订
	 */
	Map<String, String> getFieldActivityStatusForPeriod(String fieldType, Long venueId, String usableDate)
			throws FieldException;

	/**
	 * 三点<场地、预订日期、时段>定位查询可用场地
	 * @param fieldType		场地类型
	 * @param venueId		场馆ID
	 * @param usableDate	可预订日期
	 * @param period		时段
	 * @return	键值对：场地ID，场地名称
	 */
	Map<String, String> getFieldActivityStatusForField(String fieldType, Long venueId, String usableDate, String period)
			throws FieldException;

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
	 * 获得场地活动列表
	 * @param fieldId	场地ID
	 * @param nextStep
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 通过场地类型获取对应POJO名称时
	 */
	List<FieldActivity> getFieldActivity(Long fieldId, int nextStep, String fieldType) throws FieldException;

	/**
	 * 增加新订单
	 * @param order			订单对象
	 * @param activityId	场地活动ID
	 * @param fieldType		场地类型
	 * @param venueUser		场馆用户对象
	 * @param patch			是否为补登计，补登计不发送预定短信
	 * @param payStyle		付款方式
	 * @return	JSON格式的响应信息
	 * @throws FieldException	场地预订出错时
	 * @throws ClassNotFoundException 通过场地类型找不到对应的Pojo class时
	 */
	JSONObject addOrder(FieldOrder order, String activityId, String fieldType, VenueUser venueUser, boolean patch,
			String payStyle) throws FieldException, ClassNotFoundException;

	/**
	 * 批量增加新订单
	 * @param order			订单对象
	 * @param activityIds	场地活动ID，多个用逗号分隔
	 * @param fieldType		场地类型
	 * @param venueUser		场馆用户对象
	 * @return	预订成功后以订单ID为KEY，订单对象为VALUE
	 * @throws FieldException	场地预订出错时
	 * @throws ClassNotFoundException 通过场地类型找不到对应的活动Pojo class时
	 * @throws MemberException 
	 */
	JSONObject addOrderForBatch(FieldOrder order, String activityIds, String fieldType, VenueUser venueUser)
			throws FieldException, ClassNotFoundException, MemberException;

	/**
	 * <p>
	 * 1、修改订单，判断是否扣款
		已经扣款，会员卡号不能修改，修改之后做扣款。
		未扣款，会员号可以修改，修改之后做扣款动作。
		<br/>
		2、如果手机丢了，或者停机，修改手机号码
		重新生成验证码并更新，然后向新手机号发送新的验证码
	 * </p>
	 * @param orderId	订单ID
	 * @param newOrder	新的订单对象
	 * @return	更新成功true，否则false
	 * @throws FieldException	场地预订出错时
	 * @throws SmsException		发送短信时
	 */
	void updateOrder(Long orderId, FieldOrder newOrder, VenueUser venueUser) throws FieldException, SmsException;

	/**
	 * 查找当前场馆所有场地最大发布天数
	 * @param venueId	指定场馆ID，如果为null或者0则默认为全部场地
	 * @param fieldType	场地类型
	 * @return	最大发布天数
	 * @throws FieldException
	 * @throws ClassNotFoundException 根据场地类型查找对应的活动类型时
	 */
	String getMaxIssueDays(Long venueId, String fieldType) throws FieldException, ClassNotFoundException;

	/**
	 * 根据status操作ID为fieldActivityId的活动状态并退款
	 * @param fieldType			场地类型
	 * @param fieldActivityId	场地活动ID
	 * @param status			要更改的订单状态
	 * @param refundSum				退款金额
	 * @return	操作成功true，否则false
	 * @throws FieldException 根据场地类型查找对应的活动类型时
	 * @throws ClassNotFoundException 根据场地类型查找对应的活动类型时
	 * @throws FieldException	场地操作时
	 * @throws SmsException		发送短信时
	 */
	JSONObject orderHandle(String fieldType, String activityType, String fieldActivityId, String status,
			VenueInfo venueInfo, Double refundSum) throws FieldActivityException, ClassNotFoundException,
			FieldException;

	/**
	 * 根据status操作ID为fieldActivityId的活动状态
	 * @param fieldType			场地类型
	 * @param fieldActivityId	场地活动ID
	 * @param status			要更改的订单状态
	 * @return	操作成功true，否则false
	 * @throws FieldException	场地操作时
	 */
	boolean orderHandle(String fieldType, String fieldActivityId, String status) throws FieldActivityException;

	/**
	 * 把已支付的活动费用退回到会员卡上
	 * @param fieldType			场地类型
	 * @param fieldOrder		订单对象
	 * @param refundmentSum		退款总额
	 * @param deleteRecored		是否删除用卡记录
	 * @return	退款结果true|false
	 */
	boolean refundment(String fieldType, FieldOrder fieldOrder, Double refundmentSum, boolean deleteRecored)
			throws FieldException;

	/**
	 * 校验验证码，正确直接扣款
	 * @param longActivityId	活动ID
	 * @param fieldType			场地类型
	 * @param validateCode		验证码
	 * @return
	 * @throws FieldException 根据场地类型查找对应的活动类型时
	 * @throws ClassNotFoundException 根据场地类型查找对应的活动类型时
	 */
	String[] validateCode(Long longActivityId, String fieldType, String validateCode) throws FieldException,
			ClassNotFoundException;

	/**
	 * 用会员卡方式支付订单
	 * @param fieldActivity			活动对象
	 * @param memberCard			会员卡
	 * @param cardUsageRecord		使用记录
	 * @param canPayLessBalance		余额不足时能否继续支付
	 * @return	支付成功true
	 * 		<br>支付失败false
	 * 		<ul>
	 * 			<li>1.没有在数据中找到会员卡记录</li>
	 * 			<li>2.会员卡中余额不足</li>
	 * 			<li>3.订单对象为空</li>
	 * 		</ul>
	 * @throws FieldException	订单对象为空；获取节假日时
	 * @throws MemberException	支付失败时
	 */
	void payUseCard(FieldActivity fieldActivity,  double paySum, MemberCard memberCard, CardUsageRecord cardUsageRecord,
			boolean canPayLessBalance) throws FieldException, MemberException;

	/**
	 * 支付订单
	 * @param fieldActivity	活动对象
	 * @param paymentStyle	支付方式
	 * @return	订单对象为空；获取节假日时
	 * @throws FieldException	订单对象为空；获取节假日时
	 * @throws MemberException	支付失败时
	 */
	boolean pay(FieldActivity fieldActivity, String paymentStyle) throws FieldException, MemberException;

	/**
	 * 删除在制定范围内的活动
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型标示
	 * @param usableDates	日期集合
	 * @return	删除成功条数
	 */
	Integer deleteActivity(Long venueId, String fieldType, Object[] usableDates);

	/**
	 * 统计验证状态（已验证、未验证）活动数量
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	活动日期
	 * @param verificationStatus	是否验证
	 * @return	待验证的活动数量
	 * @throws FieldException 找不到类型对应的值
	 */
	Long countVerificationStatusActivity(Long venueId, String fieldType, Date usableDate, boolean verificationStatus)
			throws FieldException;

	/**
	 * 统计作废状态活动
	 * @param venueId		场馆ID
	 * @param fieldType		场地类型
	 * @param usableDate	活动日期
	 * @return	作废状态活动数量
	 * @throws FieldException	找不到类型对应的值
	 */
	Long countInvalidActivity(Long venueId, String fieldType, Date usableDate) throws FieldException;

	/**
	 * 统计场地的总量
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException	找不到类型对应的值
	 */
	Long countFields(Long venueId, String fieldType) throws FieldException;

	/**
	 * 统计所有场地的总量
	 * @param venueId	场馆ID
	 * @return
	 * @throws FieldException	找不到类型对应的值
	 */
	Long countAllFields(Long venueId) throws FieldException;

}

package net.yanhl.field.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import mlink.esms.SmsException;
import mlink.esms.SmsUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.dao.FieldDao;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.AccountOrder;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.pojo.activity.FrozenActivity;
import net.yanhl.field.service.FieldManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.member.util.MemberUtil;
import net.yanhl.price.service.PriceManager;
import net.yanhl.tactics.dao.TacticsDao;
import net.yanhl.util.DateUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.SysConstants;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 场地管理实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
@Service(value = "fieldManager")
@Transactional(rollbackFor = Exception.class)
public class FieldManagerImpl extends BaseManagerImpl implements FieldManager {

	Log log = LogFactory.getLog(this.getClass());

	/**
	 * 场地DAO接口
	 */
	@Resource
	protected FieldDao fieldDao;

	/**
	 * 场地类型DAO接口
	 */
	@Resource
	protected FieldTypeDao fieldTypeDao;

	/**
	 * 价格管理BO
	 */
	@Resource
	protected PriceManager priceManager;

	/**
	 * 价格策略DAO接口
	 */
	@Resource
	protected TacticsDao tacticsDao;

	@Transactional(readOnly = true)
	public Integer getMaxFieldNo(String fieldType, Long venueId) throws FieldException {
		return fieldDao.getMaxFieldNo(fieldType, venueId);
	}

	@Transactional(readOnly = true)
	public List<? extends BaseField> getFieldList(Long venueId, String fieldType) throws FieldException {
		List<? extends BaseField> fieldList = fieldDao.getFieldList(venueId, fieldType);
		return fieldList;
	}

	@Transactional(readOnly = true)
	public List<FieldActivity> getFieldActivity(Long fieldId, int nextStep, String fieldType) throws FieldException {
		return fieldDao.getFieldActivity(fieldId, nextStep, fieldType);
	}

	@Transactional(readOnly = true)
	public Map<String, String> getFieldActivityStatusForDate(final String fieldType, Long venueId, String fromDate)
			throws FieldException {
		return fieldDao.getFieldActivityStatusForDate(fieldType, venueId, fromDate);
	}

	@Transactional(readOnly = true)
	public Map<String, String> getFieldActivityStatusForPeriod(final String fieldType, Long venueId, String fromDate)
			throws FieldException {
		return fieldDao.getFieldActivityStatusForPeriod(fieldType, venueId, fromDate);
	}

	@Transactional(readOnly = true)
	public Map<String, List<Object[]>> getActivityGrid(Long venueId, String fieldType, String usableDate)
			throws FieldActivityException, FieldException {
		return fieldDao.getActivityGrid(venueId, fieldType, usableDate);
	}

	@Transactional(readOnly = true)
	public Map<String, String> getFieldActivityStatusForField(String fieldType, Long venueId, String usableDate,
			String period) throws FieldException {
		return fieldDao.getFieldActivityStatusForField(fieldType, venueId, usableDate, period);
	}

	public JSONObject addOrder(FieldOrder fieldOrder, String activityId, String fieldType, VenueUser venueUser,
			boolean patch, String payStyle) throws FieldException, ClassNotFoundException {
		if (StringUtils.isEmpty(activityId)) {
			throw new FieldException("没有活动ID");
		}

		JSONObject respResult = new JSONObject();
		Long longActivityId = new Long(activityId);
		Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
		Object object = get(fieldActivityPojoClass, longActivityId);
		if (object != null) {
			FieldActivity fieldActivity = (FieldActivity) object;
			fieldActivity.setOrderUser(venueUser.getUsername());

			// 生成验证码并设置验证状态为未验证
			String authenticode = FieldUtil.generateCode();
			fieldActivity.setAuthenticode(authenticode);

			/**
			 * 1、正常预订设置为“未验证“状态
			 * 2、补登计设置为”已验证"
			 */
			if (patch) {
				fieldActivity.setVerification(FieldUtil.VALIDATED_SUCCESS);
			} else {
				fieldActivity.setVerification(FieldUtil.NO_VALIDATED);
			}

			/**
			 * 1、正常预订设置为“未开始“状态
			 * 2、补登计设置为”锻炼"
			 */
			if (patch) {
				fieldActivity.setActivity(FieldUtil.FIELD_ACTIVITY_EXERCISE);
			} else {
				// 设置为“未开始”状态
				fieldActivity.setActivity(FieldUtil.FIELD_ACTIVITY_UNSTART);
			}

			/*
			 * 创建账户付款订单，和FieldOrder是1:n关系，但是为了性能不做关联关系
			 * 从场地订单复制
			 */
			AccountOrder accountOrder = new AccountOrder();
			accountOrder.setVenueId(fieldOrder.getVenueId());
			accountOrder.setContact(fieldOrder.getContact());
			accountOrder.setPayTotal(0d); // 临时设置一下，下面再更新
			accountOrder.setPhone(fieldOrder.getPhone());
			accountOrder.setPayPlatform(payStyle);
			accountOrder.setPlatformAccount(fieldOrder.getUserCode());
			accountOrder.setPayTime(new java.util.Date());// 临时用当前时间，下面更新为付款时间
			save(accountOrder);

			fieldOrder.setFieldType(fieldActivity.getFieldType());
			fieldOrder.setFieldId(fieldActivity.getField().getId());
			fieldOrder.setFieldActivity(fieldActivity);
			fieldOrder.setAccountOrderId(accountOrder.getId());

			// 设置标准价格
			fieldOrder.setStandardPrice(new Double(fieldActivity.getPrice()));

			// 保存订单、更新活动状态
			save(fieldOrder);

			// 更新活动
			fieldActivity.setFieldOrder(fieldOrder);
			update(fieldActivity);

			// 添加活动冻结记录
			FrozenActivity frozen = packageFrozenActivity(fieldActivity);
			baseDao.save(frozen);

			// 设置为增加订单成功
			respResult.element(FieldUtil.ADD_ORDER_RESULT, true);

			// 付款
			try {
				// 会员卡支付
				if (payStyle.equals(FieldUtil.PAY_STYLE_MEMBER)) {

					// 使用会员卡方式支付
					innerPayUseMemberCard(fieldOrder, patch, fieldActivity);

					// 设置冻结记录状态
					frozen.setState(FieldUtil.ACTIVITY_STATE_PAY_SUCCESS);

					baseDao.save(frozen);
				}
				// 现金支付
				else if (payStyle.equals(FieldUtil.PAY_STYLE_CASH)) {
					pay(fieldActivity, payStyle);
					frozen.setState(FieldUtil.ACTIVITY_STATE_PAY_SUCCESS);
				}

				// 更新付款状态
				accountOrder.setPayTotal(fieldOrder.getPaymentSum());
				accountOrder.setPaymentStatus(fieldOrder.getPaymentStatus());
				accountOrder.setPayTime(fieldOrder.getPaymentTime());
				save(accountOrder);

			} catch (MemberException e) {
				log.error("订单支付失败：", e);
				respResult.accumulate(FieldUtil.PAY_ERROR_INFO, e.getMessage());
			}

			log.debug("预订成功，生成订单" + fieldOrder + "成功");

			/*************************设置短信内容************************/
			boolean sendBookSms = false;
			// 补登计不发送短信
			if (patch) {
				// 模拟发送短信，设置发送短信成功标志为true
				sendBookSms = true;
			} else {
				try {
					sendBookSms = FieldUtil.sendBookSms(fieldOrder.getPhone(), venueUser.getVenueInfo(), fieldActivity);
				} catch (SmsException e) {
					log.warn("预定场地：", e);
					respResult.accumulate(SmsUtil.SEND_SMS_ERROR_INFO, e.getMessage());
				}
			}
			respResult.accumulate(SmsUtil.SEND_SMS_ERROR_LABLE, sendBookSms);

		}

		// 预订场地活动响应
		setAddOrderResponse(respResult, fieldOrder, venueUser);

		return respResult;
	}

	/**
	 * 使用会员卡支付
	 * @param fieldOrder
	 * @param canPayLessBalance
	 * @param fieldActivity
	 * @throws MemberException
	 * @throws FieldException
	 */
	private void innerPayUseMemberCard(FieldOrder fieldOrder, boolean canPayLessBalance, FieldActivity fieldActivity)
			throws MemberException, FieldException {
		// 没有会员卡时返回false
		MemberCard memberCard = findMemberCard(fieldOrder);

		// 创建会员卡使用记录
		CardUsageRecord usage = new CardUsageRecord();
		usage.setCardId(fieldOrder.getCardId());
		usage.setCardNo(fieldOrder.getUserCode());
		usage.setVenueId(fieldOrder.getVenueId());
		usage.setSignature(fieldOrder.getContact());
		usage.setUsageDate(new java.util.Date(System.currentTimeMillis()));

		// 活动时间
		String startTime = fieldActivity.getFromTime().toString().substring(0, 5);
		String endTime = fieldActivity.getToTime().toString().substring(0, 5);
		usage.setUsageTimeSlice(startTime + "~" + endTime);
		double paySum = calculateDiscountPrice(memberCard.getCardType(), fieldActivity.getPrice(),
				fieldActivity.getUsableDate());
		usage.setOptionTotal(paySum * -1);

		// 会员卡使用记录
		usage.setUsageType(MemberUtil.CARD_USAGE_TYPE_PAY);
		save(usage);

		// 补登计时如果余额不足也可以支付，支付完后手动给此会员卡充值
		payUseCard(fieldActivity, paySum, memberCard, usage, canPayLessBalance);
	}

	/**
	 * @param fieldOrder
	 * @return
	 * @throws MemberException
	 */
	private MemberCard findMemberCard(FieldOrder fieldOrder) throws MemberException {
		List<MemberCard> cards = findBy(MemberCard.class, "id", fieldOrder.getCardId());
		if (cards.isEmpty()) {
			fieldOrder.setPaymentStatus(false);
			insertOrUpdate(fieldOrder);
			String errorInfo = "没有找到此会员卡";
			log.warn("订单->" + fieldOrder + "，扣款失败，原因：" + errorInfo);
			throw new MemberException("没有找到此会员卡");
		}
		MemberCard memberCard = cards.get(0);
		return memberCard;
	}

	/**
	 * 生成预订场地活动响应
	 * @param responseResult
	 * @param fieldOrder
	 * @param venueUser
	 */
	private void setAddOrderResponse(JSONObject responseResult, FieldOrder fieldOrder, VenueUser venueUser) {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateToStringProcessorImpl());
		config.setExcludes(new String[] { "fieldActivity" });
		JSONObject orderInfo = JSONObject.fromObject(fieldOrder, config).accumulate("orderName",
				venueUser.getUsername());
		responseResult.accumulate("orderInfo", orderInfo);
	}

	public JSONObject addOrderForBatch(FieldOrder orgFieldOrder, String activityIds, String fieldType,
			VenueUser venueUser) throws FieldException, ClassNotFoundException, MemberException {
		if (StringUtils.isEmpty(activityIds)) {
			throw new FieldException("没有活动ID");
		}

		/*
		 * 创建账户付款订单，和FieldOrder是1:n关系，但是为了性能不做关联关系
		 * 从场地订单复制
		 */
		AccountOrder accountOrder = new AccountOrder();
		accountOrder.setVenueId(orgFieldOrder.getVenueId());
		accountOrder.setContact(orgFieldOrder.getContact());
		accountOrder.setPayTotal(0d); // 临时设置一下，下面再更新
		accountOrder.setPhone(orgFieldOrder.getPhone());
		accountOrder.setPayPlatform(FieldUtil.PAY_STYLE_MEMBER);
		accountOrder.setPlatformAccount(orgFieldOrder.getUserCode());
		accountOrder.setPayTime(new java.util.Date());// 临时用当前时间，下面更新为付款时间 
		save(accountOrder);

		JSONObject respResult = new JSONObject();
		List<JSONObject> respResults = new ArrayList<JSONObject>();
		String[] arrayActivityIds = StringUtils.split(activityIds, ",");
		List<FrozenActivity> frozenActivities = new ArrayList<FrozenActivity>();
		List<String> timeSlices = new ArrayList<String>();

		// 付款
		MemberCard memberCard = findMemberCard(orgFieldOrder);

		// 计算会员卡的金额是否可以支付所有活动的费用
		double allActivityStandardPrice = 0d; // 标准价格
		double allActivityDiscountPrice = 0d; // 打折价格
		for (int i = 0; i < arrayActivityIds.length; i++) {
			Long activityId = new Long(arrayActivityIds[i]);
			Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
			Object object = get(fieldActivityPojoClass, activityId);
			if (object != null) {
				FieldActivity fieldActivity = (FieldActivity) object;
				allActivityStandardPrice += fieldActivity.getPrice();
				double discountPrice = calculateDiscountPrice(memberCard.getCardType(), fieldActivity.getPrice(),
						fieldActivity.getUsableDate());
				allActivityDiscountPrice += discountPrice;
			}
		}
		
		// 会员卡余额不足
		Double memberCardBalance = memberCard.getBalance();
		if (memberCardBalance < allActivityDiscountPrice) {
			throw new FieldException("会员卡" + memberCard.getCardNumber() + "余额不足！");
		}

		// 创建会员卡使用记录
		CardUsageRecord memberCardUsage = new CardUsageRecord();
		memberCardUsage.setCardId(orgFieldOrder.getCardId());
		memberCardUsage.setCardNo(orgFieldOrder.getUserCode());
		memberCardUsage.setVenueId(orgFieldOrder.getVenueId());
		memberCardUsage.setSignature(orgFieldOrder.getContact());
		memberCardUsage.setUsageDate(new java.util.Date(System.currentTimeMillis()));
		memberCardUsage.setUsageType(MemberUtil.CARD_USAGE_TYPE_PAY);
		memberCardUsage.setOptionTotal(allActivityDiscountPrice * -1);
		save(memberCardUsage);

		// 已付款成功的总额
		for (int i = 0; i < arrayActivityIds.length; i++) {
			FieldOrder fieldOrder = new FieldOrder();
			try {
				PropertyUtils.copyProperties(fieldOrder, orgFieldOrder);
			} catch (IllegalAccessException e1) {
				log.error("copy new and old field order object: ", e1);
			} catch (InvocationTargetException e1) {
				log.error("copy new and old field order object: ", e1);
			} catch (NoSuchMethodException e1) {
				log.error("copy new and old field order object: ", e1);
			}

			JSONObject singleResult = new JSONObject();
			Long activityId = new Long(arrayActivityIds[i]);
			Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
			Object object = get(fieldActivityPojoClass, activityId);
			if (object != null) {
				FieldActivity fieldActivity = (FieldActivity) object;
				fieldActivity.setOrderUser(venueUser.getUsername());

				// 生成验证码并设置验证状态为未验证
				String authenticode = FieldUtil.generateCode();
				fieldActivity.setAuthenticode(authenticode);
				fieldActivity.setVerification(FieldUtil.NO_VALIDATED);

				// 设置为“未开始”状态
				fieldActivity.setActivity(FieldUtil.FIELD_ACTIVITY_UNSTART);

				fieldOrder.setFieldType(fieldActivity.getFieldType());
				fieldOrder.setFieldId(fieldActivity.getField().getId());
				fieldOrder.setFieldActivity(fieldActivity);
				fieldOrder.setAccountOrderId(accountOrder.getId());
				fieldOrder.setCardUsageId(memberCardUsage.getId());

				// 设置标准价格
				fieldOrder.setStandardPrice(new Double(fieldActivity.getPrice()));

				// 保存订单、更新活动状态
				fieldOrder.setId(null);
				
				// 支付信息
				double discountPrice = calculateDiscountPrice(memberCard.getCardType(), fieldActivity.getPrice(), fieldActivity.getUsableDate());
				fieldOrder.setActivityId(fieldActivity.getId());
				fieldOrder.setCardUsageId(memberCardUsage.getId());
				fieldOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));// 付款时间
				fieldOrder.setPaymentSum(discountPrice);// 付款金额
				fieldOrder.setPaymentStyle(FieldUtil.PAY_STYLE_MEMBER);// 会员卡支付方式
				fieldOrder.setPaymentStatus(true);
				save(fieldOrder);

				// 更新活动
				fieldActivity.setFieldOrder(fieldOrder);
				update(fieldActivity);
				
				// 添加活动冻结记录
				FrozenActivity frozen = packageFrozenActivity(fieldActivity);
				frozenActivities.add(frozen);
				
				// 活动时间
				String startTime = fieldActivity.getFromTime().toString().substring(0, 5);
				String endTime = fieldActivity.getToTime().toString().substring(0, 5);
				String timeSlice = startTime + "~" + endTime;
				timeSlices.add(timeSlice);

				log.debug("预订成功，生成订单" + fieldOrder + "成功");

				/*************************设置短信内容************************/
				boolean sendBookSms = false;
				try {
					sendBookSms = FieldUtil.sendBookSms(fieldOrder.getPhone(), venueUser.getVenueInfo(), fieldActivity);
				} catch (SmsException e) {
					log.warn("批量预定场地：", e);
					respResult.element(SmsUtil.SEND_SMS_ERROR_INFO, e.getMessage());
				}
				respResult.element(SmsUtil.SEND_SMS_ERROR_LABLE, sendBookSms);
			}

			setAddOrderResponse(singleResult, fieldOrder, venueUser);
			respResults.add(singleResult);
		}
		
		// 批量付款--单个用卡记录
		memberCard.setBalance(memberCardBalance - allActivityDiscountPrice);
		update(memberCard);
		
		// 使用记录的时段
		String[] timeSlicesArray = new String[timeSlices.size()];
		timeSlicesArray = timeSlices.toArray(timeSlicesArray);
		memberCardUsage.setUsageTimeSlice(ArrayUtils.toString(timeSlicesArray).replaceAll("\\{|\\}", ""));
		save(memberCardUsage);
		
		// 更新冻结记录状态
		for (FrozenActivity frozenActivity : frozenActivities) {
			frozenActivity.setState(FieldUtil.ACTIVITY_STATE_PAY_SUCCESS);
			baseDao.save(frozenActivity);
		}

		// 更新付款订单信息
		accountOrder.setPayTotal(allActivityDiscountPrice);
		accountOrder.setPaymentStatus(allActivityDiscountPrice > 0);
		save(accountOrder);

		// 返回结果
		respResult.accumulate("results", JSONArray.fromObject(respResults));
		return respResult;
	}

	/**
	 * 根据活动信息封装冻结记录对象
	 * @param fieldActivity	活动对象
	 */
	private FrozenActivity packageFrozenActivity(FieldActivity fieldActivity) {
		FrozenActivity frozen = new FrozenActivity();
		frozen.setActivityId(fieldActivity.getId());
		frozen.setState(FieldUtil.ACTIVITY_STATE_PAYING);
		frozen.setUserId(fieldActivity.getVenueId());
		frozen.setCreateAt(new java.util.Date());
		frozen.setUpdateAt(new java.util.Date());
		return frozen;
	}

	@SuppressWarnings("null")
	public void updateOrder(Long orderId, FieldOrder newFieldOrder, VenueUser venueUser) throws FieldException,
			SmsException {

		// 准备工作
		Object object = get(FieldOrder.class, orderId);
		if (object == null) {
			throw new FieldException("没有此订单，请刷新");
		}

		// 重新发送短信
		boolean resendSms = false;

		FieldOrder oldFieldOrder = (FieldOrder) object;

		// 是否修改了会员卡号
		boolean isSameUserCode = StringUtils.equals(oldFieldOrder.getUserCode(), newFieldOrder.getUserCode());

		// 手机号码是否一样
		boolean isSamePhone = StringUtils.equals(oldFieldOrder.getPhone(), newFieldOrder.getPhone());

		FieldActivity fieldActivity = null;
		// 1.1、订单已扣款
		if (oldFieldOrder.getPaymentStatus() == null || !oldFieldOrder.getPaymentStatus()) {

			if (!isSameUserCode) {
				throw new FieldException("已经扣款，不能修改会员卡号");
			} else {

				if (!isSamePhone) {
					// 未验证才发送短信
					fieldActivity = (FieldActivity) oldFieldOrder.getFieldActivity();
					if (!fieldActivity.getVerification().equals(FieldUtil.VALIDATED_SUCCESS)) {
						resendSms = true;
					}
					oldFieldOrder.setPhone(newFieldOrder.getPhone());
				}

			}

		} else { // 1.2、订单未扣款

			if (!isSameUserCode) {

				// 修改会员卡号不能修改手机
				if (!isSamePhone) {
					throw new FieldException("修改会员卡号的同时不能修改手机");
				}

				Object cardId = findBy(MemberCard.class, "id", "cardNumber", newFieldOrder.getUserCode());
				if (cardId == null) {
					throw new FieldException("输入的卡号不存在");
				}

				// 更换会员卡号
				Long longCardId = Long.parseLong(cardId.toString());
				oldFieldOrder.setCardId(longCardId);
				oldFieldOrder.setUserCode(newFieldOrder.getUserCode());

				try {
					innerPayUseMemberCard(oldFieldOrder, false, fieldActivity);
				} catch (MemberException e) {
					log.error("更新场地订单时支付费用失败：", e);
				}

			}

			if (!isSamePhone) {
				/*
				 * 修改了手机重新生成验证码更新到活动并发送新验证码到新手机号
				 */
				String authenticode = FieldUtil.generateCode();
				fieldActivity.setAuthenticode(authenticode);
				resendSms &= !fieldActivity.getVerification().equals(FieldUtil.VALIDATED_SUCCESS);
			}

		}

		// 数据库更新
		oldFieldOrder.setContact(newFieldOrder.getContact());
		oldFieldOrder.setPhone(newFieldOrder.getPhone());
		update(oldFieldOrder);

		/**
		 * 更新完订单后续工作
		 */
		// 是否重新发送短信
		if (resendSms) {
			FieldUtil.sendBookSms(oldFieldOrder.getPhone(), venueUser.getVenueInfo(), fieldActivity);
		}

	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public String getMaxIssueDays(Long venueId, String fieldType) throws FieldException, ClassNotFoundException {
		if (venueId == null || venueId == 0) {
			throw new FieldException("没有登录的用户信息");
		}
		Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);
		ListQuery listQuery = new ListQuery(fieldPojoClass);
		listQuery.setOwnerLabel(new String[] { "o.venueInfo.id", venueId.toString() });
		listQuery.getQueryFilter().put("o.name", new String[] { BaseQuery.AND, BaseQuery.LIKE });
		listQuery.getOrderByMap().put("o.advance", BaseQuery.ORDER_DSEC);

		List find = baseDao.find(listQuery);
		if (find.isEmpty()) {
			return "";
		} else {
			List fieldList = (List) find.get(1);
			Object advanceObj = fieldList.get(0);
			try {
				advanceObj = BeanUtils.getProperty(advanceObj, "advance");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return advanceObj.toString();
		}

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<? extends BaseField> getFieldList(Long venueId, String fieldType, String status) throws FieldException {
		return fieldDao.getFieldList(venueId, fieldType, status);
	}

	public boolean refundment(String fieldType, FieldOrder fieldOrder, Double refundSum, boolean deleteRecored)
			throws FieldException {

		if (fieldOrder == null) {
			throw new FieldException("没有此订单");
		}

		// 业务判断，退款总额不能大于实际支付金额
		if (refundSum > fieldOrder.getPaymentSum()) {
			log.info("退款总额不能大于实际支付金额{实际支付=" + fieldOrder.getPaymentSum() + ",退款金额=" + refundSum + "}");
			throw new FieldException("退款总额不能大于实际支付金额");
		}

		boolean refundmentResult = fieldDao.refundment(fieldType, fieldOrder, refundSum, deleteRecored);

		if (refundmentResult) {

			try {
				// 插入用卡记录
				CardUsageRecord usage = new CardUsageRecord();
				Long cardId = fieldOrder.getCardId();
				usage.setCardId(cardId);
				usage.setCardNo(fieldOrder.getUserCode());

				// 检查会员卡是否存在
				Object object = get(MemberCard.class, cardId);
				if (object == null) {
					log.error("订单->" + fieldOrder + "，插入用卡记录失败，原因：没有找到相应会员卡");
					throw new FieldException("会员卡编号[" + cardId + "]不存在");
				}

				usage.setVenueId(fieldOrder.getVenueId());
				usage.setSignature(fieldOrder.getContact());
				usage.setUsageDate(new java.util.Date(System.currentTimeMillis()));

				// 使用时段
				Calendar ca = Calendar.getInstance();
				String timeSlice = ca.get(Calendar.HOUR_OF_DAY) + ":" + ca.get(Calendar.MINUTE);
				usage.setUsageTimeSlice(timeSlice);
				usage.setOptionTotal(refundSum);
				usage.setUsageType(MemberUtil.CARD_USAGE_TYPE_REFUND);
				save(usage);
			} catch (RuntimeException e) {
				e.printStackTrace();
				throw new FieldException("内部错误");
			}

		}

		return refundmentResult;
	}

	public JSONObject orderHandle(String fieldType, String activityType, String activityIds, String status,
			VenueInfo venueInfo, Double refundmentSum) throws FieldActivityException, ClassNotFoundException,
			FieldException {

		if (StringUtils.isEmpty(activityIds)) {
			throw new FieldActivityException("没有活动ID");
		}

		JSONObject respResult = new JSONObject();
		this.orderHandle(fieldType, activityIds, status);

		// 取消、作废操作时清除验证码及状态
		String[] activityStatus = new String[] { FieldUtil.FIELD_ACTIVITY_CANCEL, FieldUtil.FIELD_ACTIVITY_BLANKOUT };
		if (StringUtil.hasInArray(activityStatus, status)) {

			/*
			 * 是否删除用卡记录
			 */
			boolean deleteRecored = false;
			if (status.equals(FieldUtil.FIELD_ACTIVITY_BLANKOUT)) {
				refundmentSum = 0.0;
				deleteRecored = false;
			}

			Class<?> activityClass = FieldUtil.getFieldActivityPojoClass(fieldType);
			// 策略表的备份活动
			if (activityType.equals("tactics")) {
				activityClass = FieldUtil.getFieldTacticsActivityPojoClass(fieldType);
			}
			FieldActivity fieldActivity = (FieldActivity) get(activityClass, new Long(activityIds));
			FieldOrder orgOrder = fieldActivity.getFieldOrder();
			FieldOrder order = new FieldOrder();
			try {
				order = (FieldOrder) BeanUtils.cloneBean(orgOrder);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			/**
			 * 1、退款：只退还到用会员卡支付的订单
			 */
			if ((order.getPaymentStatus() != null && order.getPaymentStatus()) // 确定已经支付过
					&& StringUtil.getValue(order.getPaymentStyle()).equals(FieldUtil.PAY_STYLE_MEMBER)) {// 支付方式为会员卡
				try {
					refundment(fieldType, order, refundmentSum, deleteRecored);
				} catch (FieldException e) {
					e.printStackTrace();
					// 退款错误信息
					respResult.accumulate(FieldUtil.REFUNDMENT_ERROR_LABEL, false);
					respResult.accumulate(FieldUtil.REFUNDMENT_ERROR_INFO, e.getMessage());
				}
			}

			/**
			 * 从策略备份活动列表操作时需要删除活动
			 */
			if (activityType.equals("tactics")) {
				// 1.1、删除策略表备份活动
				baseDao.delete(activityClass, new Long(activityIds));
			}

			// 2.1、取消活动和订单的绑定
			fieldActivity.setFieldOrder(null);
			getCurrentSession().flush();

			// 2.2、删除订单信息
			fieldDao.deleteOrderByActivity(fieldType, activityIds);

			// 退款成功
			respResult.accumulate("handle", true);

			if (status.equals(FieldUtil.FIELD_ACTIVITY_CANCEL)) {
				//3、 发送取消预订短信通知
				boolean sendBookSms = false;
				try {
					FieldUtil.sendUnbookSms(order.getPhone(), venueInfo, fieldActivity);
				} catch (SmsException e) {
					log.error("[取消预定]短信发送失败:", e);
					respResult.accumulate(SmsUtil.SEND_SMS_ERROR_INFO, e.getMessage());
				}
				respResult.accumulate(SmsUtil.SEND_SMS_ERROR_LABLE, sendBookSms);
			}

		}

		return respResult;
	}

	public boolean orderHandle(String fieldType, String activityIds, String status) throws FieldActivityException {
		boolean result = false;

		if (activityIds == null) {
			return false;
		}

		try {
			result = fieldDao.changeActivityStatus(fieldType, activityIds, status);
		} catch (Exception e) {
			e.printStackTrace();
			throw new FieldActivityException("更改状态失败");
		}

		return result;
	}

	public String[] validateCode(Long longActivityId, String fieldType, String validateCode)
			throws ClassNotFoundException, FieldException {

		String[] result = { SysConstants.RESPONSE_SUCCESS, "" };

		Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
		Object object = get(fieldActivityPojoClass, longActivityId);
		if (object != null) {
			FieldActivity fieldActivity = (FieldActivity) object;

			// 验证动作
			if (StringUtil.getValue(fieldActivity.getAuthenticode()).equalsIgnoreCase(validateCode)) {

				// 1、更新验证状态
				fieldActivity.setVerification(FieldUtil.VALIDATED_SUCCESS);

				// 2、更新锻炼状态为--锻炼
				fieldActivity.setActivity(FieldUtil.FIELD_ACTIVITY_EXERCISE);
				update(fieldActivity);

				// 验证成功
				log.info("场地" + fieldActivity + "验证成功，验证码：" + validateCode);
			} else {
				// 验证失败
				result[0] = SysConstants.RESPONSE_ERROR;
				result[1] = "输入的验证码错误";
				log.info("场地" + fieldActivity + "验证失败，验证码：" + validateCode + "，正确验证码："
						+ fieldActivity.getAuthenticode());
			}
		} else {
			result[0] = SysConstants.RESPONSE_ERROR;
			result[1] = "没有找到场地活动对象";
		}

		return result;
	}

	public void payUseCard(FieldActivity fieldActivity, double paySum, MemberCard memberCard,
			CardUsageRecord cardUsageRecord, boolean canPayLessBalance) throws FieldException, MemberException {
		FieldOrder fieldOrder = fieldActivity.getFieldOrder();
		if (fieldOrder == null) {
			throw new FieldException("订单为空");
		}

		if (memberCard != null) {

			Double balance = memberCard.getBalance();
			Double curBalance = 0d;

			// 扣款后的余额
			curBalance = balance - paySum;

			if (curBalance < 0 && !canPayLessBalance) {
				fieldOrder.setPaymentStatus(false);
				log.warn("订单" + fieldOrder + "，会员卡：[NO.=" + memberCard.getCardNumber() + ",name="
						+ memberCard.getName() + "]，因余额不足扣款失败");
				throw new MemberException("会员卡余额不足");
			}

			// 会员卡剩余额度
			memberCard.setBalance(curBalance);

			// 更新会员卡
			update(memberCard);

			// 设置订单信息
			fieldOrder.setActivityId(fieldActivity.getId());
			fieldOrder.setCardUsageId(cardUsageRecord.getId());
			fieldOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));// 付款时间
			fieldOrder.setPaymentSum(paySum);// 付款金额
			fieldOrder.setPaymentStyle(FieldUtil.PAY_STYLE_MEMBER);// 会员卡支付方式
			fieldOrder.setPaymentStatus(true);
			insertOrUpdate(fieldOrder);
			log.debug("订单->" + fieldOrder + "，成功扣款");

		} else {
			String errorInfo = "没有提供会员卡号";
			log.warn("订单->" + fieldOrder + "，扣款失败，原因：" + errorInfo);
			throw new MemberException(errorInfo);
		}
	}

	/**
	 * 根据会员卡的类型计算折扣后的价格
	 * @param cardType		会员卡类型
	 * @param standardPrice	标准价格
	 * @param activityDate	活动日期
	 * @return
	 * @throws FieldException 
	 */
	private double calculateDiscountPrice(CardType cardType, Integer standardPrice, java.util.Date activityDate)
			throws FieldException {
		double paySum = 0d;

		// 是否周末
		boolean weekday = DateUtil.isWeekend(activityDate);

		// 是否节假日
		boolean holiday = false;
		try {
			holiday = DateUtil.isHoliday(activityDate);
		} catch (ConfigurationException e) {
			throw new FieldException(e);
		}
		if ((holiday && cardType.getHoliDay()) || (weekday && cardType.getWeekDay()) || cardType.getCommonDay()) {
			// 处理扣款方式：折扣率、折扣额
			if (cardType.getDiscountType().equals(MemberUtil.CARD_DISCOUNT_TYPE_RATE)) {
				Long rate = cardType.getDiscountRate();// 折扣率
				paySum += standardPrice * (Double.parseDouble(rate.toString())) / 100.0;
			} else if (cardType.getDiscountType().equals(MemberUtil.CARD_DISCOUNT_TYPE_QUOTA)) {// 折扣额
				paySum += cardType.getDiscountPrice();
			}
		} else {
			paySum += standardPrice;
		}
		return paySum;
	}

	public boolean pay(FieldActivity fieldActivity, String paymentStyle) throws FieldException, MemberException {

		FieldOrder fieldOrder = fieldActivity.getFieldOrder();
		if (fieldOrder == null) {
			throw new FieldException("订单为空");
		}

		Double paymentSum = 0d;
		Double standardPrice = 0d;

		paymentSum += fieldActivity.getPrice() * 1.0;
		standardPrice += fieldActivity.getPrice() * 1.0;

		// 设置订单信息
		fieldOrder.setPaymentTime(new Timestamp(System.currentTimeMillis()));// 付款时间
		fieldOrder.setPaymentSum(paymentSum);// 付款金额
		fieldOrder.setStandardPrice(standardPrice);// 标准价格
		fieldOrder.setPaymentStyle(paymentStyle);// 会员卡支付方式
		fieldOrder.setPaymentStatus(true);
		update(fieldOrder);

		// 更新活动冻结的状态
		Object loadById = baseDao.loadById(FrozenActivity.class, fieldActivity.getId());
		if (loadById != null) {
			FrozenActivity frozen = (FrozenActivity) loadById;
			frozen.setState(FieldUtil.ACTIVITY_STATE_PAY_SUCCESS);
			frozen.setUpdateAt(new java.util.Date());
			baseDao.save(frozen);
		}
		log.debug("场地活动，" /*+ fieldActivity*/+ ",以<" + paymentStyle + ">成功支付" + paymentSum + "元");
		return true;
	}

	public Integer deleteActivity(Long venueId, String fieldType, Object[] usableDates) {
		return fieldDao.deleteActivity(venueId, fieldType, usableDates);
	}

	@Transactional(readOnly = true)
	public Long countInvalidActivity(Long venueId, String fieldType, Date usableDate) throws FieldException {
		return fieldDao.countInvalidActivity(venueId, fieldType, usableDate);
	}

	@Transactional(readOnly = true)
	public Long countVerificationStatusActivity(Long venueId, String fieldType, Date usableDate,
			boolean verificationStatus) throws FieldException {
		return fieldDao.countVerificationStatusActivity(venueId, fieldType, usableDate, verificationStatus);
	}

	@Transactional(readOnly = true)
	public Long countFields(Long venueId, String fieldType) throws FieldException {
		return fieldDao.countFields(venueId, fieldType);
	}

	@Transactional(readOnly = true)
	public Long countAllFields(Long venueId) throws FieldException {
		Long fieldSize = 0l;
		String[] types = fieldTypeDao.getEnableFieldTypes(venueId);
		for (String type : types) {
			Long countFields = this.countFields(venueId, type);
			fieldSize += countFields;
		}
		log.info("场馆ID=" + venueId + "，统计所有场地数量：" + fieldSize);
		return fieldSize;
	}
}
package net.yanhl.field.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mlink.esms.SmsException;
import mlink.esms.SmsUtil;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.badminton.FieldBadmintoon;
import net.yanhl.field.pojo.badminton.FieldBadmintoonActivity;
import net.yanhl.field.pojo.badminton.FieldBadmintoonActivityTactics;
import net.yanhl.field.pojo.football.FieldFootball;
import net.yanhl.field.pojo.football.FieldFootballActivity;
import net.yanhl.field.pojo.football.FieldFootballActivityTactics;
import net.yanhl.field.pojo.tennis.FieldTennicActivityTactics;
import net.yanhl.field.pojo.tennis.FieldTennis;
import net.yanhl.field.pojo.tennis.FieldTennisActivity;
import net.yanhl.field.pojo.type.FieldType;
import net.yanhl.util.DateUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.venue.pojo.VenueInfo;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><b>Title：</b>场地工具类</p>
 * <p><b>Description：</b>
 * <ul>
 * 	<li>定义场地的状态、英文标示、以及支付的常量</li>
 *  <li>定义通过场地类型获取各种数据的方法</li>
 *  <li>定义通过场地类型和价格类型获取价格对象的方法</li>
 *  <li>预定、取消预定场地的短信发送方法</li>
 * </ul>
 * </p>
 *
 * @author 闫洪磊
 */
public class FieldUtil {

	static Log log = LogFactory.getLog(FieldUtil.class);

	/**
	 * 生成场地活动提前天数
	 */
	public static final int ADVANCE_DAYS = 7;

	/**
	 * 场地活动.状态.取消
	 */
	public static final String FIELD_ACTIVITY_CANCEL = "取消";

	/**
	 * 场地活动.状态.保留
	 */
	public static final String FIELD_ACTIVITY_PRESERVING = "保留";

	/**
	 * 场地活动.状态.取消保留
	 */
	public static final String FIELD_ACTIVITY_UNPRESERVING = "取消保留";

	/**
	 * 场地活动.锻炼情况.未预订
	 */
	public static final String FIELD_ACTIVITY_UNRESERVED = "未预订";

	/**
	 * 场地活动.锻炼情况.未开始
	 */
	public static final String FIELD_ACTIVITY_UNSTART = "未开始";

	/**
	 * 场地活动.状态.作废
	 */
	public static final String FIELD_ACTIVITY_BLANKOUT = "作废";

	/**
	 * 场地活动.状态.锻炼
	 */
	public static final String FIELD_ACTIVITY_EXERCISE = "锻炼";

	/**
	 * 场地活动.验证成功标志
	 */
	public static final String VALIDATED_SUCCESS = "1";

	/**
	 * 场地活动.未验证标志
	 */
	public static final String NO_VALIDATED = "0";

	/**
	 * 场地类型.羽毛球
	 */
	public static final String FIELD_TYPE_BADMINTOON = "badmintoon";

	/**
	 * 场地类型.网球
	 */
	public static final String FIELD_TYPE_TENNIS = "tennis";
	
	/**
	 * 场地类型.足球
	 */
	public static final String FIELD_TYPE_FOOTBALL = "football";

	/**
	 * 支付方式.会员卡
	 */
	public static final String PAY_STYLE_MEMBER = "会员卡";

	/**
	 * 支付方式.现金
	 */
	public static final String PAY_STYLE_CASH = "现金";

	/**
	 * 支付方式.信用卡
	 */
	public static final String PAY_STYLE_CREDITCARD = "信用卡";
	
	/**
	 * 活动冻结表中的state状态--预定中
	 */
	public static final int ACTIVITY_STATE_BOOKING = 0;
	
	/**
	 * 活动冻结表中的state状态--支付中
	 */
	public static final int ACTIVITY_STATE_PAYING = 1;
	
	/**
	 * 活动冻结表中的state状态--支付成功
	 */
	public static final int ACTIVITY_STATE_PAY_SUCCESS = 2;

	/**
	 * 响应.订单生成结果
	 */
	public static final String ADD_ORDER_RESULT = "addOrder";

	/**
	 * 响应.订单支付失败
	 */
	public static final String PAY_ERROR_INFO = "payError";

	/**
	 * 退款.失败标志
	 */
	public static final String REFUNDMENT_ERROR_LABEL = "refundLabel";

	/**
	 * 退款.失败原因
	 */
	public static final String REFUNDMENT_ERROR_INFO = "refundError";

	/**
	 * 场地POJO<英文名称, 全路径类名><br/>
	 * 例如：<code><badmintoon, net.yanhl.field.pojo.FieldBadmintoon></code>
	 */
	private static Map<String, String> FIELD_POJO_FULLNAME = new HashMap<String, String>();

	/**
	 * 场地活动表<英文名称, 中文名称>
	 */
	private static Map<String, String> FIELD_EN_ZH_NAME = new HashMap<String, String>();

	/**
	 * 场地Class--{场地类型英文名称，[场地类型中文名称, 场地类型英文名称]}
	 */
	private static Map<Class<? extends BaseField>, String[]> FIELD_POJO_NAMES = new HashMap<Class<? extends BaseField>, String[]>();

	/**
	 * 场地活动表<英文名称, 数据库表名>
	 */
	private static Map<String, String> FIELD_ACTIVITY_TABLENAME = new HashMap<String, String>();

	/**
	 * 场地策略活动表<英文名称, 数据库表名>
	 */
	private static Map<String, String> FIELD_TACTICS_ACTIVITY_TABLENAME = new HashMap<String, String>();

	/**
	 * 场地活动POJO<英文名称, 全路径类名>
	 */
	private static Map<String, String> FIELD_ACTIVITY_POJO_FULLNAME = new HashMap<String, String>();

	/**
	 * 场地策略活动POJO<英文名称, 全路径类名>
	 */
	private static Map<String, String> FIELD_TACTICS_ACTIVITY_POJO_FULLNAME = new HashMap<String, String>();

	static {
		/************************场地POJO<英文名称, 全路径类名>************************/
		FIELD_EN_ZH_NAME.put(FIELD_TYPE_BADMINTOON, "羽毛球");
		FIELD_EN_ZH_NAME.put(FIELD_TYPE_TENNIS, "网球");
		FIELD_EN_ZH_NAME.put(FIELD_TYPE_FOOTBALL, "足球");

		/************************场地POJO<英文名称, 全路径类名>************************/
		// 羽毛球
		FIELD_POJO_FULLNAME.put(FIELD_TYPE_BADMINTOON, FieldBadmintoon.class.getName());

		// 网球
		FIELD_POJO_FULLNAME.put(FIELD_TYPE_TENNIS, FieldTennis.class.getName());
		
		// 足球
		FIELD_POJO_FULLNAME.put(FIELD_TYPE_FOOTBALL, FieldFootball.class.getName());

		/************************场地Class--场地中、英文名称************************/
		FIELD_POJO_NAMES.put(FieldBadmintoon.class, new String[] {FIELD_TYPE_BADMINTOON, "羽毛球"});
		FIELD_POJO_NAMES.put(FieldTennis.class, new String[] {FIELD_TYPE_TENNIS, "网球"});
		FIELD_POJO_NAMES.put(FieldFootball.class, new String[] {FIELD_TYPE_FOOTBALL, "足球"});

		/************************场地活动数据库表名************************/
		FIELD_ACTIVITY_TABLENAME.put(FIELD_TYPE_BADMINTOON, "t_field_badmintoon_activity");
		FIELD_ACTIVITY_TABLENAME.put(FIELD_TYPE_TENNIS, "t_field_tennis_activity");
		FIELD_ACTIVITY_TABLENAME.put(FIELD_TYPE_FOOTBALL, "t_field_football_activity");

		/************************场地策略活动数据库表名************************/
		FIELD_TACTICS_ACTIVITY_TABLENAME.put(FIELD_TYPE_BADMINTOON, "t_field_badmintoon_activity_tactics");
		FIELD_TACTICS_ACTIVITY_TABLENAME.put(FIELD_TYPE_TENNIS, "t_field_tennis_activity_tactics");
		FIELD_TACTICS_ACTIVITY_TABLENAME.put(FIELD_TYPE_FOOTBALL, "t_field_tennis_activity_tactics");

		/************************场地活动POJO************************/
		FIELD_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_BADMINTOON, FieldBadmintoonActivity.class.getName());
		FIELD_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_TENNIS, FieldTennisActivity.class.getName());
		FIELD_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_FOOTBALL, FieldFootballActivity.class.getName());

		/************************场地策略活动POJO************************/
		FIELD_TACTICS_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_BADMINTOON, FieldBadmintoonActivityTactics.class.getName());
		FIELD_TACTICS_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_TENNIS, FieldTennicActivityTactics.class.getName());
		FIELD_TACTICS_ACTIVITY_POJO_FULLNAME.put(FIELD_TYPE_FOOTBALL, FieldFootballActivityTactics.class.getName());

	}
	
	/**
	 * 获得字符串形式的场地类型POJO名称
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldPojoName(String fieldType) throws FieldException {
		String result = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			result = FIELD_POJO_FULLNAME.get(fieldType);
			if (result == null) {
				throw new FieldException("没有对应的场地POJO--" + fieldType);
			}
		}
		return result;
	}

	/**
	 * 获得场地类型POJO的Class
	 * @param 	fieldType	场地类型
	 * @return	和fieldType对应的实体类Class
	 * @throws ClassNotFoundException 没有找到和此类型对应的实体类
	 * @throws FieldException 找不到类型对应的值
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends BaseField> getFieldPojoClass(String fieldType) throws ClassNotFoundException, FieldException {
		String pojoName = getFieldPojoName(fieldType);
		return (Class<? extends BaseField>) Class.forName(pojoName);
	}

	/**
	 * 获得POJO对象对应的场地英文类型
	 * @param clazz	场地POJO的class
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldTypeByPojo(Class<? extends BaseField> clazz) throws FieldException {
		String result = null;
		Class<?> newClazz = null;
		if (clazz == null) {
			throw new FieldException("场地类型Class不能为空");
		} else {
			// 因为hibernate代理问题，会在class后面加上_$$符号，所以这里做一些处理
			String className = clazz.getName();
			int indexOf = className.indexOf("_$$");
			if (indexOf != -1) {
				try {
					className = className.substring(0, indexOf);
					newClazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				newClazz = clazz;
			}
			String[] names = FIELD_POJO_NAMES.get(newClazz);
			result = names[0];
			if (result == null) {
				throw new FieldException("没有和" + clazz + "对应的场地英文标示");
			}
		}
		return result;
	}

	/**
	 * 获得POJO对象对应的场地中文类型
	 * @param clazz	场地POJO的class
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldZhTypeByPojo(Class<? extends BaseField> clazz) throws FieldException {
		String result = null;
		if (clazz == null) {
			throw new FieldException("场地类型Class不能为空");
		} else {
			String[] names = FIELD_POJO_NAMES.get(clazz);
			result = names[1];
			if (result == null) {
				throw new FieldException("没有和" + clazz + "对应的场地中文标示");
			}
		}
		return result;
	}

	/**
	 * 获得中文场地类型
	 * @param fieldType	英文的类型
	 * @return	对应中文类型
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldZhType(String fieldType) throws FieldException {
		String zhname = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			zhname = FIELD_EN_ZH_NAME.get(fieldType);
			if (zhname == null) {
				throw new FieldException("没有和" + fieldType + "对应的场地中文标示");
			}
		}
		return zhname;
	}

	/**
	 * 获得字符串形式的场地活动类型表名
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldActivityTableName(String fieldType) throws FieldException {
		String tableName = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			tableName = FIELD_ACTIVITY_TABLENAME.get(fieldType);
			if (tableName == null) {
				throw new FieldException("没有找到和 " + fieldType + " 对应的数据库表名");
			}
		}
		return tableName;
	}

	/**
	 * 获得字符串形式的场地活动类型表名
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldActivityTacticsTableName(String fieldType) throws FieldException {
		String tableName = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			tableName = FIELD_TACTICS_ACTIVITY_TABLENAME.get(fieldType);
			if (tableName == null) {
				throw new FieldException("没有找到和 " + fieldType + " 对应的策略活动备份数据库表名");
			}
		}
		return tableName;
	}

	/**
	 * 获得字符串形式的场地活动类型POJO名
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldActivityPojoName(String fieldType) throws FieldException {
		String pojoName = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			pojoName = FIELD_ACTIVITY_POJO_FULLNAME.get(fieldType);
			if (pojoName == null) {
				throw new FieldException("没有找到和 " + fieldType + " 对应的Pojo名称");
			}
		}
		return pojoName;
	}

	/**
	 * 获得字符串形式的场地活动类型POJO的class
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends FieldActivity> getFieldActivityPojoClass(String fieldType)
			throws ClassNotFoundException, FieldException {
		String pojoName = getFieldActivityPojoName(fieldType);
		return (Class<? extends FieldActivity>) Class.forName(pojoName);
	}

	/**
	 * 获得字符串形式的场地策略活动POJO名
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	public static String getFieldTacticsActivityPojoName(String fieldType) throws FieldException {
		String pojoName = null;
		if (StringUtils.isEmpty(fieldType)) {
			throw new FieldException("场地类型不能为空");
		} else {
			pojoName = FIELD_TACTICS_ACTIVITY_POJO_FULLNAME.get(fieldType);
			if (pojoName == null) {
				throw new FieldException("没有找到和 " + fieldType + " 对应的策略备份活动Pojo名称");
			}
		}
		return pojoName;
	}

	/**
	 * 获得字符串形式的场地活动类型POJO的class
	 * @param fieldType	场地类型
	 * @return
	 * @throws FieldException 找不到类型对应的值
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends FieldActivity> getFieldTacticsActivityPojoClass(String fieldType)
			throws ClassNotFoundException, FieldException {
		String pojoName = getFieldTacticsActivityPojoName(fieldType);
		return (Class<? extends FieldActivity>) Class.forName(pojoName);
	}
	
	/**
	 * 从场地类型对象中抽取场地类型标示字符
	 * @param fieldTypeList
	 * @return	数组形式的场地类型标示（英文）
	 */
	public static String[] getEnableFieldTypes(List<FieldType> fieldTypeList) {
		String[] fieldTyps = new String[fieldTypeList.size()];
		for (int i = 0; i < fieldTypeList.size(); i++) {
			fieldTyps[i] = fieldTypeList.get(i).getTypeId();
		}
		return fieldTyps;
	}
	
	/**
	 * 根据英文标识返回对应的场地类型中英文对照
	 * @param enableFieldTypes
	 * @return	Map<英文, 中文>
	 * @throws FieldException 找不到对应中文类型时
	 */
	public static Map<String, String> getEnableFieldTypes(String[] enableFieldTypes) throws FieldException {
		Map<String, String> fieldTypeEnAndZhName = new HashMap<String, String>();
		for (String enName : enableFieldTypes) {
			fieldTypeEnAndZhName.put(enName, getFieldZhType(enName));
		}
		return fieldTypeEnAndZhName;
	}


	/**
	 * 生成4位数字+字母的验证码
	 * @return
	 */
	public static String generateCode() {
		Random ran = new Random();
		int[] ci = { 48, 97, 65 };
		char[] c = new char[4];
		for (int i = 0; i < c.length; i++) {
			int temp = ci[ran.nextInt(3)];
			if (temp != 48)
				c[i] = (char) (temp + ran.nextInt(26));
			else
				c[i] = (char) (temp + ran.nextInt(9));
		}
		String code = new String(c);
		return code.toUpperCase();
	}

	/**
	 * 判断场地是否可预订
	 * @param activity	场地状态
	 * @return	可预订true，否则false
	 */
	public static boolean canBook(String activity) {
		if(StringUtils.isEmpty(activity)) {
			return true;
		}

		if(activity.equals(FIELD_ACTIVITY_UNRESERVED)// 未预订
				|| activity.equals(FIELD_ACTIVITY_CANCEL)// 取消
				) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断活动的当前状态
	 * @param obj	场地活动
	 * @return	中文状态
	 */
	public static String getActivityStatus(Object obj) {
		FieldActivity activity = (FieldActivity) obj;
		String status = FieldUtil.FIELD_ACTIVITY_UNRESERVED;

		String[] tt = { "作废", "过期" };
		if (activity.getActivity() != null && StringUtil.hasInArray(tt, activity.getActivity())) {
			status = activity.getActivity();
			return status;
		}

		Date serverTime = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(serverTime);
		int serverHour = ca.get(Calendar.HOUR_OF_DAY);
		int serverMinute = ca.get(Calendar.MINUTE);

		String[] fromTimePriod = activity.getFromTime().toString().split(":");
		String[] toTimePriod = activity.getToTime().toString().split(":");

		int activityFromHour = new Integer(fromTimePriod[0]) * 1;
		int activityFromMinute = new Integer(fromTimePriod[1]) * 1;

		int activityToHour = new Integer(toTimePriod[0]) * 1;
		int activityToMinute = new Integer(toTimePriod[1]) * 1;

		// 1、是否已预订
		if(activity.getFieldOrder() != null) {

			// 2.1、是否已付款
			if (BooleanUtils.isTrue(activity.getFieldOrder().getPaymentStatus())) {

				// 3.1、是否验证通过
				if(activity.getVerification() == null || activity.getVerification().equals(VALIDATED_SUCCESS)) {

					// 4.1、是否为当天
					if(DateUtil.isSameDate(activity.getUsableDate(), serverTime)) {

						// 4.2、按时间段判断状态
						if(activity.getActivity().equals(FieldUtil.FIELD_ACTIVITY_EXERCISE)) {
							if (activityFromHour <= serverHour
									&& (
											(activityToHour == serverHour && activityFromMinute > serverMinute)
											|| (activityToHour > serverHour)
										)
									) {// 4.2.1 、当前时间在预订的时间范围内
								status = "正在锻炼";
							} else if (activity.getUsableDate().getTime() < serverTime.getTime()) {
								status = "锻炼结束";
							} else if (activityToHour <= serverHour && activityToMinute <= serverMinute) {// 4.2.2、系统时间之前
								status = "过期";
							} else if(activityFromHour > serverHour) {// 4.2.3 、未到锻炼时间
								status = "等待锻炼";
							} else { // 已过锻炼结束时间
								status = "未知";
							}
						} else if (activityToHour <= serverHour && activityToMinute <= serverMinute) {// 4.2.2、系统时间之前
							status = "过期";
						} else if(activityFromHour > serverHour) {// 4.2.3 、未到锻炼时间
							status = "等待锻炼";
						}
					} else {
						if (activity.getUsableDate().getTime() < serverTime.getTime()) {
							status = "锻炼结束";
						}
					}

				} else { // 3.2、没有验证或验证失败
					status = "等待验证";
				}

			} else { // 2.2、 未付款
				status = "等待付款";
			}
		}

		/*System.out.println(serverHour + "\t" + activityFromHour + ":" + activityFromMinute
			  + "\t" + activityToHour + ":" + activityToMinute
			  + "\t" + status);*/

		return status;
	}

	/**
	 * 倒计时计算器
	 * @param hour	终止小时
	 * @param minute	终止分钟
	 * @return	当前时间减去指定小时、分钟剩下的秒数
	 */
	public static long getCountDown(int hour, int minute) {
		Calendar serverCa = Calendar.getInstance();
		Date serverTime = serverCa.getTime();

		// 结束时间
		Calendar endCa = Calendar.getInstance();
		endCa.setTime(serverTime);
		endCa.set(Calendar.HOUR_OF_DAY, hour);
		endCa.set(Calendar.MINUTE, minute);
		endCa.set(Calendar.SECOND, 0);

		long countDown = (endCa.getTimeInMillis() - serverTime.getTime()) / 1000;
		return countDown;
	}

	/**
	 * 发送预订成功短信
	 * @param mobilePhone	手机号码
	 * @param venueName		场馆名称
	 * @param fieldActivity	场地活动对象
	 * @throws ConfigurationException	获取短信配置文件出错时
	 * @throws SmsException 短信发送失败
	 */
	public static boolean sendBookSms(String mobilePhone, VenueInfo venueInfo, FieldActivity fieldActivity)
			throws SmsException {
		if (venueInfo.getSendSms() == null || !venueInfo.getSendSms()) {
			throw new SmsException("没有开启短信功能");
		}
		Map<String, String> messages = new HashMap<String, String>();
		messages.put("venueName", venueInfo.getVenueName());// 场馆名称
		Calendar ca = Calendar.getInstance();
		ca.setTime(fieldActivity.getUsableDate());
		String date = ca.get(Calendar.YEAR) + "/" + (ca.get(Calendar.MONTH) + 1) + "/"
				+ ca.get(Calendar.DATE);
		messages.put("date", date);// 日期
		messages.put("period", fieldActivity.getPeriod());// 时段
		messages.put("item", "羽毛球");// 项目
		messages.put("code", fieldActivity.getAuthenticode());// 验证码

		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration("conf/sms.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			log.error("预订活动时读取短信配置出错：" + e);
			throw new SmsException(e);
		}
		String smsContent = config.getString("sms.booking");
		Iterator<String> keys = messages.keySet().iterator();
		for (Iterator<String> iterator = keys; iterator.hasNext();) {
			String next = iterator.next();
			smsContent = smsContent.replaceAll("#" + next, messages.get(next));
		}

		boolean sendSingleSms = SmsUtil.sendSingleSms(mobilePhone, smsContent);
		return sendSingleSms;
	}

	/**
	 * 发送场地取消通知短信
	 * @param venueInfo
	 * @param fieldActivity
	 * @throws SmsException 短信发送失败时
	 */
	public static boolean sendUnbookSms(String mobilePhone, VenueInfo venueInfo, FieldActivity fieldActivity) throws SmsException {
		if (venueInfo.getSendSms() == null || !venueInfo.getSendSms()) {
			throw new SmsException("没有开启短信功能");
		}
		Map<String, String> messages = new HashMap<String, String>();
		messages.put("venueName", fieldActivity.getFieldName());// 场地名称
		messages.put("date", DateUtil.formatZh(fieldActivity.getUsableDate(), DateUtil.TYPE_DATE));// 日期
		messages.put("period", fieldActivity.getPeriod());// 时段
		messages.put("telphone", venueInfo.getPhone());// 电话号码

		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration("conf/sms.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			log.error("预订活动时读取短信配置出错：" + e);
			throw new SmsException(e);
		}
		String smsContent = config.getString("sms.unbooking");
		Iterator<String> keys = messages.keySet().iterator();
		for (Iterator<String> iterator = keys; iterator.hasNext();) {
			String next = iterator.next();
			smsContent = smsContent.replaceAll("#" + next, messages.get(next));
		}

		boolean sendSingleSms = SmsUtil.sendSingleSms(mobilePhone, smsContent);
		return sendSingleSms;
	}

}

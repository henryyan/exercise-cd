package net.yanhl.member.util;

import java.sql.Date;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b> 会员卡工具类</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090919
 */
public class MemberUtil {

	/**
	 * 会员卡使用类型-入会
	 */
	public static final String CARD_USAGE_TYPE_JOIN = "入会";

	/**
	 * 会员卡使用类型-支付
	 */
	public static final String CARD_USAGE_TYPE_PAY = "支付";
	
	/**
	 * 会员卡使用类型-购买零售商品
	 */
	public static final String CARD_USAGE_TYPE_RETAIL = "购买零售品";

	/**
	 * 会员卡使用类型-退款
	 */
	public static final String CARD_USAGE_TYPE_REFUND = "退款";

	/**
	 * 会员卡使用类型-充值
	 */
	public static final String CARD_USAGE_TYPE_RECHARGE = "充值";

	/**
	 * 会员卡折扣方式-折扣率
	 */
	public static final String CARD_DISCOUNT_TYPE_RATE = "1";

	/**
	 * 会员卡折扣方式-折扣额
	 */
	public static final String CARD_DISCOUNT_TYPE_QUOTA = "2";
	
	/**
	 * 默认的会员卡有效期月数
	 */
	public static final int DEFAULT_MEMBER_CARD_PERIOD_VALIDITY = 12;
	
	/**
	 * 会员卡类型有效期-年
	 */
	public static final int MEMBER_CARD_TYPE_PERIOD_YEAR = 1;
	
	/**
	 * 会员卡类型有效期-月
	 */
	public static final int MEMBER_CARD_TYPE_PERIOD_MONTH = 2;

	/**
	 * 获取中文的折扣类型
	 * @param discountType	折扣标志
	 * @return	中文的折扣类型
	 */
	public static String getDiscountType(String discountType) {
		if (StringUtils.isEmpty(discountType)) {
			return "未知类型";
		} else if (discountType.equals(CARD_DISCOUNT_TYPE_RATE)) {
			return "折扣率";
		} else if (discountType.equals(CARD_DISCOUNT_TYPE_QUOTA)) {
			return "折扣额";
		}
		return "未知类型";
	}
	
	public static Date createPeriodValidity(Date oldPeriod, int periodNum, int periodType) {
		
		Calendar periodCalendar = Calendar.getInstance();
		
		if (oldPeriod != null) {
			periodCalendar.setTime(oldPeriod);
		}
		
		// 按照年处理
		if (periodType == MEMBER_CARD_TYPE_PERIOD_YEAR) {
			periodCalendar.add(Calendar.YEAR, periodNum);
		}
		// 按照月处理
		else if (periodType == MEMBER_CARD_TYPE_PERIOD_MONTH) {
			periodCalendar.add(Calendar.MONTH, periodNum);
		}
		
		return new Date(periodCalendar.getTimeInMillis());
	}
	
	public static void main(String[] args) {
		System.out.println(createPeriodValidity(Date.valueOf("2010-05-09"), 1, 2));
	}
	
}

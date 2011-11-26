package net.yanhl.util;

import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b> 数字工具类</p>
 * <p><b>Description：</b>提供一些对数字进行检验、转换功能</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091222
 */
public class NumberUtil {

	/**
	 * 判断Long型是否为空
	 * @param longValue
	 * @return	null和0均为true,其他为false
	 */
	public static boolean isEmptyWithZero(Long longValue) {
		if (longValue == null) {
			return true;
		} else if (longValue == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 把字符串类型的数字序列转换成LONG型数组
	 * @param strNumber		字符串数字序列
	 * @return	LONG型数组
	 */
	public static Long[] stringToLongArray(String strNumber) {
		if (StringUtils.isEmpty(strNumber)) {
			return new Long[] {};
		}
		String[] strNumbers = strNumber.split(",");
		Long[] longArray = stringArrayToLongArray(strNumbers);
		return longArray;
	}
	
	/**
	 * 把字符串类型的数字
	 * @param strNumbers
	 * @return
	 */
	public static Long[] stringArrayToLongArray(String[] strNumbers) {
		if (strNumbers == null || strNumbers.length == 0) {
			return new Long[] {};
		}
		Long[] longArrays = new Long[strNumbers.length];
		for (int i = 0; i < strNumbers.length; i++) {
			longArrays[i] = new Long(strNumbers[i]);
		}
		return longArrays;
	}
	
}

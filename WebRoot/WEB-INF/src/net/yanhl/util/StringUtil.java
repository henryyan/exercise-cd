package net.yanhl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b> 字符串操作工具类</p>
 * <p><b>Description：</b> </p>
 * @author	闫洪磊
 * @version	1.0.0.20080504
*/
public class StringUtil {

	public final static String FORMAT_TIME = "HH:mm";
	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	public final static String TYPE_DATE = "date";
	public final static String TYPE_DATETIME = "datetime";
	public final static String TYPE_TIME = "time";

	/**
	 * 判断数组strs中是否有str这个值
	 *
	 * @param strs
	 * @param str
	 * @return 有true 没有false
	 */
	public static boolean hasInArray(String[] strs, String str) {
		for (String tmp : strs) {
			if (tmp.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 逐个判断数组names中的每个字符串是否为空
	 *
	 * @param names
	 * @return 有一个为空true 都不为空 false
	 */
	public static boolean isEmpty(String[] names) {
		if(names == null) return true;
		for (String name : names) {
			if (StringUtils.isEmpty(name)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 逐个判断数组names中的每个字符串是否为空
	 *
	 * @param names
	 * @return 有一个为空true 都不为空 false
	 */
	public static boolean isEmpty(Object[] names) {
		for (Object name : names) {
			if (StringUtils.isEmpty(name.toString())) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是不是为空，返回处理后的字符串 可以根据需要截取一定长度的字符串
	 *
	 * @maxLength 0表示不截取，返回全部字符串 当maxLength>0时返回截取长度的字符串
	 *
	 */
	public static String getValue(String srcValue, int maxLength) {
		if (srcValue == null || srcValue.toString().equals(""))
			return "";
		if (maxLength > 0 && srcValue.toString().length() > maxLength) {
			return srcValue.toString().substring(0, maxLength);
		} else {
			return srcValue.toString();
		}
	}

	/**
	 * 如果字符串为null或者空串返回空""
	 * 	否则直接返回
	 * @param srcValue
	 * @return 处理后的值
	 */
	public static String getValue(String srcValue) {
		if (srcValue == null || srcValue.toString().equals(""))
			return "";
		return srcValue;
	}

	public static int getIntValue(String srcValue) {
		if (srcValue == null || srcValue.toString().equals(""))
			return 0;
		return Integer.parseInt(srcValue);
	}

	public static int getIntValue(String srcValue, HttpServletRequest request) {
		return getIntValue(request.getParameter(srcValue));
	}

	public static long getLongValue(String srcValue) {
		if (srcValue == null || srcValue.toString().equals(""))
			return 0;
		return Integer.parseInt(srcValue);
	}

	public static long getLongValue(String srcValue, HttpServletRequest request) {
		return getLongValue(request.getParameter(srcValue));
	}

	public static double getDoubleValue(String srcValue) {
		if (srcValue == null || srcValue.toString().equals(""))
			return 0;
		return Double.parseDouble(srcValue);
	}

	public static double getDoubleValue(String srcValue, HttpServletRequest request) {
		return getDoubleValue(request.getParameter(srcValue));
	}

	/**
	 * 获得前台请求参数
	 * @param request
	 * @param requestKey
	 * @return srcValue参数对应的值
	 * @deprecated	建议使用{@link #getParameter}
	 */
	public static String getValue(HttpServletRequest request, String requestKey) {
		String decode = null;
		try {
			String sourceValue = getValue(request.getParameter(requestKey));
			decode = URLDecoder.decode(sourceValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	/**
	 * 获得前台请求参数
	 * @param request
	 * @param requestKey
	 * @return srcValue参数对应的值
	 */
	public static String getParameter(String requestKey, HttpServletRequest request) {
		String decode = null;
		try {
			String sourceValue = getValue(request.getParameter(requestKey));
			decode = URLDecoder.decode(sourceValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	/**
	 * 获得前台请求参数
	 * @param requestKey	参数键
	 * @param defaultValue	为空时的默认值
	 * @param request		HTTP请求对象
	 */
	public static String getParameter(String requestKey, String defaultValue, HttpServletRequest request) {
		String decode = null;
		try {
			String sourceValue = getValue(request.getParameter(requestKey));
			if (StringUtils.isEmpty(sourceValue)) {
				decode = defaultValue;
			} else {
				decode = URLDecoder.decode(sourceValue, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	/**
	 * 获得字符串数组的值
	 * 	以逗号分隔
	 * @param srcArray
	 * @return 以逗号分隔的值
	 */
	public static String getValue(String[] srcArray) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < srcArray.length; i++) {
			result.append(srcArray[i] + ",");
		}
		return deleteComma(result.toString());
	}

	/**
	 * 删除字符串最后的逗号
	 * @param src
	 * @return
	 */
	public static String deleteComma(String src) {
		src = getValue(src);
		if (!src.endsWith(",")) {
			return src;
		}
		src = src.substring(0, src.length() - 1);
		return src;
	}

	/**
	 * 删除字符串最后的后缀
	 * @param src
	 * @return
	 */
	public static String deleteSuffix(String src, String suffix) {
		src = getValue(src);
		if (!src.endsWith(suffix)) {
			return src;
		}
		src = src.substring(0, src.length() - 1);
		return src;
	}
}

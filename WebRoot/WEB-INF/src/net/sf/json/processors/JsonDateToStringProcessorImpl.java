package net.sf.json.processors;

import java.sql.Time;
import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.yanhl.util.StringUtil;

/**
 * <p> Title:为日期类型转换字符串实现的一个JSON处理类</p>
 * <p><b>Description：</b>如果是java.sql.Date则返回yyyy-MM-dd格式的字符串
 * 如果是java.util.Date则返回yyyy-MM-dd HH:mm:ss格式的字符串</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * 
 * @author 闫洪磊
 * @version 1.0.0.200800720
 */
public class JsonDateToStringProcessorImpl implements JsonValueProcessor {

	public JsonDateToStringProcessorImpl() {
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		String[] obj = {};
		if (value == null)
			return obj;
		if (value instanceof java.sql.Timestamp || value instanceof java.util.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(StringUtil.FORMAT_DATE);
			java.util.Date[] dates = (java.util.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		} else if (value instanceof java.sql.Date[]) {
			SimpleDateFormat sf = new SimpleDateFormat(StringUtil.FORMAT_DATETIME);
			java.sql.Date[] dates = (java.sql.Date[]) value;
			obj = new String[dates.length];
			for (int i = 0; i < dates.length; i++) {
				obj[i] = sf.format(dates[i]);
			}
		}
		return obj;
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if (value == null)
			return "";
		//注意：在判断几个父子级类型时要先判断子类型再判断父类型
		if (value instanceof java.sql.Date) {
			String str = new SimpleDateFormat(StringUtil.FORMAT_DATE).format((java.sql.Date) value);
			return str;
		} else if (value instanceof java.sql.Timestamp || value instanceof java.util.Date) {
			String str = new SimpleDateFormat(StringUtil.FORMAT_DATETIME).format((java.util.Date) value);
			return str;
		} else if (value instanceof Time) {
			String str = new SimpleDateFormat(StringUtil.FORMAT_TIME).format((Time) value);
			return str;
		}
		return value.toString();
	}

}

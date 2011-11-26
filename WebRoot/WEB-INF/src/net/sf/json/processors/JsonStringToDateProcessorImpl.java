package net.sf.json.processors;

import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b>为字符串转换日期类型实现的一个JSON处理类</p>
 * <p><b>Description：</b>如果是java.sql.Date则返回yyyy-MM-dd格式的字符串
 * 如果是java.util.Date则返回yyyy-MM-dd HH:mm:ss格式的字符串</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * 
 * @author 闫洪磊
 * @version 1.0.0.200800720
 */
public class JsonStringToDateProcessorImpl implements JsonValueProcessor {

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return value;
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if (value == null || StringUtils.isEmpty(value.toString())) {
			return null;
		}
		return value;
	}

}

package net.yanhl.util;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><b>Title:</b>JSON工具类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100318
 */
public class JSONUtil {

	static Log log = LogFactory.getLog(JSONUtil.class);

	/**
	 * 获取JSON转换对象，默认配置了日期转换处理类
	 * @return	{@link JsonCofig}
	 */
	public static JsonConfig getConfigWithDateTime() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
		config.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());
		config.registerJsonValueProcessor(Timestamp.class, new JsonDateToStringProcessorImpl());
		config.registerJsonValueProcessor(Time.class, new JsonDateToStringProcessorImpl());
		return config;
	}

	/**
	 * 读取请求参数解析为JSON数据格式
	 *
	 * @param request
	 * @return json格式的String对象
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject readJson(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = null;
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			jsonObject = pareseJson(parameterMap);
			log.debug("从客户端获得json=" + jsonObject.toString());
		} catch (Exception e) {
			log.error("获取json数据出错，错误信息如下：\n\t" + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return jsonObject;
	}

	/**
	 * 把参数MAP转换成Json对象
	 * @param jsonObject
	 * @param parameterMap
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static JSONObject pareseJson(Map<String, String[]> parameterMap)
			throws UnsupportedEncodingException {
		JSONObject jsonObject = new JSONObject();
		Iterator<String> paIter = parameterMap.keySet().iterator();
		while (paIter.hasNext()) {
			String key = paIter.next().toString();
			String[] values = (String[])parameterMap.get(key);
			log.debug("key：" + key + ", value：" + values[0]);
			if(values.length == 1) {
				jsonObject.accumulate(key, java.net.URLDecoder.decode(values[0], "UTF-8"));
			} else if(values.length > 1) {
				jsonObject.accumulate(key, values);
			}
		}
		return jsonObject;
	}

}

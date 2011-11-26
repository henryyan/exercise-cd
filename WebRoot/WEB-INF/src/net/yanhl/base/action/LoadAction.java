package net.yanhl.base.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.IgnoreFieldProcessorImpl;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.config.LoadPojoConfig;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b>加载POJO对象</p>
 * <p><b>Description：</b>可以根据<b><i>conf/pojo.properties</i></b>文件中的配置
 * 和主键ID加载一个对象，并根据<b><i>conf/load_pojo_config.xml</i></b>中的配置
 * 返回一个JSON格式的字符串输出到前台</p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.2009
 */
public class LoadAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String pojoPreName = request.getParameter("pn");
		String pojoId = request.getParameter("id");
		if (StringUtils.isEmpty(pojoPreName) || StringUtils.isEmpty(pojoId)) {
			printErrorLabel(response);
		}
		log.debug("POJO.NAME=<" + pojoPreName + ">\tPOJO.IDS=[" + pojoId + "]");

		try {
			LoadPojoConfig loadConfig = LoadPojoConfig.getInstance(pojoPreName);
			PropertiesConfiguration config = new PropertiesConfiguration("conf/pojo.properties");
			String fullPojoName = config.getString(pojoPreName);
			Class<? extends Object> pojoClass = Class.forName(fullPojoName);

			/*
			 * 根据加载POJO配置文件输出对象
			 */
			JsonConfig jsonConfig = new JsonConfig();

			// 是否转换日期
			if (loadConfig.isConvertDate()) {
				jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());
				jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateToStringProcessorImpl());
				jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
			}

			// 是否忽略集合
			if (loadConfig.isIgnoreCollection() && loadConfig.getIgnoreAttributes().length > 0) {
				jsonConfig.setJsonPropertyFilter(new IgnoreFieldProcessorImpl(true, loadConfig.getIgnoreAttributes()));
			} else if (loadConfig.isIgnoreCollection()) {
				jsonConfig.setJsonPropertyFilter(new IgnoreFieldProcessorImpl(true));
			}

			// 从数据库查询对象并以JSON格式输出到前台
			Object object = getBaseManager().get(pojoClass, new Long(pojoId));
			JSONObject fromObject = JSONObject.fromObject(object, jsonConfig);
			print(fromObject.toString(), response);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}

		return null;
	}

}

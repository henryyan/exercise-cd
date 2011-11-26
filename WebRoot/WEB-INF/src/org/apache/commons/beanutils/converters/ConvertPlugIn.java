package org.apache.commons.beanutils.converters;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * <p><b>Title：</b> 注册类型转换</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090730
 */
public class ConvertPlugIn implements PlugIn {
	public ConvertPlugIn() {
	}

	public void destroy() {
		ConvertUtils.deregister();
	}

	public void init(ActionServlet arg0, ModuleConfig arg1) throws ServletException {
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
	}

}
package net.yanhl.base.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.yanhl.base.callback.DeleteCallback;
import net.yanhl.base.service.DeleteManager;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b>对象删除Action，采用AJAX方式</p>
 * <p><b>Description：</b>可根据<b><i>conf/pojo.properties</i></b>文件中的配置
 * 	删除一个或者多个POJO对象</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090624
 */
public class DeleteAction extends BaseAction {
	
	Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	private DeleteManager deleteManager;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pojoPreName = request.getParameter("pn");
		String pojoId = request.getParameter("id");
		if(StringUtils.isEmpty(pojoPreName) || StringUtils.isEmpty(pojoId)) {
			printErrorLabel(response);
		}
		log.debug("POJO.NAME=<" + pojoPreName + ">\tPOJO.IDS=[" + pojoId + "]");
		
		try {
			
			/**
			 * 读取POJO配置文件
			 */
			PropertiesConfiguration config = new PropertiesConfiguration("conf/pojo.properties");
			String fullPojoName = config.getString(pojoPreName);
			Class pojoClass = Class.forName(fullPojoName);
			
			/**
			 * 读取回调配置文件
			 */
			DeleteCallback deleteCallback = null;
			config = new PropertiesConfiguration("conf/deletecallback.properties");
			String callbackClassName = config.getString(pojoPreName);
			if (StringUtils.isNotEmpty(callbackClassName)) {
				Class callbackClass = Class.forName(callbackClassName);
				deleteCallback = (DeleteCallback) callbackClass.newInstance();
			}
			
			if(pojoClass != null) {
				log.debug("通过" + pojoPreName + "查找得到对应的CLASS=" + fullPojoName);
				// 删除多个
				String[] splitPids = pojoId.split(",");
				if(splitPids.length > 1) {
					deleteManager.deleteAll(pojoClass, splitPids);
					printSuccess(response);
					log.info("成功删除" + splitPids.length + "条[" + pojoClass + "]");
				} else {// 删除单个
					Long longPojoId = new Long(pojoId);
					
					deleteManager.deleteSingle(pojoClass, longPojoId, deleteCallback);
					
					printSuccess(response);
					log.info("成功删除[" + fullPojoName + "],ID=" + pojoId);
				}
			} else {
				log.warn("没有找到" + pojoPreName + "所对应的POJO对象");
				printErrorLabel(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			printErrorLabel(response);
		}
		return null;
	}

	public DeleteManager getDeleteManager() {
		return deleteManager;
	}
	
}

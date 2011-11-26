package net.yanhl.field.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.field.pojo.type.FieldType;
import net.yanhl.field.service.FieldTypeManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.util.UserUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b>场地类型管理Action</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public class FieldTypeAction extends BaseAction {
	
	Log log = LogFactory.getLog(this.getClass());
	
	@Resource
	protected FieldTypeManager fieldTypeManager;

	/**
	 * 场地类型列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward list(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			ListQuery listQuery = new ListQuery(FieldType.class, request);
			List list = fieldTypeManager.list(listQuery);
			setAttribute("list", list, request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("list");
	}
	
	/**
	 * 获取当前场馆拥有的场地类型
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ActionForward enableFieldtypeList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			List list = fieldTypeManager.getEnableFieldTypeList(venueId);
			setAttribute("list", list, request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("list");
	}
	
	/**
	 * 场地类型保存
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			FieldType type = (FieldType) JSONObject.toBean(jsonObject, FieldType.class);
			getBaseManager().insertOrUpdate(type);
			printSuccess(response);
		} catch (Exception e) {
			log.error("保存场地类型:" + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 处理场馆和场地类型的关联关系
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward linkFieldType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			boolean enable = Boolean.parseBoolean(request.getParameter("enable"));
			Long fieldTypeId = Long.parseLong(request.getParameter("fieldTypeId"));
			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			if (enable) {
				// 建立联系
				fieldTypeManager.linkFieldType(venueLongId, fieldTypeId);
			} else {
				// 删除联系
				fieldTypeManager.unLinkFieldType(venueLongId, fieldTypeId);
			}
			
			printSuccess(response);
		} catch (Exception e) {
			log.error("处理场馆和场地类型的关联关系:" + e.getMessage(), e);
			printErrorInfo("操作失败", response);
		}
		return null;
	}
	
	/**
	 * 获取场地类型
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getFieldTypes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String[] enableFieldTypes = fieldTypeManager.getEnableFieldTypes(venueId);
			Map<String, String> fieldTypes = FieldUtil.getEnableFieldTypes(enableFieldTypes);
			JSONObject result = JSONObject.fromObject(fieldTypes);
			print(result.toString(), response);
		} catch (Exception e) {
			log.error("获取启用的场地类型:" + e.getMessage(), e);
			printErrorInfo("获取启用的场地类型：操作失败", response);
		}
		return null;
	}
	
}

package net.yanhl.information.action;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.information.pojo.Information;
import net.yanhl.util.JSONUtil;
import net.yanhl.util.NumberUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title:</b>信息颁布Action</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100319
 */
public class InformationAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	/**
	 * 保存信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveInfo(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			Information infoObj = (Information) JSONObject.toBean(jsonObject, Information.class);
			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			if (NumberUtil.isEmptyWithZero(infoObj.getId())) {
				infoObj.setVenueId(venueLongId);
				infoObj.setCreateDate(new Timestamp(System.currentTimeMillis()));
			} else {
				infoObj.setModifyDate(new Timestamp(System.currentTimeMillis()));
			}
			getBaseManager().insertOrUpdate(infoObj);
			JsonConfig jsonConfig = JSONUtil.getConfigWithDateTime();
			JSONObject fromObject = JSONObject.fromObject(infoObj, jsonConfig);
			print(fromObject.toString(), response);
			//printSuccess(response);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存信息颁布时：" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 信息列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward infoList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String currentVenueId = UserUtil.getCurrentVenueId(request);
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(Information.class, request);
			listQuery.setOwnerLabel(new String[] {"venueId", currentVenueId});
			queryFilter(request, listQuery, listResult);// 设置页码
			listQuery.getOrderByMap().put("createDate", BaseQuery.ORDER_DSEC);

			// 设置可以根据参数读取条数
			String strSize = StringUtil.getParameter("size", request);
			if (StringUtils.isNotEmpty(strSize) && StringUtils.isNumeric(strSize)) {
				int size = Integer.parseInt(strSize);
				listQuery.setPageSize(size);
			}

			// 查询条件
			String title = StringUtil.getParameter("title", request);
			String infoContent = StringUtil.getParameter("infoContent", request);
			if (StringUtils.isNotEmpty(title)) {
				listQuery.getCustomFilter().add(new Object[] {"o.title", BaseQuery.LIKE, title});
			}
			if (StringUtils.isNotEmpty(infoContent)) {
				listQuery.getCustomFilter().add(new Object[] {"o.infoContent", BaseQuery.LIKE, infoContent});
			}

			List<Object> result = getBaseManager().find(listQuery);

			JsonConfig jsonConfig = JSONUtil.getConfigWithDateTime();
			saveSearchResult(listQuery, result, listResult, jsonConfig);
			print(listResult.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

}

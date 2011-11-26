package net.yanhl.statistics.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.yanhl.base.action.BaseAction;
import net.yanhl.statistics.service.StatisticsManager;
import net.yanhl.util.UserUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StatisticsAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected StatisticsManager statisticsManager;

	/**
	 * 获取台帐数据
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getStatisticsDatas(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Long currentVenueLongId = UserUtil.getCurrentVenueLongId(request);
			Map<String, Object> datas = statisticsManager.getStatisticsDatas(currentVenueLongId);
			JSONObject result = JSONObject.fromObject(datas);
			log.info("场馆[" + currentVenueLongId + "]，获取台帐数据：\n" + result.toString());
			print(result.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorInfo("获取数据失败", response);
		}
		return null;

	}

}

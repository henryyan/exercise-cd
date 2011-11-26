package net.yanhl.report.action;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.field.pojo.AccountOrder;
import net.yanhl.field.pojo.activity.AllFieldActivity;
import net.yanhl.report.ReportException;
import net.yanhl.report.pojo.ReportDaily;
import net.yanhl.report.pojo.VenueFinancial;
import net.yanhl.report.service.ReportManager;
import net.yanhl.util.DateUtil;
import net.yanhl.util.JSONUtil;
import net.yanhl.util.PaymentStyle;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b> 报表ACTION</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public class ReportAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected ReportManager reportManager;

	/**
	 * 生成每天报表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward reportDaily(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//long venueId = StringUtil.getLongValue("venueId", request);
		long venueId = UserUtil.getCurrentVenueLongId(request);
		String reportDate = StringUtil.getParameter("reportDate", request);
		String activityType = StringUtil.getParameter("activityType", request);

		try {
			List<ReportDaily> reportDaily = reportManager.reportDaily(venueId, reportDate,
					activityType);

			JSONArray reportJson = JSONArray.fromObject(reportDaily);
			print(reportJson.toString(), response);
		} catch (ReportException e) {
			e.printStackTrace();
			String errorInfo = "生成[每日报表]时报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		} catch (Exception e) {
			e.printStackTrace();
			String errorInfo = "生成[每日报表]时报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}

	/**
	 * 读取资金状况报表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward reportCash(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String venueId = UserUtil.getCurrentVenueId(request);
		String reportDate = StringUtil.getParameter("reportDate", request);

		try {

			List<VenueFinancial> reportCashList = reportManager.reportCash(venueId, reportDate);

			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateToStringProcessorImpl());
			config.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());

			JSONArray reportJson = JSONArray.fromObject(reportCashList, config);
			print(reportJson.toString(), response);
		} catch (Exception e) {
			e.printStackTrace();
			String errorInfo = "生成[资金状况]报表报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}

	/**
	 * 查询活动订单列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward orderItemList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(AllFieldActivity.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<AllFieldActivity> result = reportManager.orderItemList(listQuery);

			JsonConfig config = JSONUtil.getConfigWithDateTime();
			
			saveSearchResult(listQuery, result, listResult, config);
			print(listResult.toString(), response);

		} catch (Exception e) {
			String errorInfo = "生成[订单列表]报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}
	
	/**
	 * 通过付款订单查询活动详细
	 */
	public ActionForward findFieldActivity(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JsonConfig config = JSONUtil.getConfigWithDateTime();
			JSONObject listResult = new JSONObject();
			Long accountOrderId = Long.parseLong(StringUtils.defaultIfEmpty(request.getParameter("accountOrderId"), "0"));
			List<AllFieldActivity> fieldOrders = reportManager.findFieldOrder(accountOrderId);
			config.setExcludes(new String[] {"fieldActivity"});
			listResult.accumulate("fieldOrders", fieldOrders, config);
			print(listResult.toString(), response);
		} catch (Exception e) {
			String errorInfo = "通过付款订单查询活动订单报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}
		return null;
	}
	
	/**
	 * 付款订单列表
	 */
	public ActionForward accountOrderList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(AccountOrder.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<Object> result = reportManager.accountOrderList(listQuery);

			JsonConfig config = JSONUtil.getConfigWithDateTime();
			
			saveSearchResult(listQuery, result, listResult, config);
			print(listResult.toString(), response);

		} catch (Exception e) {
			String errorInfo = "生成[订单列表]报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}

	/**
	 * 查询协议服务费记录
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward protocolFee(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(AllFieldActivity.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<AllFieldActivity> result = reportManager.protocolFee(listQuery);

			JsonConfig config = JSONUtil.getConfigWithDateTime();

			saveSearchResult(listQuery, result, listResult, config);
			print(listResult.toString(), response);

		} catch (Exception e) {
			String errorInfo = "生成[协议服务费]报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}
	
	/**
	 * 统计协议服务费
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sumProtocolFee(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String strStartDate = StringUtils.defaultIfEmpty(request.getParameter("startDate"), DateUtil.getSysdate());
			String strEndDate = request.getParameter("endDate");
			Date endDate = null;
			if (StringUtils.isNotEmpty(strEndDate)) {
				endDate = Date.valueOf(strEndDate);
			}
			PaymentStyle[] paymentStyles = {PaymentStyle.快钱, PaymentStyle.支付宝};
			List<Double> sumList = reportManager.sumProtocolFee(venueId, Date.valueOf(strStartDate), endDate, paymentStyles);
			Double sum = sumList.get(0) == null ? 0 : sumList.get(0);
			print(sum.toString(), response);

		} catch (Exception e) {
			String errorInfo = "统计协议服务费报错：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}
	
	/**
	 * 统计活动原始价格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sumActivityPrice(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String strStartDate = StringUtils.defaultIfEmpty(request.getParameter("startDate"), DateUtil.getSysdate());
			String strEndDate = request.getParameter("endDate");
			Date endDate = null;
			if (StringUtils.isNotEmpty(strEndDate)) {
				endDate = Date.valueOf(strEndDate);
			}
			PaymentStyle[] paymentStyles = {PaymentStyle.快钱, PaymentStyle.支付宝};
			List<Double> sumList = reportManager.sumActivityPrice(venueId, Date.valueOf(strStartDate), endDate, paymentStyles);
			Double sum = sumList.get(0) == null ? 0 : sumList.get(0);
			print(sum.toString(), response);

		} catch (Exception e) {
			String errorInfo = "统计活动原始价格：" + e.getMessage();
			log.error(errorInfo, e);
			printErrorInfo(errorInfo, response);
		}

		return null;
	}
	
}

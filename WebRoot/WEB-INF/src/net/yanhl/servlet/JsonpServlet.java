package net.yanhl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.yanhl.base.service.BaseManager;
import net.yanhl.field.action.FieldAction;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.service.FieldManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.tactics.action.TacticsAction;
import net.yanhl.tactics.service.TacticsManager;
import net.yanhl.util.DateUtil;
import net.yanhl.util.JSONUtil;
import net.yanhl.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JsonpServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	Log log = LogFactory.getLog(this.getClass());
	
	protected BaseManager baseManager;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JsonpServlet() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		baseManager = (BaseManager) ctx.getBean("baseManager");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		JSONObject params = null;
		try {
			params = JSONUtil.readJson(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String action = params.getString("action");
		if (StringUtils.isNotEmpty(action)) {
			String outputText = "";
			if ("countActivityOfTactics".equals(action)) {
				outputText = countActivityOfTactics(params);
			} 
			else if ("applyDefaultTactics".equals(action)) {
				outputText = applyDefaultTactics(params);
			} 
			else if ("deleteAndBakActivity".equals(action)) {
				outputText = deleteAndBakActivity(params);
			} 
			else if ("getMaxIssueDays".equals(action)) {
				outputText = getMaxIssueDays(params);
			} 
			else if ("createActivity".equals(action)) {
				outputText = createActivity(params);
			}
			else if ("fieldActivityStatus".equals(action)) {
				outputText = fieldActivityStatus(params);
			}
			else if ("getActivityGrid".equals(action)) {
				outputText = getActivityGrid(params);
			}
			else if ("loadOrder".equals(action)) {
				outputText = loadOrder(params);
			}
			// 读取场地活动列表
			else if ("fieldActivityList".equals(action)) {
				outputText = fieldActivityList(request);
			}
			// 读取当前场馆开启的场地名称和ID
			else if ("getEnableFieldNameId".equals(action)) {
				outputText = getEnableFieldNameId(request);
			}
			
			// 输出
			response.setContentType("text/javascript;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println(outputText);
			out.flush();
		}

	}

	/**
	 * 读取指定场馆开启的场地名称和ID
	 * @param request
	 * @return
	 */
	private String getEnableFieldNameId(HttpServletRequest request) {
		String responseText = "";
		try {
			String fieldType = StringUtil.getParameter("fieldType", request);
			String venueId = StringUtil.getParameter("venueId", request);
			if (StringUtils.isEmpty(venueId) || StringUtils.isEmpty(fieldType)) {
				String output =request.getParameter("callback") + "('参数不正确');";
				return output;
			}
			
			Long venueInfoId = new Long(venueId);
			List<? extends BaseField> fieldList = getFieldManager().getFieldList(venueInfoId, fieldType, "启用");
			JSONObject result = new JSONObject();
			for (Iterator<? extends BaseField> iterator = fieldList.iterator(); iterator.hasNext();) {
				BaseField field = iterator.next();
				if(StringUtils.isEmpty(field.getName())) {
					continue;
				}
				result.accumulate(field.getId().toString(), field.getName());
			}
			
			String output =request.getParameter("callback") + "(" + result.toString() + ");";
			responseText = output;
		} catch (Exception e) {
			log.error("方法：fieldActivityList，错误：", e);
			String output = request.getParameter("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	/**
	 * 读取场地活动列表
	 * @param request
	 * @return
	 * @see FieldAction#fieldActivity
	 */
	private String fieldActivityList(HttpServletRequest request) {
		String responseText = "";
		try {
			String fieldId = StringUtil.getParameter("fieldId", request);
			String fieldType = StringUtil.getParameter("fieldType", request);
			if (StringUtils.isEmpty(fieldId) || StringUtils.isEmpty(fieldType)) {
				String output =request.getParameter("callback") + "('参数不正确');";
				return output;
			}
			
			Long longFieldId = new Long(fieldId);
			
			Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);
			BaseField field = (BaseField) baseManager.get(fieldPojoClass, longFieldId);

			String strStep = StringUtil.getParameter("step", "0", request);
			int nextStep = new Integer(strStep);// 查询间隔天数
			String strPickedDate = request.getParameter("pickedDate");

			if (StringUtils.isNotEmpty(strPickedDate)) {
				nextStep = DateUtil.getBetweenDays(DateUtil.getSysdate(), strPickedDate, false);
			}
			
			FieldManager fieldManager = getFieldManager();
			List<FieldActivity> fieldActivities = fieldManager.getFieldActivity(longFieldId, nextStep, fieldType);
			
			// 设置活动状态
			for (FieldActivity fieldActivity : fieldActivities) {
				String activityStatus = FieldUtil.getActivityStatus(fieldActivity);
				fieldActivity.setActivityStatus(activityStatus);
			}
			
			JsonConfig config = JSONUtil.getConfigWithDateTime();
			config.setExcludes(new String[] {"field"});
			
			JSONObject jsonResult = new JSONObject();
			jsonResult.accumulate("activities", fieldActivities, config);
			jsonResult.accumulate("step", strStep);
			jsonResult.accumulate("pickedDate", strPickedDate);
			jsonResult.accumulate("fieldId", fieldId);
			jsonResult.accumulate("advance", field.getAdvance());
			
			// 设置前一天、后一天导航
			strPickedDate = StringUtils.isEmpty(strPickedDate) ? DateUtil.getSysdate() : strPickedDate;
			java.util.Date preDate = DateUtil.dateAdd(strPickedDate, -1);
			java.util.Date lasterDate = DateUtil.dateAdd(strPickedDate, 1);
			jsonResult.accumulate("preDate", DateUtil.format(preDate, DateUtil.TYPE_DATE));
			jsonResult.accumulate("lasterDate", DateUtil.format(lasterDate, DateUtil.TYPE_DATE));
			
			String output =request.getParameter("callback") + "(" + jsonResult.toString() + ");";
			responseText = output;
		} catch (Exception e) {
			log.error("方法：fieldActivityList，错误：", e);
			String output = request.getParameter("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	/**
	 * 查询订单信息
	 * @param params
	 * @return
	 * @see FieldAction#loadOrder
	 */
	private String loadOrder(JSONObject params) {
		String responseText = "";
		try {
			// 如果没有设置转为从今天开始
			String orderId = params.getString("orderId");
			if (StringUtils.isEmpty(orderId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			JsonConfig config = JSONUtil.getConfigWithDateTime();
			config.setExcludes(new String[] {"fieldActivity"});

			Object object = baseManager.get(FieldOrder.class, new Long(orderId));
			JSONObject result = JSONObject.fromObject(object, config);
			
			String output = params.getString("callback") + "(" + result.toString() + ");";
			responseText = output;
			log.info("方法：loadOrder执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：loadOrder，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	/**
	 * 生成场地活动时段预定信息
	 * @param params
	 * @return
	 */
	private String getActivityGrid(JSONObject params) {
		String responseText = "";
		try {
			// 如果没有设置转为从今天开始
			String usableDate = params.getString("usableDate");
			String fieldType = params.getString("fieldType");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(venueId) || StringUtils.isEmpty(usableDate)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			FieldManager fieldManager = getFieldManager();
			
			Map<String, List<Object[]>> activityGrid = fieldManager.getActivityGrid(new Long(venueId), fieldType, usableDate);
			JSONObject result = JSONObject.fromObject(activityGrid);
			
			String output = params.getString("callback") + "(" + result.toString() + ");";
			responseText = output;
			log.info("方法：fieldActivityStatus执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：fieldActivityStatus，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	private FieldManager getFieldManager() {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		FieldManager fieldManager = (FieldManager) ctx.getBean("fieldManager");
		return fieldManager;
	}

	/**
	 * 从今天以后的场地预订状态
	 * @param params
	 * @return
	 * @see FieldAction#fieldActivityStatus
	 */
	private String fieldActivityStatus(JSONObject params) {
		String responseText = "";
		try {
			// 如果没有设置转为从今天开始
			String fromDate = StringUtils.defaultIfEmpty(params.getString("fromDate"), DateUtil.getSysdate());
			String fieldType = params.getString("fieldType");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(venueId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			FieldManager fieldManager = getFieldManager();
			
			Map<String, String> statusForDate = fieldManager.getFieldActivityStatusForDate(fieldType, new Long(venueId), fromDate);
			JSONObject fromObject = JSONObject.fromObject(statusForDate);
			
			String output = params.getString("callback") + "(" + fromObject.toString() + ");";
			responseText = output;
			log.info("方法：fieldActivityStatus执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：fieldActivityStatus，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	/**
	 * 生成活动到指定日期
	 * @param params
	 * @return
	 * @see FieldAction#issueActivity
	 */
	private String createActivity(JSONObject params) {
		String responseText = "";
		try {
			String lastDate = params.getString("lastDate");
			String fieldType = params.getString("fieldType");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(venueId) || StringUtils.isEmpty(lastDate)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			java.util.Date tempDate = DateUtil.getDate(lastDate, DateUtil.TYPE_DATE);
			Date issueDate = new java.sql.Date(tempDate.getTime());
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			FieldActivityEngine fieldActivityEngine = (FieldActivityEngine) ctx.getBean("fieldActivityEngine");
			fieldActivityEngine.createActivity(new Long(venueId), fieldType, issueDate);
			
			String output = params.getString("callback") + "('true');";
			responseText = output;
			log.info("方法：createActivity执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：createActivity，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		
		return responseText;
	}

	/**
	 * 获得场馆中场地的最大提前发布天数
	 * @param params
	 * @return
	 * @see FieldAction#getMaxIssueDays
	 */
	private String getMaxIssueDays(JSONObject params) {
		String responseText = "";
		try {
			String fieldType = params.getString("fieldType");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(venueId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			FieldManager fieldManager = getFieldManager();
			String maxIssueDays = fieldManager.getMaxIssueDays(new Long(venueId), fieldType);
			String output = params.getString("callback") + "(" + maxIssueDays + ");";
			responseText = output;
			log.info("方法：getMaxIssueDays执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：getMaxIssueDays，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		return responseText;
	}

	/**
	 * 统计使用特殊策略生成的活动条数
	 * @param params
	 * @see {@link TacticsAction#countActivityOfTactics}
	 * @return
	 */
	private String countActivityOfTactics(JSONObject params) {
		String responseText = "";
		try {
			String fieldType = params.getString("fieldType");
			String tacticsId = params.getString("tacticsId");
			
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(tacticsId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			Class<? extends FieldActivity> activityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
			List<? extends FieldActivity> activities = baseManager.findBy(activityPojoClass, "tacticsId", tacticsId);
			
			String output = params.getString("callback") + "(" + activities.size() + ");";
			responseText = output;
			log.info("方法：countActivityOfTactics执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：countActivityOfTactics，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		return responseText;
	}
	
	/**
	 * 把指定策略下面&未预定的活动价格设置成默认策略的价格
	 * @param params
	 * @see {@link TacticsAction#applyDefaultTactics}
	 * @return
	 */
	private String applyDefaultTactics(JSONObject params) {
		String responseText = "";
		try {
			String fieldType = params.getString("fieldType");
			String tacticsId = params.getString("tacticsId");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(tacticsId) || StringUtils.isEmpty(venueId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			TacticsManager tacticsManager = (TacticsManager) ctx.getBean("tacticsManager");
			tacticsManager.applyDefaultTactics(new Long(venueId), new Long(tacticsId), fieldType);
			
			String output = params.getString("callback") + "('true');";
			responseText = output;
			log.info("方法：applyDefaultTactics执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：applyDefaultTactics，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		return responseText;
	}
	
	private String deleteAndBakActivity(JSONObject params) {
		String responseText = "";
		try {
			String fieldType = params.getString("fieldType");
			String tacticsId = params.getString("tacticsId");
			String venueId = params.getString("venueId");
			if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(tacticsId) || StringUtils.isEmpty(venueId)) {
				String output = params.getString("callback") + "('参数不正确');";
				return output;
			}
			
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			TacticsManager tacticsManager = (TacticsManager) ctx.getBean("tacticsManager");
			tacticsManager.deleteAndBakActivity(new Long(venueId), new Long(tacticsId), fieldType);
			
			String output = params.getString("callback") + "('true');";
			responseText = output;
			log.info("方法：deleteAndBakActivity执行成功，响应内容：\n" + responseText);
		} catch (Exception e) {
			log.error("方法：deleteAndBakActivity，错误：", e);
			String output = params.getString("callback") + "('false');";
			responseText = output;
		}
		return responseText;
	}

}

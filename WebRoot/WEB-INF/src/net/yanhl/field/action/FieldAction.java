package net.yanhl.field.action;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mlink.esms.SmsException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.IgnoreFieldProcessorImpl;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.action.BaseAction;
import net.yanhl.field.engine.FieldActivityEngine;
import net.yanhl.field.exception.FieldActivityException;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.BaseField;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.service.FieldManager;
import net.yanhl.field.service.FieldTypeManager;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.util.DateUtil;
import net.yanhl.util.NumberUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b> 场地ACTION</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since 1.0
 * @version 1.0.0.2009
 */
public class FieldAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected FieldManager fieldManager;

	@Resource
	protected FieldTypeManager fieldTypeManager;

	@Resource
	protected FieldActivityEngine fieldActivityEngine;

	/**
	 * 增加场地
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addField(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			VenueInfo venueInfo = venueUser.getVenueInfo();

			// 批量增加场地
			int fieldTotal = jsonObject.getInt("fieldTotal");
			String fieldType = jsonObject.getString("fieldType");

			if (StringUtils.isEmpty(fieldType)) {
				throw new FieldException("没有指定场地类型");
			}

			Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);
			for (int i = 0; i < fieldTotal; i++) {
				BaseField field = (BaseField) JSONObject.toBean(jsonObject, fieldPojoClass);
				Integer maxFieldNo = fieldManager.getMaxFieldNo(fieldType, venueInfo.getId());
				field.setFieldCode(String.valueOf(maxFieldNo));

				String fieldName = maxFieldNo + "号" + FieldUtil.getFieldZhTypeByPojo(field.getClass()) + "场地";
				field.setName(fieldName);

				field.setVenueInfo(venueInfo);
				field.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
				field.setStatus("启用");
				getBaseManager().save(field);
			}
			log.info("场馆：" + venueInfo.getId() + "；成功添加了 " + fieldTotal + " 块" + FieldUtil.getFieldZhType(fieldType)
					+ "场地");

			// 增加场地后立即生成本场馆的@fieldType指定场地类型的活动
			fieldActivityEngine.createActivity(venueInfo.getId(), fieldType, null);

			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 场地列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fieldList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			String fieldType = request.getParameter("fieldType");
			Long venueInfoId = venueUser.getVenueInfo().getId();
			List<? extends BaseField> fieldList = fieldManager.getFieldList(venueInfoId, fieldType);
			request.setAttribute("fieldList", fieldList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("fieldList");
	}

	/**
	 * 场地列表名称及ID
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fieldNames(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);

			// 没有场馆信息直接返回
			if (venueUser.getVenueInfo() == null) {
				print("", response);
				return null;
			}

			String fieldType = request.getParameter("fieldType");
			Long venueInfoId = venueUser.getVenueInfo().getId();
			List<? extends BaseField> fieldList = fieldManager.getFieldList(venueInfoId, fieldType, "启用");
			JSONObject result = new JSONObject();
			for (Iterator<? extends BaseField> iterator = fieldList.iterator(); iterator.hasNext();) {
				BaseField field = iterator.next();
				if (StringUtils.isEmpty(field.getName())) {
					continue;
				}
				result.accumulate(field.getId().toString(), field.getName());
			}
			print(result.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 已启用场地
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward enableFieldList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String forward = "enableFieldList";
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Boolean wizard = new Boolean(StringUtil.getParameter("wizard", request));
			String fieldType = request.getParameter("fieldType");
			if (venueUser.getVenueInfo() == null) {
				setAttribute("fieldList", new ArrayList<BaseField>(), request);
			} else {
				Long venueInfoId = venueUser.getVenueInfo().getId();
				List<? extends BaseField> fieldList = fieldManager.getFieldList(venueInfoId, fieldType, "启用");
				setAttribute("fieldList", fieldList, request);
			}

			// 返回向导页面
			if (wizard) {
				forward = "wizardEnableFieldList";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return mapping.findForward(FORWARD_ERROR);
		}
		return mapping.findForward(forward);
	}

	/**
	 * 加载一个场地信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadField(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			if (strId == null) {
				printErrorLabel(response);
			} else {
				long longId = Long.parseLong(strId);

				String fieldType = request.getParameter("fieldType");
				Class<? extends BaseField> pojoClass = FieldUtil.getFieldPojoClass(fieldType);
				Object object = getBaseManager().get(pojoClass, longId);

				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
				config.setJsonPropertyFilter(new IgnoreFieldProcessorImpl(true, new String[] { "fieldActivities" }));
				JSONObject fromObject = JSONObject.fromObject(object, config);
				fromObject.accumulate("fieldType", fieldType);

				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 更新场地
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateField(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			if (strId == null) {
				printErrorLabel(response);
			} else {
				long longId = Long.parseLong(strId);
				JSONObject jsonObject = readJson(request);
				String fieldType = request.getParameter("fieldType");
				Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);

				BaseField field = (BaseField) getBaseManager().get(fieldPojoClass, longId);
				field.setFieldCode(jsonObject.getString("fieldCode"));
				field.setName(jsonObject.getString("name"));
				field.setStatus(jsonObject.getString("status"));
				getBaseManager().update(field);
				printSuccess(response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 修改提前[生成、发布]天数
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward changeAdvance(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String fieldType = StringUtil.getParameter("fieldType", request);
			String[] strIds = request.getParameterValues("ids");
			String advance = request.getParameter("advance");
			String advanceType = request.getParameter("advanceType");
			if (StringUtils.isEmpty(fieldType) || strIds == null || strIds.length == 0 || StringUtils.isEmpty(advance)
					|| StringUtils.isEmpty(advanceType)) {
				printErrorLabel(response);
			} else {
				String dbAdvanceType = "";
				if (advanceType.equals("create")) {
					dbAdvanceType = "advance";
				} else if (advanceType.equals("issue")) {
					dbAdvanceType = "issueAdvance";
				}

				Long[] longArray = NumberUtil.stringArrayToLongArray(strIds);

				Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);

				Map<String, Object> setKVMap = new HashMap<String, Object>();
				setKVMap.put(dbAdvanceType, new Integer(advance));

				Map<String, Object[]> filterKVMap = new HashMap<String, Object[]>();
				filterKVMap.put("id", new Object[] { longArray });

				getBaseManager().update(fieldPojoClass, setKVMap, filterKVMap);
				printSuccess(response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 场地活动
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fieldActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String fieldId = StringUtil.getParameter("fieldId", request);
		String fieldType = StringUtil.getParameter("fieldType", request);
		String type = StringUtil.getParameter("type", request);
		try {
			if (fieldId == null) {
				return mapping.findForward(FORWARD_ERROR);
			} else {
				Long longFieldId = new Long(fieldId);

				Class<? extends BaseField> fieldPojoClass = FieldUtil.getFieldPojoClass(fieldType);
				BaseField field = (BaseField) getBaseManager().get(fieldPojoClass, longFieldId);
				request.setAttribute("field", field);

				String strStep = StringUtil.getParameter("step", "0", request);
				int nextStep = new Integer(strStep);// 查询间隔天数
				String strPickedDate = request.getParameter("pickedDate");

				if (StringUtils.isNotEmpty(strPickedDate)) {
					nextStep = DateUtil.getBetweenDays(DateUtil.getSysdate(), strPickedDate, false);
				}

				if (type.equals("local") && nextStep > 0) {
					nextStep *= -1;
				}
				List<FieldActivity> fieldActivitys = fieldManager.getFieldActivity(longFieldId, nextStep, fieldType);

				request.setAttribute("step", String.valueOf(nextStep));
				request.setAttribute("pickedDate", strPickedDate);
				request.setAttribute("fieldId", fieldId);
				request.setAttribute("result", fieldActivitys);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward(type);
	}

	/**
	 * 手动发布日期
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward issueActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String lastDate = request.getParameter("lastDate");
			String fieldType = request.getParameter("fieldType");
			Boolean wizard = new Boolean(StringUtil.getParameter("wizard", request));

			// 向导
			if (wizard) {
				// 生成本场馆的场地，生成到正常发布日期
				VenueInfo currentVenueInfo = UserUtil.getCurrentVenueInfo(request);
				log.info("******向导页面生成场地活动--开始******");
				fieldActivityEngine.createActivity(currentVenueInfo.getId(), null);
				log.info("******向导页面生成场地活动--结束******");
				printSuccess(response);
			}
			// 手动生成
			else {
				VenueInfo currentVenueInfo = UserUtil.getCurrentVenueInfo(request);
				if (StringUtils.isEmpty(lastDate) || StringUtils.isEmpty(fieldType)) {
					log.error("生成活动：参数为空；" + "@lastDate=" + lastDate + "，@fieldType=" + fieldType);
					printErrorLabel(response);
					return null;
				} else if (StringUtils.isNotEmpty(lastDate)) {
					java.util.Date tempDate = DateUtil.getDate(lastDate, DateUtil.TYPE_DATE);
					Date issueDate = new java.sql.Date(tempDate.getTime());
					Long venueId = currentVenueInfo.getId();
					log.debug("场馆ID=" + venueId + "手动发布日期为=" + issueDate);
					fieldActivityEngine.createActivity(venueId, fieldType, issueDate);
					printSuccess(response);
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 获得场馆中场地的最大提前发布天数
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getMaxIssueDays(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			String fieldType = request.getParameter("fieldType");
			String maxIssueDays = fieldManager.getMaxIssueDays(venueLongId, fieldType);
			print(maxIssueDays, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 从今天以后的场地预订状态
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fieldActivityStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String fieldType = request.getParameter("fieldType");
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Long venueId = venueUser.getVenueInfo().getId();
			String dataType = StringUtil.getParameter("dataType", request);

			if (StringUtils.isEmpty(dataType)) {
				printErrorLabel(response);
			}

			// 如果没有设置转为从今天开始
			String fromDate = StringUtil.getParameter("fromDate", DateUtil.getSysdate(), request);

			Object result = null;
			String jsonStatus = null;
			if (dataType.equals("date")) {
				result = fieldManager.getFieldActivityStatusForDate(fieldType, venueId, fromDate);
				jsonStatus = JSONObject.fromObject(result).toString();
				log.debug("获取日期对应状态：" + jsonStatus);
			}
			/*else if(dataType.equals("period")) {
				result = fieldManager.getFieldActivityStatusForPeriod(fieldType, venueId, fromDate);
				jsonStatus = JSONObject.fromObject(result).toString();
				log.debug("获取时段对应状态：" + jsonStatus);
			} else if(dataType.equals("field")) {
				String period = StringUtil.getValue(request, "period");
				if(StringUtils.isEmpty(period)) {
					printError(response);
				}
				result = fieldManager.getFieldActivityStatusForField(fieldType, venueId, fromDate, period);
				jsonStatus = JSONObject.fromObject(result).toString();
				log.debug("获取可用场地：" + jsonStatus);
			}*/

			print(jsonStatus, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorInfo("场地预订信息加载失败", response);
		}
		return null;
	}

	/**
	 * 生成场地活动预订表格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getActivityGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String fieldType = request.getParameter("fieldType");
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Long venueId = venueUser.getVenueInfo().getId();
			String usableDate = request.getParameter("usableDate");

			Map<String, List<Object[]>> activityGrid = fieldManager.getActivityGrid(venueId, fieldType, usableDate);
			JSONObject result = JSONObject.fromObject(activityGrid);
			print(result.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 订单状态操作
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward orderHandle(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String fieldType = request.getParameter("fieldType");
			String activityIds = request.getParameter("activityIds");
			String refundment = StringUtils.isEmpty(request.getParameter("refundment")) ? "0" : request
					.getParameter("refundment");
			String opType = StringUtil.getParameter("opType", request);
			String activityType = StringUtil.getParameter("activityType", "normal", request);
			String status = "";

			// 没有活动ID返回错误
			if (StringUtils.isEmpty(activityIds)) {
				printSuccess(response);
				return null;
			}

			JSONObject responseResult = new JSONObject();

			/*
			 *1、取消按照用户输入的退款
			 */
			if (opType.equals("cancel")) {
				status = FieldUtil.FIELD_ACTIVITY_CANCEL;
				VenueInfo venueInfo = UserUtil.getCurrentVenueInfo(request);
				double doubleRefundment = Double.parseDouble(refundment);
				try {
					responseResult = fieldManager.orderHandle(fieldType, activityType, activityIds, status, venueInfo,
							doubleRefundment);
				} catch (FieldActivityException e) {
					e.printStackTrace();
					log.error("订单状态操作出错：" + e.getMessage());
					printErrorInfo(e.getMessage(), response);
				}
			} else {

				// 只修改状态
				String[] onlyChangeStatus = { "unpreserving", "preserving", "blankout" };
				if (StringUtil.hasInArray(onlyChangeStatus, opType)) {

					if (opType.equals("unpreserving")) {
						status = FieldUtil.FIELD_ACTIVITY_UNPRESERVING;
					} else if (opType.equals("preserving")) {
						status = FieldUtil.FIELD_ACTIVITY_PRESERVING;
					} else if (opType.equals("blankout")) {
						status = FieldUtil.FIELD_ACTIVITY_BLANKOUT;
					}
					try {
						fieldManager.orderHandle(fieldType, activityIds, status);
						responseResult.accumulate("handle", true);
					} catch (FieldActivityException e) {
						e.printStackTrace();
						log.error("订单状态操作出错：" + e.getMessage());
						printErrorInfo(e.getMessage(), response);
					}

				}

			}
			print(responseResult.toString(), response);

		} catch (Exception e) {
			log.error("订单状态操作出错：" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 新增订单
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		JSONObject respResult = new JSONObject();
		try {
			JSONObject jsonObject = readJson(request);
			boolean patch = jsonObject.getBoolean("patch");
			String payStyle = jsonObject.getString("payStyle");
			String fieldType = jsonObject.getString("fieldType");
			String strActivityIds = jsonObject.getString("activityIds");

			FieldOrder order = (FieldOrder) JSONObject.toBean(jsonObject, FieldOrder.class);

			if (StringUtils.isEmpty(strActivityIds)) {
				printErrorLabel(response);
				return null;
			} else {
				VenueUser venueUser = UserUtil.getUserFromSession(request);

				// 设置当前场馆、预订时间
				order.setVenueId(UserUtil.getCurrentVenueLongId(request));
				order.setBookTime(new Timestamp(System.currentTimeMillis()));

				int idsLength = strActivityIds.split(",").length;
				if (idsLength == 1) {
					respResult = fieldManager.addOrder(order, strActivityIds, fieldType, venueUser, patch, payStyle);
				} else if (idsLength > 1) {
					respResult = fieldManager.addOrderForBatch(order, strActivityIds, fieldType, venueUser);
				}
				print(respResult.toString(), response);
			}
		} catch (FieldException e) {
			log.error("新增订单出错：", e);
			respResult.accumulate("errorInfo", e.getMessage());
			print(respResult.toString(), response);
		} catch (Exception e) {
			log.error("新增订单出错：" + e.getMessage(), e);
			printErrorInfo("新增订单失败", response);
		}

		return null;
	}

	/**
	 * 加载一个订单信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward loadOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			if (strId == null) {
				printErrorLabel(response);
			} else {
				long longId = Long.parseLong(strId);
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonDateToStringProcessorImpl());
				config.setExcludes(new String[] { "fieldActivity" });

				Object object = getBaseManager().get(FieldOrder.class, longId);
				JSONObject fromObject = JSONObject.fromObject(object, config);
				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error("加载一个订单信息，" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 更新订单信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			if (strId == null) {
				printErrorLabel(response);
			} else {
				long longOrderId = Long.parseLong(strId);
				VenueUser venueUser = UserUtil.getUserFromSession(request);
				JSONObject jsonObject = readJson(request);
				FieldOrder newOrder = (FieldOrder) JSONObject.toBean(jsonObject, FieldOrder.class);
				try {
					fieldManager.updateOrder(longOrderId, newOrder, venueUser);
					printSuccess(response);
				} catch (FieldException e) {
					e.printStackTrace();
					log.warn("修改订单ID=" + longOrderId + ",消息：" + e.getMessage());
					printErrorInfo(e.getMessage(), response);
				} catch (SmsException e) {
					e.printStackTrace();
					log.error("修改订单ID=：" + e.getMessage());
					printErrorInfo("修改成功，发送短信出错", response);
				}
			}
		} catch (Exception e) {
			log.error("更新订单信息，" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 校验验证码是否正确
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward validateCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String activityId = request.getParameter("activityId");
			String validateCode = StringUtil.getParameter("code", request);
			String fieldType = StringUtil.getParameter("fieldType", request);

			if (activityId == null) {
				return mapping.findForward(FORWARD_ERROR);
			} else {
				Long longActivityId = new Long(activityId);
				String[] validateResult = fieldManager.validateCode(longActivityId, fieldType, validateCode);
				Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
				Object orderId = getBaseManager().findBy(fieldActivityPojoClass, "fieldOrder.id", "id", longActivityId);

				JSONObject handleResult = getJsonByHandleResult(validateResult);
				handleResult.accumulate("orderId", orderId);

				print(handleResult.toString(), response);
			}
		} catch (Exception e) {
			log.error("校验验证码是否正确，" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 获取校验验证码
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward getValidateCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String activityId = request.getParameter("activityId");
			String fieldType = request.getParameter("fieldType");

			if (activityId == null) {
				throw new FieldException("没有此记录");
			} else {
				Long longActivityId = new Long(activityId);
				Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
				Object authentiCode = getBaseManager().findBy(fieldActivityPojoClass, "authenticode", "id",
						longActivityId);
				print(authentiCode.toString(), response);
			}
		} catch (Exception e) {
			log.error("校验验证码是否正确，" + e.getMessage(), e);
			printErrorInfo(e.getMessage(), response);
		}
		return null;
	}

	/**
	 * 扣款
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward payForOrder(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String activityId = StringUtil.getParameter("activityId", request);
			String fieldType = request.getParameter("fieldType");

			if (activityId == null) {
				printErrorLabel(response);
				return null;
			} else {
				Long longActivityId = new Long(activityId);
				Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
				FieldActivity fieldActivity = (FieldActivity) getBaseManager().get(fieldActivityPojoClass,
						longActivityId);
				boolean pay = fieldManager.pay(fieldActivity, FieldUtil.PAY_STYLE_CASH);
				log.debug("场地活动," + fieldActivity + "，支付结果=" + pay);
				print(String.valueOf(pay), response);
			}
		} catch (Exception e) {
			log.error("扣款操作，" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 统计本场馆的各个场地类型的总数
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward countField(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String[] enableFieldTypes = fieldTypeManager.getEnableFieldTypes(venueId);
			Map<String, Integer> amount = new HashMap<String, Integer>();
			Integer total = 0;
			for (String fieldType : enableFieldTypes) {
				Integer countFields = fieldManager.countFields(venueId, fieldType).intValue();
				total += countFields;
				amount.put(fieldType, countFields);
			}
			amount.put("total", total);
			JSONObject result = JSONObject.fromObject(amount);
			print(result.toString(), response);
			log.debug("场馆ID=" + venueId + "，统计场地总数：" + amount);
		} catch (Exception e) {
			log.error("统计场馆场地总数：" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 查询活动状态
	 */
	public ActionForward getActivityStatus(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String activityId = StringUtil.getParameter("activityId", request);
			String fieldType = request.getParameter("fieldType");

			if (StringUtils.isEmpty(activityId) && StringUtils.isEmpty(fieldType)) {
				printErrorLabel(response);
				return null;
			} else {
				Long longActivityId = new Long(activityId);
				Class<? extends FieldActivity> fieldActivityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
				FieldActivity fieldActivity = (FieldActivity) getBaseManager().get(fieldActivityPojoClass,
						longActivityId);
				String activityStatus = FieldUtil.getActivityStatus(fieldActivity);
				print(activityStatus, response);
			}
		} catch (Exception e) {
			log.error("查询活动状态，" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

}

package net.yanhl.tactics.action;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.IgnoreFieldProcessorImpl;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.field.pojo.FieldActivity;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.tactics.exception.TacticsException;
import net.yanhl.tactics.pojo.Tactics;
import net.yanhl.tactics.pojo.TacticsDate;
import net.yanhl.tactics.pojo.TacticsPrice;
import net.yanhl.tactics.service.TacticsManager;
import net.yanhl.util.DateUtil;
import net.yanhl.util.NumberUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueUser;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <p><b>Title：</b> 策略Action</p>
 * <p><b>Description：</b>价格策略制定</p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091205
 */
public class TacticsAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected TacticsManager tacticsManager;

	/**
	 * 保存策略
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveTactics(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			Tactics tactics = (Tactics) JSONObject.toBean(jsonObject, Tactics.class);
			if (StringUtils.isNotEmpty(tactics.validate())) {
				throw new TacticsException(tactics.validate());
			}
			if (tactics.getVenueId() == null) {
				tactics.setVenueId(UserUtil.getCurrentVenueLongId(request));
			}
			getBaseManager().insertOrUpdate(tactics);
			printSuccess(response);
		} catch (TacticsException e) {
			e.printStackTrace();
			log.error("保存价格策略时：" + e.getMessage());
			printErrorInfo(e.getMessage(), response);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存价格策略时：" + e.getMessage());
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 添加策略日期
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward addTacticsDate(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			String strTacticsId = StringUtil.getParameter("tacticsId", request);
			Tactics tactics = null;

			if (StringUtils.isEmpty(strTacticsId)) {
				throw new TacticsException("没有找到相应策略");
			} else {
				long tacticsId = Long.parseLong(strTacticsId);
				Object object = getBaseManager().get(Tactics.class, tacticsId);
				if (object == null) {
					throw new TacticsException("没有找到相应策略");
				} else {
					tactics = (Tactics) object;
				}
			}

			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String strFromDate = jsonObject.getString("fromDate");
			String strToDate = jsonObject.getString("toDate");
			String fieldType = tactics.getFieldType();
			Date fromDate = DateUtil.getSqlDate(strFromDate, DateUtil.TYPE_DATE);
			Date toDate = DateUtil.getSqlDate(strToDate, DateUtil.TYPE_DATE);
			if (StringUtils.isEmpty(strFromDate) || StringUtils.isEmpty(strToDate)) {
				throw new TacticsException("日期段不能为空");
			} else {
				JSONObject checkRepeatDates = tacticsManager.checkRepeatDates(venueId, fieldType, fromDate, toDate);
				if (checkRepeatDates.getBoolean("repeat")) {
					throw new TacticsException(checkRepeatDates.toString());
				}
			}

			TacticsDate tacticsDate = new TacticsDate();
			tacticsDate.setFromDate(fromDate);
			tacticsDate.setToDate(toDate);
			if (NumberUtil.isEmptyWithZero(tacticsDate.getId())) {
				tacticsDate.setVenueId(UserUtil.getCurrentVenueLongId(request));
				tacticsDate.setTactics(tactics);
			}

			getBaseManager().insertOrUpdate(tacticsDate);

			// 设置更新状态
			if (!tactics.getIsModify()) {
				tactics.setIsModify(true);
				getBaseManager().insertOrUpdate(tactics);
			}

			printSuccess(response);

		} catch (TacticsException e) {
			e.printStackTrace();
			log.error("保存策略日期时：" + e.getMessage());
			printErrorInfo(e.getMessage(), response);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存策略日期时：" + e.getMessage());
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 策略列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tacticsList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			ListQuery listQuery = new ListQuery(Tactics.class, request);
			List<Tactics> result = tacticsManager.getTacticsList(listQuery);
			savePagination(request, result, Tactics.class);// 保存结果集
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("tacticsList");
	}

	/**
	 * 策略日期列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActionForward tacticsDateList(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			ListQuery listQuery = new ListQuery(TacticsDate.class, request);
			List<TacticsDate> result = tacticsManager.getTacticsDateList(listQuery);
			if (result.size() > 0) {
				result = (List<TacticsDate>) result.get(1);
			}
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
			config.setJsonPropertyFilter(new IgnoreFieldProcessorImpl(true, new String[] {"tactics"}));

			JSONArray dates = JSONArray.fromObject(result, config);
			print(dates.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapping.findForward("tacticsList");
	}

	/**
	 * 保存策略价格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveTacticsPrice(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Long venueId = venueUser.getVenueInfo().getId();
			JSONObject jsonObject = readJson(request);

			String tacticsId = jsonObject.getString("tacticsId");
			if (StringUtils.isEmpty(tacticsId)) {
				throw new TacticsException("没有附属策略对象");
			}

			Tactics tactics = (Tactics) getBaseManager().get(Tactics.class, Long.parseLong(tacticsId));
			List<TacticsPrice> pricePojos = new ArrayList<TacticsPrice>();
			Object start = jsonObject.get("start");
			Object end = jsonObject.get("end");
			Object price = jsonObject.get("price");

			/**
			 * 一个价格
			 */
			if (start instanceof java.lang.String) {
				TacticsPrice basicPrice = new TacticsPrice();
				basicPrice.setTactics(tactics);
				basicPrice.setFromTime(start.toString());
				basicPrice.setToTime(end.toString());
				basicPrice.setPrice(new Integer(price.toString()));
				basicPrice.setVenueId(venueId);
				pricePojos.add(basicPrice);
			}
			/**
			 * 多个价格
			 */
			else {
				JSONArray starts = (JSONArray) start;
				JSONArray ends = (JSONArray) end;
				JSONArray prices = (JSONArray) price;

				for (int i = 0; i < starts.size(); i++) {
					TacticsPrice basicPrice = new TacticsPrice();
					basicPrice.setTactics(tactics);
					basicPrice.setFromTime(starts.getString(i));
					basicPrice.setToTime(ends.getString(i));
					basicPrice.setPrice(new Integer(prices.getString(i)));
					basicPrice.setVenueId(venueId);
					pricePojos.add(basicPrice);
				}
			}

			tacticsManager.savePrices(tactics.getId(), pricePojos);

			// 设置更新状态
			if (!tactics.getIsModify()) {
				tactics.setIsModify(true);
				getBaseManager().insertOrUpdate(tactics);
			}
			
			printSuccess(response);
			log.info("场馆ID=" + venueId + "，成功添加策略价格；策略ID=" + tactics.getId());
		} catch (TacticsException e) {
			log.error("保存策略价格时：" + e.getMessage(), e);
			printErrorInfo(e.getMessage(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 策略价格列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tacticsPriceList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			String fieldType = request.getParameter("fieldType");
			Long venueId = venueUser.getVenueInfo().getId();
			List<TacticsPrice> tacticsPriceList = tacticsManager.getTacticsPriceList(venueId, fieldType);
			request.setAttribute("priceList", tacticsPriceList);
			request.setAttribute("fieldType", fieldType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return mapping.findForward(FORWARD_SUCCESS);
	}

	/**
	 * 更新价格信息
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updatePrice(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			String price = request.getParameter("price");
			if(strId == null) {
				printErrorLabel(response);
			} else {
				long longId = Long.parseLong(strId);
				Object object = getBaseManager().get(TacticsPrice.class, longId);
				if (object != null) {
					TacticsPrice tacticsPrice = (TacticsPrice) object;
					tacticsPrice.setPrice(new Integer(price));
					getBaseManager().update(tacticsPrice);

					// 设置更新状态
					Tactics tactics = tacticsPrice.getTactics();
					if (tactics != null) {
						if (!tactics.getIsModify()) {
							tactics.setIsModify(true);
							getBaseManager().insertOrUpdate(tactics);
						}
					}

					printSuccess(response);
				} else {
					printErrorInfo("没有找到此价格记录", response);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 清空策略范围内的活动，并重新生成，已预定的备份到策略活动备份表中
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteAndBakActivity(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			String fieldType = request.getParameter("fieldType");
			Long tacticsId = Long.parseLong(StringUtil.getParameter("tacticsId", "0", request));

			tacticsManager.deleteAndBakActivity(venueLongId, tacticsId, fieldType);
			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 待处理策略活动列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward tacticsActivityList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Long venueId = venueUser.getVenueInfo().getId();
			String fieldType = request.getParameter("fieldType");

			List<Object> list = tacticsManager.tacticsActivityList(venueId, fieldType);

			setAttribute("activityList", list.get(1), request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return mapping.findForward(FORWARD_SUCCESS);
	}

	/**
	 * 统计使用特殊策略生成的活动条数
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward countActivityOfTactics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String fieldType = StringUtil.getParameter("fieldType", request);
			String tacticsId = StringUtil.getParameter("tacticsId", request);
			Class<? extends FieldActivity> activityPojoClass = FieldUtil.getFieldActivityPojoClass(fieldType);
			List<? extends FieldActivity> findBy = getBaseManager().findBy(activityPojoClass, "tacticsId", tacticsId);
			print(String.valueOf(findBy.size()), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 把指定策略下面&未预定的活动价格设置成默认策略的价格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward applyDefaultTactics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String fieldType = StringUtil.getParameter("fieldType", request);
			String strTacticsId = StringUtil.getParameter("tacticsId", "0", request);
			Long tacticsId = Long.valueOf(strTacticsId);
			tacticsManager.applyDefaultTactics(venueId, tacticsId, fieldType);
			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 检查指定的策略日期是否和本场馆已存在的策略日期重叠
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward checkRepeatDates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			String strFromDate = StringUtil.getParameter("fromDate", request);
			String strToDate = StringUtil.getParameter("toDate", request);
			String fieldType = StringUtil.getParameter("fieldType", request);
			Date fromDate = Date.valueOf(strFromDate);
			Date toDate = Date.valueOf(strToDate);
			JSONObject checkRepeatDates = tacticsManager.checkRepeatDates(venueId, fieldType, fromDate, toDate);
			print(checkRepeatDates.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

}

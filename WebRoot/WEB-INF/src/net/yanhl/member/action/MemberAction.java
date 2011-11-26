package net.yanhl.member.action;

import java.util.Calendar;
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
import net.yanhl.member.exception.MemberException;
import net.yanhl.member.pojo.CardType;
import net.yanhl.member.pojo.CardUsageRecord;
import net.yanhl.member.pojo.MemberCard;
import net.yanhl.member.service.MemberManager;
import net.yanhl.member.util.MemberUtil;
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
 * <p><b>Title：</b> 会员卡ACTION</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public class MemberAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected MemberManager memberManager;

	/**
	 * 检查会员卡是否有重复
	 */
	public ActionForward checkCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cardNumber = StringUtil.getParameter("cardNumber", request);
		try {
			JSONObject result = new JSONObject();
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			List<MemberCard> memberCards = getBaseManager().findBy(MemberCard.class,
					new String[] { "cardNumber", "venueId" }, new Object[] { cardNumber, venueId });
			if (memberCards.isEmpty()) {
				result.accumulate("exist", false);
			} else {
				result.accumulate("exist", true);
				MemberCard memberCard = memberCards.get(0);
				result.accumulate("cardId", memberCard.getCardNumber());
			}
			print(result.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return null;

	}

	/**
	 * 保存会员卡
	 */
	public ActionForward saveMemberCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String oper = request.getParameter("oper");
			// 新增
			if ("add".equals(oper)) {
				VenueUser venueUser = UserUtil.getUserFromSession(request);
				JSONObject jsonObject = readJson(request);

				// 处理会员卡类型字段
				Object typeId = jsonObject.get("cardType.typeName");
				jsonObject.remove("cardType.typeName");
				jsonObject.accumulate("cardType", memberManager.get(CardType.class, Long.parseLong(typeId.toString())));

				MemberCard memberCard = (MemberCard) JSONObject.toBean(jsonObject, MemberCard.class);
				memberCard.setCreateDate(new java.util.Date());
				memberCard.setVenueId(venueUser.getVenueInfo().getId());

				String strPeriodValidity = jsonObject.getString("periodValidity");
				if (StringUtils.isNotEmpty(strPeriodValidity)) {
					java.sql.Date periodValidity = java.sql.Date.valueOf(strPeriodValidity);
					memberCard.setPeriodValidity(periodValidity);
				}

				String strEffectDate = jsonObject.getString("effectDate");
				if (StringUtils.isNotEmpty(strEffectDate)) {
					java.sql.Date effectDate = java.sql.Date.valueOf(strEffectDate);
					memberCard.setEffectDate(effectDate);
				}

				getBaseManager().insertOrUpdate(memberCard);

				// 插入会员卡使用记录
				CardUsageRecord usage = new CardUsageRecord();
				usage.setCardId(memberCard.getId());
				usage.setCardNo(memberCard.getCardNumber());
				usage.setVenueId(UserUtil.getCurrentVenueLongId(request));
				usage.setSignature(memberCard.getName());
				usage.setUsageDate(new java.util.Date(System.currentTimeMillis()));
				usage.setOptionTotal(memberCard.getBalance());
				usage.setUsageType(MemberUtil.CARD_USAGE_TYPE_JOIN);
				Calendar ca = Calendar.getInstance();
				String timeSlice = ca.get(Calendar.HOUR_OF_DAY) + ":" + ca.get(Calendar.MINUTE);
				usage.setUsageTimeSlice(timeSlice);
				getBaseManager().insertOrUpdate(usage);

				printSuccess(response);
			} else if ("edit".equals(oper)) { // 修改
				JSONObject jsonObject = readJson(request);
				String strId = jsonObject.getString("id");

				// 处理会员卡类型字段
				Object typeId = jsonObject.get("cardType.typeName");
				jsonObject.remove("cardType.typeName");
				jsonObject.accumulate("cardType", memberManager.get(CardType.class, Long.parseLong(typeId.toString())));

				MemberCard memberCard = (MemberCard) JSONObject.toBean(jsonObject, MemberCard.class);
				long longId = Long.parseLong(strId);
				Object object = getBaseManager().get(MemberCard.class, longId);
				if (object == null) {
					printErrorLabel(response);
				} else {
					MemberCard oldMemberCard = (MemberCard) object;
					oldMemberCard.setName(memberCard.getName());
					oldMemberCard.setMobilePhone(memberCard.getMobilePhone());
					oldMemberCard.setIdNo(memberCard.getIdNo());
					oldMemberCard.setAddress(memberCard.getAddress());

					String strPeriodValidity = jsonObject.getString("periodValidity");
					if (StringUtils.isNotEmpty(strPeriodValidity)) {
						java.sql.Date periodValidity = java.sql.Date.valueOf(strPeriodValidity);
						oldMemberCard.setPeriodValidity(periodValidity);
					}

					String strEffectDate = jsonObject.getString("effectDate");
					if (StringUtils.isNotEmpty(strEffectDate)) {
						java.sql.Date effectDate = java.sql.Date.valueOf(strEffectDate);
						oldMemberCard.setEffectDate(effectDate);
					}

					getBaseManager().insertOrUpdate(oldMemberCard);
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
	 * 会员卡充值
	 */
	public ActionForward recharge(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			double total = StringUtil.getDoubleValue("total", request);
			if (StringUtils.isEmpty(strId)) {
				printErrorLabel(response);
				log.error("会员卡充值：没有接收到会员卡ID");
			} else {
				long venueId = UserUtil.getUserFromSession(request).getVenueInfo().getId();
				long memberCardId = Long.parseLong(strId);
				try {
					double finalBalance = memberManager.recharge(venueId, memberCardId, total);
					JSONObject result = new JSONObject();
					result.accumulate("finalBalance", finalBalance);
					print(result.toString(), response);
				} catch (MemberException e) {
					e.printStackTrace();
					print(e.getMessage(), response);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 会员卡列表
	 */
	public ActionForward memberCardList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(MemberCard.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<MemberCard> result = memberManager.memberCardList(listQuery);
			saveSearchResult(listQuery, result, listResult, null);

			print(listResult.toString(), response);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
		//		return mapping.findForward("memberCardList");
	}

	/**
	 * 加载一个会员卡
	 */
	public ActionForward loadMemberCard(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			String mobilePhone = request.getParameter("mobilePhone");
			String userCode = request.getParameter("userCode");

			long venueId = UserUtil.getCurrentVenueLongId(request);

			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());
			Object object = null;

			// 根据ID查询
			if (StringUtils.isNotEmpty(strId)) {
				long longId = Long.parseLong(strId);
				object = getBaseManager().get(MemberCard.class, longId);
				JSONObject fromObject = JSONObject.fromObject(object, config);
				print(fromObject.toString(), response);
			}
			// 根据会员卡号查询
			else if (StringUtils.isNotEmpty(userCode)) {
				// 需要确保该手机号码在本场馆内有会员卡
				String[] keys = new String[] { "venueId", "cardNumber" };
				Object[] values = new Object[] { venueId, userCode };
				object = getBaseManager().findBy(MemberCard.class, keys, values);
				JSONArray fromObject = JSONArray.fromObject(object, config);
				print(fromObject.toString(), response);
			}
			// 根据手机号码查询
			else if (StringUtils.isNotEmpty(mobilePhone)) {
				// 需要确保该手机号码在本场馆内有会员卡
				object = memberManager.memberCardList(venueId, mobilePhone);

				JSONArray fromObject = JSONArray.fromObject(object, config);
				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 新增一个会员卡类型
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return
	 */
	public ActionForward addCardType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObject = readJson(request);
			CardType type = (CardType) JSONObject.toBean(jsonObject, CardType.class);
			Long venueLongId = UserUtil.getCurrentVenueLongId(request);
			type.setVenueId(venueLongId);
			getBaseManager().insertOrUpdate(type);
			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 会员卡类型列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return
	 */
	public ActionForward cardTypeList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long currentVenueId = UserUtil.getCurrentVenueLongId(request);
			List<CardType> types = memberManager.getCardTypes(currentVenueId);
			request.setAttribute("cardTypes", types);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return mapping.findForward("cardTypeList");
	}

	/**
	 * 加载一个会员卡类型
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return
	 */
	public ActionForward loadCardType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String id = request.getParameter("id");
			if (id == null) {
				printErrorLabel(response);
			} else {
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(java.util.Date.class, new JsonDateToStringProcessorImpl());

				Object object = getBaseManager().get(CardType.class, Long.parseLong(id));
				JSONObject fromObject = JSONObject.fromObject(object, config);
				print(fromObject.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 获取本场馆下面的所以会员卡类型
	 */
	public ActionForward getMemberCardTypes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long currentVenueId = UserUtil.getCurrentVenueLongId(request);
			List<CardType> types = memberManager.getCardTypes(currentVenueId);
			JSONArray ja = JSONArray.fromObject(types);
			print(ja.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 获得会员卡使用记录
	 */
	public ActionForward cardUsageRecordList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {

			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(CardUsageRecord.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<CardUsageRecord> result = memberManager.cardUsageRecordList(listQuery);
			saveSearchResult(listQuery, result, listResult, null);

			print(listResult.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 获得会员卡使用记录详细信息，包含哪个场地的哪个时间段等信息
	 */
	public ActionForward loadCardUsageDetail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String strId = request.getParameter("id");
			if (strId == null) {
				printErrorLabel(response);
			} else {
				Long cardUsageId = new Long(strId);
				JSONObject listResult = memberManager.loadCardUsageDetail(cardUsageId);
				print(listResult.toString(), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

}

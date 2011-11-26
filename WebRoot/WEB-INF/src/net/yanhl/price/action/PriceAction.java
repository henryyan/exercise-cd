package net.yanhl.price.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.yanhl.base.action.BaseAction;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.price.service.PriceManager;
import net.yanhl.price.util.PriceUtil;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueInfo;
import net.yanhl.venue.pojo.VenueUser;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PriceAction extends BaseAction {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected PriceManager priceManager;
	
	@Resource
	protected FieldTypeDao fieldTypeDao;

	/**
	 * 判断当前用户是否已经预设了价格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward hasPrice(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			VenueInfo venueInfo = venueUser.getVenueInfo();
			if(venueInfo == null) {
				print("请先添加场馆", response);
			} else {
				boolean hasPrice = priceManager.hasPrice(venueInfo.getId());
				print(String.valueOf(hasPrice), response);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}

	/**
	 * 价格列表
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward fieldPriceList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String priceType = StringUtil.getParameter("priceType", request);
		Boolean wizard = new Boolean(StringUtil.getParameter("wizard", request));
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			String fieldType = request.getParameter("fieldType");
			Long venueId = venueUser.getVenueInfo().getId();
			List<? extends BasicPricePojo> fieldPriceList = priceManager.getFieldPriceList(venueId, fieldType, priceType);
			request.setAttribute("priceList", fieldPriceList);
			request.setAttribute("fieldType", fieldType);

			// 返回向导页面
			if(wizard) {
				priceType = "w" + priceType;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return mapping.findForward(priceType);
	}

	/**
	 * 保存价格
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward savePrice(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			VenueUser venueUser = UserUtil.getUserFromSession(request);
			Long venueId = venueUser.getVenueInfo().getId();
			JSONObject jsonObject = readJson(request);
			jsonObject.accumulate("venueId", String.valueOf(venueId));

			String priceType = request.getParameter("priceType");
			String fieldType = request.getParameter("fieldType");

			BasicPricePojo basicPrice = null;
			List<BasicPricePojo> pricePojos = new ArrayList<BasicPricePojo>();
			JSONArray starts = (JSONArray) jsonObject.get("start");
			JSONArray ends = (JSONArray) jsonObject.get("end");
			JSONArray prices = (JSONArray) jsonObject.get("price");

			for (int i = 0; i < starts.size(); i++) {
				basicPrice = PriceUtil.getPriceObject(fieldType, priceType);

				basicPrice.setFromTime(starts.getString(i));
				basicPrice.setToTime(ends.getString(i));
				basicPrice.setPrice(new Integer(prices.getString(i)));
				basicPrice.setVenueId(venueUser.getVenueInfo().getId());

				pricePojos.add(basicPrice);
			}
			priceManager.savePrices(venueId, pricePojos);
			log.info("场馆[priceType=" + priceType + "，fieldType=" + fieldType + "]设置价格成功");
			printSuccess(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
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
				String priceType = request.getParameter("priceType");
				String fieldType = request.getParameter("fieldType");
				Class<?> pojoClass = PriceUtil.getPriceClass(fieldType, priceType);
				Object object = getBaseManager().get(pojoClass, longId);
				BeanUtils.setProperty(object, "price", new Integer(price));
				getBaseManager().update(object);
				printSuccess(response);
			}
		} catch (Exception e) {
			log.error("更新价格对象：" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}
	
	/**
	 * 统计场馆的价格数量
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward countPrice(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			Map<String, Map<String, Long>> countPrice = priceManager.countPrice(venueId);
			log.debug("场馆ID=" + venueId + "，统计价格数量=" + countPrice.toString());
			JSONObject fromObject = JSONObject.fromObject(countPrice);
			print(fromObject.toString(), response);
		} catch (Exception e) {
			log.error("统计价格数量：" + e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}
	
}

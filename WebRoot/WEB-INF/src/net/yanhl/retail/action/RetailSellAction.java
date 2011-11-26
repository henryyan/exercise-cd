package net.yanhl.retail.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonDateToStringProcessorImpl;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.retail.pojo.RetailSell;
import net.yanhl.retail.service.RetailSellManager;
import net.yanhl.util.UserUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 零售商品销售Action
 * 
 * @author HenryYan
 *
 */
public class RetailSellAction extends BaseAction {
	
	@Autowired
	RetailSellManager retailSellManager;
	
	/**
	 * 零售商品销售记录
	 */
	public ActionForward sell(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			Long retailGoodsId = Long.parseLong(StringUtils.defaultIfEmpty(request.getParameter("retailGoodsId"), "0"));
			Integer amount = Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("amount"), "0"));
			String memberCardNumber = request.getParameter("memberCardNumber");
			retailSellManager.sell(venueId, amount, retailGoodsId, memberCardNumber);
			print("success", response);
		} catch (RuntimeException e) {
			print(e.getMessage(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 零售商品销售记录
	 */
	public ActionForward sellList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			JsonConfig config = new JsonConfig();
			config.registerJsonValueProcessor(java.sql.Date.class, new JsonDateToStringProcessorImpl());
			
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(RetailSell.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<RetailSell> result = retailSellManager.retailSellList(listQuery);
			saveSearchResult(listQuery, result, listResult, config);

			print(listResult.toString(), response);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
}

package net.yanhl.retail.action;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.yanhl.base.action.BaseAction;
import net.yanhl.base.query.ListQuery;
import net.yanhl.retail.pojo.RetailGoods;
import net.yanhl.retail.service.RetailGoodsManager;
import net.yanhl.util.StringUtil;
import net.yanhl.util.UserUtil;
import net.yanhl.venue.pojo.VenueUser;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author HenryYan
 *
 */
public class RetailGoodsAction extends BaseAction {
	
	@Autowired
	protected RetailGoodsManager retailGoodsManager;
	
	/**
	 * 保存货品
	 */
	public ActionForward saveRetailGoods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String oper = request.getParameter("oper");
			// 新增
			if ("add".equals(oper)) {
				VenueUser venueUser = UserUtil.getUserFromSession(request);
				JSONObject jsonObject = readJson(request);
				
				RetailGoods retailGoods = (RetailGoods) JSONObject.toBean(jsonObject, RetailGoods.class);
				retailGoods.setVenueId(venueUser.getVenueInfo().getId());
				retailGoods.setCreateDate(new Date(System.currentTimeMillis()));
				getBaseManager().insertOrUpdate(retailGoods);

				printSuccess(response);
			} else if ("edit".equals(oper)) { // 修改
				JSONObject jsonObject = readJson(request);
				String strId = jsonObject.getString("id");
				
				long longId = Long.parseLong(strId);
				Object object = getBaseManager().get(RetailGoods.class, longId);
				if (object == null) {
					printErrorLabel(response);
				} else {
					RetailGoods retailGoods = (RetailGoods) object;
					getBaseManager().insertOrUpdate(retailGoods);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			printErrorLabel(response);
		}
		return null;
	}
	
	/**
	 * 检查名称是否有重复
	 */
	public ActionForward checkRepeatRetailName(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String retailName = StringUtil.getParameter("retailName", request);
		try {
			JSONObject result = new JSONObject();
			Long venueId = UserUtil.getCurrentVenueLongId(request);
			List<RetailGoods> retailGoodsList = getBaseManager().findBy(RetailGoods.class,
					new String[] { "retailName", "venueId" }, new Object[] { retailName, venueId });
			if (retailGoodsList.isEmpty()) {
				result.accumulate("exist", false);
			} else {
				result.accumulate("exist", true);
				RetailGoods retailGoods = retailGoodsList.get(0);
				result.accumulate("retailName", retailGoods.getRetailName());
			}
			print(result.toString(), response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;

	}

	/**
	 * 零售商品列表
	 */
	public ActionForward retailGoodsList(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			JSONObject listResult = new JSONObject();
			ListQuery listQuery = new ListQuery(RetailGoods.class, request);
			queryFilter(request, listQuery, listResult);// 设置页码
			List<RetailGoods> result = retailGoodsManager.retailGoodsList(listQuery);
			saveSearchResult(listQuery, result, listResult, null);

			print(listResult.toString(), response);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

}

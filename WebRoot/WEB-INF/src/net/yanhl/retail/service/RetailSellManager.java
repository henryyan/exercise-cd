package net.yanhl.retail.service;

import java.util.List;

import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.BaseManager;
import net.yanhl.retail.pojo.RetailSell;

/**
 * 零售商品业务销售接口
 * 
 * @author HenryYan
 *
 */
public interface RetailSellManager extends BaseManager {

	/**
	 * 查询商品销售列表
	 * @param listQuery
	 * @return
	 */
	List<RetailSell> retailSellList(ListQuery listQuery);
	
	/**
	 * 出售商品
	 * @param venueId			场馆ID
	 * @param amount			数量
	 * @param retailGoodsId		商品ID
	 * @param memberCardNumber	会员卡卡号
	 */
	void sell(Long venueId, Integer amount, Long retailGoodsId, String memberCardNumber);

}

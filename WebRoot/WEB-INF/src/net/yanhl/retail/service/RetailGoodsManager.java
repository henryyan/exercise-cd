package net.yanhl.retail.service;

import java.util.List;

import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.BaseManager;
import net.yanhl.retail.pojo.RetailGoods;

/**
 * 零售商品业务管理接口
 * 
 * @author HenryYan
 *
 */
public interface RetailGoodsManager extends BaseManager {

	/**
	 * 查询商品列表
	 * @param listQuery
	 * @return
	 */
	List<RetailGoods> retailGoodsList(ListQuery listQuery);

}

package net.yanhl.retail.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.retail.dao.RetailGoodsDao;
import net.yanhl.retail.pojo.RetailGoods;
import net.yanhl.retail.service.RetailGoodsManager;
import net.yanhl.util.UserUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 零售商品管理实现类
 * 
 * @author HenryYan
 *
 */
@Service(value = "retailGoodsManager")
@Transactional(rollbackFor = Exception.class)
public class RetailGoodsManagerImpl extends BaseManagerImpl implements RetailGoodsManager {

	@Resource
	protected RetailGoodsDao retailGoodsDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<RetailGoods> retailGoodsList(ListQuery listQuery) {
		// 设置场馆ID
		String venueId = UserUtil.getCurrentVenueId(listQuery.getRequest());
		listQuery.setOwnerLabel(new String[] { "o.venueId", venueId });

		// 设置查询条件
		listQuery.getQueryFilter().put("o.retailName", new String[] { BaseQuery.AND, BaseQuery.LIKE });
		listQuery.getQueryFilter().put("o.description", new String[] { BaseQuery.AND, BaseQuery.LIKE });

		return baseDao.find(listQuery);
	}

}

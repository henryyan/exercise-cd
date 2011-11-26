package net.yanhl.price.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.price.dao.PriceDao;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.price.service.PriceManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p><b>Title：</b> 价格业务处理实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090816
 */
@Service("priceManager")
@Transactional(rollbackFor=Exception.class)
public class PriceManagerImpl extends BaseManagerImpl implements PriceManager {

	Log log = LogFactory.getLog(this.getClass());

	@Resource
	protected PriceDao priceDao;

	@Transactional(readOnly = true)
	public boolean hasPrice(Long venueId) throws PriceException {
		Map<String, Map<String, Long>> countPrice = priceDao.countPrice(venueId);
		Long total = countPrice.get("total").get("total");
		if (total > 0) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = true)
	public List<? extends BasicPricePojo> getFieldPriceList(Long venueId, String fieldType, String priceType) throws PriceException {
		if (StringUtils.isEmpty(fieldType) || StringUtils.isEmpty(priceType)) {
			return new ArrayList<BasicPricePojo>();
		}

		List<? extends BasicPricePojo> fieldList = priceDao.getPriceList(venueId, fieldType, priceType);

		return fieldList;
	}

	public void savePrices(Long venudId, Collection<BasicPricePojo> prices) {

		if (prices == null || prices.isEmpty()) {
			return;
		}

		// 1、删除已设置数据
		List<BasicPricePojo> listPrices = (List<BasicPricePojo>) prices;
		baseDao.deleteAll(listPrices.get(0).getClass(), "venueId", venudId);

		// 2、保存新数据
		saveCollection(prices);

	}

	@Transactional(readOnly = true)
	public Map<String, Map<String, Long>> countPrice(Long venueId) throws PriceException {
		return priceDao.countPrice(venueId);
	}

}

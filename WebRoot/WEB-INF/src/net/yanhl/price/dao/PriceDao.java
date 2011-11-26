package net.yanhl.price.dao;

import java.util.List;
import java.util.Map;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;

public interface PriceDao extends BaseDao {
	
	/**
	 * 统计每种价格的数量
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param priceType	价格类型
	 * @return	Map<场地类型, Map<价格类型, 价格数量>>
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	Map<String, Map<String, Long>> countPrice(Long venueId) throws PriceException;

	/**
	 * 价格列表
	 * @param venueId	场馆ID
	 * @param fieldType	场地类型
	 * @param priceType	价格类型
	 * @return
	 * @throws PriceException	根据场地、价格类型找不到对应的价格对象时
	 */
	List<? extends BasicPricePojo> getPriceList(Long venueId, String fieldType, String priceType) throws PriceException;
}

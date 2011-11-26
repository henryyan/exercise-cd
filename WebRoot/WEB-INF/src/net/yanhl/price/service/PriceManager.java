package net.yanhl.price.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;

/**
 * <p><b>Title：</b> 价格业务处理接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090816
 */
public interface PriceManager {

	/**
	 * 是否已经预设了价格
	 * @param venueId
	 * @return true or false
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	boolean hasPrice(Long venueId) throws PriceException;
	
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
	 * 获得场地价格列表
	 * @param venueId
	 * @param fieldType
	 * @param priceType
	 * @return
	 * @throws PriceException 根据场地、价格类型找不到对应的价格对象时
	 */
	List<? extends BasicPricePojo> getFieldPriceList(Long venueId, String fieldType, String priceType) throws PriceException;

	/**
	 * 保存价格集合，保存之前删除已设置价格信息
	 * @param venueId
	 * @param prices 价格集合
	 */
	void savePrices(Long venudId, Collection<BasicPricePojo> prices);

}

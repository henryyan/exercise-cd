package net.yanhl.price.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.price.dao.PriceDao;
import net.yanhl.price.exception.PriceException;
import net.yanhl.price.pojo.BasicPricePojo;
import net.yanhl.price.util.PriceUtil;

/**
 * <p><b>Title：</b>价格DAO的Hibernate实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
@SuppressWarnings("unchecked")
@Repository(value="priceDao")
public class PriceDaoHibernateImpl extends BaseDaoHibernate implements PriceDao{
	
	/**
	 * 场地类型DAO接口
	 */
	@Resource
	protected FieldTypeDao fieldTypeDao;
	
	/**
	 * 统计每种价格的数量
	 */
	
	public Long [] getCountForPrice(Long venueId) {
		String[] pojoNames = {"BadmintoonBasePrice", "BadmintoonWeekendPrice"};
		Long[] counts = new Long[pojoNames.length];

		String hql = "";
		for (int i = 0; i < pojoNames.length; i++) {
			hql = "select count(*) from " + pojoNames[i] + " p where p.venueId=?";
			List<Long> find = getHibernateTemplate().find(hql, new Object[] {venueId});
			counts[i] = find.get(0);
		}
		return counts;
	}

	
	public List<? extends BasicPricePojo> getPriceList(Long venueId, String fieldType, String priceType) throws PriceException {
		String pojoName = PriceUtil.getPricePojoName(fieldType, priceType);
		String hql = "from " + pojoName + " p where p.venueId=?";
		List<? extends BasicPricePojo> find = getHibernateTemplate().find(hql, new Object[] {venueId});
		return find;
	}

	
	public Map<String, Map<String, Long>> countPrice(Long venueId) throws PriceException {
		String[] enableFieldTypes = fieldTypeDao.getEnableFieldTypes(venueId);
		String[] enablePriceTypes = PriceUtil.getEnablePriceTypes();
		Map<String, Map<String, Long>> amount = new HashMap<String, Map<String, Long>>();
		String hql = "";
		Long total = 0l;
		
		for (String fieldType : enableFieldTypes) {
			amount.put(fieldType, new HashMap<String, Long>());
			for (String priceType : enablePriceTypes) {
				String pricePojoName = PriceUtil.getPricePojoName(fieldType, priceType);
				hql = "select count(*) from " + pricePojoName + " p where p.venueId=?";
				List<Long> find = getHibernateTemplate().find(hql, new Object[] {venueId});
				Long pc = find.get(0);
				total += pc;
				amount.get(fieldType).put(priceType, pc);
			}
		}
		Map<String, Long> totalMap = new HashMap<String, Long>();
		totalMap.put("total", total);
		amount.put("total", totalMap);
		return amount;
	}

}

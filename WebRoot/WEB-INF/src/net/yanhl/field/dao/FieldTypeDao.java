package net.yanhl.field.dao;

import java.util.List;

import net.yanhl.base.dao.BaseDao;
import net.yanhl.field.pojo.type.FieldType;

/**
 * <p><b>Title：</b>场地类型DAO接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public interface FieldTypeDao extends BaseDao {

	/**
	 * 获取本场馆启用的场地类型
	 * @param venueId	场馆ID，如果场馆ID为<b>-1</b>则查询所有场馆启用的场地类型
	 * @return	场地类型集合
	 */
	public List<FieldType> getEnableFieldTypeList(Long venueId);
	
	/**
	 * 获取本场馆启用的场地类型
	 * @param venueId	场馆ID，如果场馆ID为<b>-1</b>则查询所有场馆启用的场地类型
	 * @return	场地类型数组
	 */
	public String[] getEnableFieldTypes(Long venueId);
	
}

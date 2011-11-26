package net.yanhl.field.service;

import java.util.List;

import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.BaseManager;
import net.yanhl.field.exception.FieldTypeException;
import net.yanhl.field.pojo.type.FieldType;

/**
 * <p><b>Title：</b>场地类型业务接口</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public interface FieldTypeManager extends BaseManager {

	/**
	 * 场地类型列表
	 * @param listQuery
	 * @return
	 * @throws FieldTypeException
	 */
	public List<FieldType> list(ListQuery listQuery) throws FieldTypeException;
	
	/**
	 * 建立场馆和场地类型的关联关系
	 * @param venueId	场馆信息ID
	 * @param fieldTypeId	场地类型ID
	 * @throws FieldTypeException
	 */
	public void linkFieldType(Long venueId, Long fieldTypeId) throws FieldTypeException;

	/**
	 * 移除场馆和场地类型的关联关系
	 * @param venueId	场馆信息ID
	 * @param fieldTypeId	场地类型ID
	 */
	public void unLinkFieldType(Long venueId, Long fieldTypeId);
	
	/**
	 * 获取本场馆启用的场地类型
	 * @param venueId	场馆ID
	 * @return	场地类型集合
	 */
	public List<FieldType> getEnableFieldTypeList(Long venueId);
	
	/**
	 * 获取本场馆启用的场地类型
	 * @param venueId	场馆ID
	 * @return	场地类型数组
	 */
	public String[] getEnableFieldTypes(Long venueId);
	
}

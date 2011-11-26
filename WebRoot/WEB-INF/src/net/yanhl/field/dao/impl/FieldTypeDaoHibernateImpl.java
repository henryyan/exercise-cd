package net.yanhl.field.dao.impl;

import java.util.ArrayList;
import java.util.List;

import net.yanhl.base.dao.impl.BaseDaoHibernate;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.pojo.type.FieldType;

import org.springframework.stereotype.Repository;

/**
 * <p><b>Title：</b>场地类型DAO的Hibernate实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
@SuppressWarnings("unchecked")
@Repository(value="fieldTypeDao")
public class FieldTypeDaoHibernateImpl extends BaseDaoHibernate implements FieldTypeDao {

	public String[] getEnableFieldTypes(Long venueId) {
		String[] fieldTypes = null;
		
		// 根据场馆ID查询
		if (venueId != -1) {
			List<FieldType> enableFieldTypeList = getEnableFieldTypeList(venueId);
			fieldTypes = new String[enableFieldTypeList.size()];
			for (int i = 0; i < enableFieldTypeList.size(); i++) {
				fieldTypes[i] = enableFieldTypeList.get(i).getTypeId();
			}
		} else {
			// 直接从类型表查询
			String hql = "select ft.typeId from FieldType ft where ft.enable=?";
			List<String> find = getHibernateTemplate().find(hql, true);
			fieldTypes = new String[find.size()];
			for (int i = 0; i < find.size(); i++) {
				fieldTypes[i] = find.get(i);
			}
		}
		return fieldTypes;
	}

	public List<FieldType> getEnableFieldTypeList(Long venueId) {
		List<FieldType> fieldTypeList = new ArrayList<FieldType>();
		String hql = "select vftl.fieldType from VenueInfoFieldTypeLink vftl where vftl.fieldType.enable=?";
		if (venueId != -1) {
			hql += " and vftl.venueInfo.id=?";
		}
		if (venueId == -1) {
			fieldTypeList = getHibernateTemplate().find(hql, true);
		} else {
			fieldTypeList = getHibernateTemplate().find(hql, new Object[] {true, venueId});
		}
		return fieldTypeList;
	}

}

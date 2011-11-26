package net.yanhl.field.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.yanhl.base.query.BaseQuery;
import net.yanhl.base.query.ListQuery;
import net.yanhl.base.service.impl.BaseManagerImpl;
import net.yanhl.field.dao.FieldTypeDao;
import net.yanhl.field.exception.FieldTypeException;
import net.yanhl.field.pojo.type.FieldType;
import net.yanhl.field.pojo.type.VenueInfoFieldTypeLink;
import net.yanhl.field.service.FieldTypeManager;
import net.yanhl.venue.pojo.VenueInfo;

/**
 * <p><b>Title：</b>场地类型业务实现类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
@Service(value="fieldTypeManager")
@Transactional(rollbackFor=Exception.class)
public class FieldTypeManagerImpl extends BaseManagerImpl implements FieldTypeManager {
	
	@Resource
	protected FieldTypeDao fieldTypeDao;

	@Transactional(readOnly = true)
	public List<FieldType> list(ListQuery listQuery) throws FieldTypeException {
		listQuery.getCustomFilter().add(new Object[] {"enable", BaseQuery.EQ, 1});
		List<FieldType> find = find(listQuery);
		return find;
	}

	public void linkFieldType(Long venueId, Long fieldTypeId) throws FieldTypeException {
		FieldType fieldType = (FieldType) get(FieldType.class, fieldTypeId);
		VenueInfo venueInfo = (VenueInfo) get(VenueInfo.class, venueId);
		VenueInfoFieldTypeLink vft1 = new VenueInfoFieldTypeLink(fieldType, venueInfo);
		save(vft1);
	}

	public void unLinkFieldType(Long venueId, Long fieldTypeId) {
		List<VenueInfoFieldTypeLink> links = findBy(VenueInfoFieldTypeLink.class, new String[] { "venueInfo.id",
				"fieldType.id" }, new Object[] { venueId, fieldTypeId });
		for (VenueInfoFieldTypeLink link : links) {
			baseDao.delete(link);
		}
	}

	@Transactional(readOnly = true)
	public List<FieldType> getEnableFieldTypeList(Long venueId) {
		return fieldTypeDao.getEnableFieldTypeList(venueId);
	}

	@Transactional(readOnly = true)
	public String[] getEnableFieldTypes(Long venueId) {
		return fieldTypeDao.getEnableFieldTypes(venueId);
	}

}

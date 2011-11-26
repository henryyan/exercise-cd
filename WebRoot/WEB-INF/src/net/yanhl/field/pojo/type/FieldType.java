package net.yanhl.field.pojo.type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <p><b>Title：</b>场地类型</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 */
public class FieldType implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Long id;
	protected String typeId;
	protected String typeName;
	protected Boolean enable = true;
	protected String tableName;
	private Set<VenueInfoFieldTypeLink> venueInfoFieldTypeLinks = new HashSet<VenueInfoFieldTypeLink>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Set<VenueInfoFieldTypeLink> getVenueInfoFieldTypeLinks() {
		return venueInfoFieldTypeLinks;
	}

	public void setVenueInfoFieldTypeLinks(Set<VenueInfoFieldTypeLink> venueInfoFieldTypeLinks) {
		this.venueInfoFieldTypeLinks = venueInfoFieldTypeLinks;
	}
	
	/**
	 * 判断此场地类型是否包含指定场馆
	 * @param venueInfoId	场馆ID
	 * @return
	 */
	public boolean hasThis(Long venueInfoId) {
		for (VenueInfoFieldTypeLink link : venueInfoFieldTypeLinks) {
			if (link.getVenueInfo().getId().equals(venueInfoId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "FieldType [enable=" + enable + ", id=" + id + ", typeId=" + typeId + ", typeName=" + typeName + "]";
	}

}

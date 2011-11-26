package net.yanhl.field.pojo.type;

import java.io.Serializable;

import net.yanhl.venue.pojo.VenueInfo;

public class VenueInfoFieldTypeLink implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private FieldType fieldType;
	private VenueInfo venueInfo;

	public VenueInfoFieldTypeLink() {
		super();
	}

	public VenueInfoFieldTypeLink(FieldType fieldType, VenueInfo venueInfo) {
		super();
		this.fieldType = fieldType;
		this.venueInfo = venueInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	public VenueInfo getVenueInfo() {
		return venueInfo;
	}

	public void setVenueInfo(VenueInfo venueInfo) {
		this.venueInfo = venueInfo;
	}

}

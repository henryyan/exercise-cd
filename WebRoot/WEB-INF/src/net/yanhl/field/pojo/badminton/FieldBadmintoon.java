package net.yanhl.field.pojo.badminton;

import java.io.Serializable;

import net.yanhl.field.pojo.BaseField;

public class FieldBadmintoon extends BaseField implements Serializable {

	// Constructors

	private static final long serialVersionUID = 1L;

	/** default constructor */
	public FieldBadmintoon() {
	}

	// Property accessors

	@Override
	public String toString() {
		return "FieldBadmintoon [advance=" + advance + ", createDate=" + createDate + ", envType="
				+ envType + ", fieldCode=" + fieldCode + ", id=" + id + ", issueAdvance="
				+ issueAdvance + ", issueLastDate=" + issueLastDate + ", name=" + name
				+ ", status=" + status + ", venueInfo.id=" + (venueInfo == null ? 0 : venueInfo.getId()) + "]";
	}



}
package net.yanhl.field.pojo.football;

import java.io.Serializable;

import net.yanhl.field.pojo.BaseField;

/**
 * <p><b>Title：</b>足球场地</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class FieldFootball extends BaseField implements Serializable {

	private static final long serialVersionUID = 1L;

	public FieldFootball() {
		super();
	}

	@Override
	public String toString() {
		return "FieldFootball [advance=" + advance + ", createDate=" + createDate + ", envType="
				+ envType + ", fieldCode=" + fieldCode + ", id=" + id + ", issueAdvance="
				+ issueAdvance + ", issueLastDate=" + issueLastDate + ", name=" + name
				+ ", status=" + status + ", venueInfo=" + (venueInfo == null ? 0 : venueInfo.getId()) + "]";
	}

}

package net.yanhl.field.pojo.tennis;

import java.io.Serializable;

import net.yanhl.field.pojo.BaseField;

/**
 * <p><b>Title：</b>网球场地</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class FieldTennis extends BaseField implements Serializable {

	private static final long serialVersionUID = 1L;

	public FieldTennis() {
		super();
	}

	@Override
	public String toString() {
		return "FieldTennis [advance=" + advance + ", createDate=" + createDate + ", envType="
				+ envType + ", fieldCode=" + fieldCode + ", id=" + id + ", issueAdvance="
				+ issueAdvance + ", issueLastDate=" + issueLastDate + ", name=" + name
				+ ", status=" + status + ", venueInfo=" + (venueInfo == null ? 0 : venueInfo.getId()) + "]";
	}

}

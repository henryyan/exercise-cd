package net.yanhl.field.pojo.tennis;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>网球活动</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class FieldTennisActivity extends FieldActivity implements Serializable {

	private static final long serialVersionUID = 1L;

	public FieldTennisActivity() {
		super();
	}

	@Override
	public String toString() {
		return "FieldTennisActivity [id=" + id + ", activity=" + activity + ", authenticode=" + authenticode
				+ ", field=" + field + ", fieldName=" + fieldName + ", fieldOrder=" + fieldOrder + ", fieldType="
				+ fieldType + ", fromTime=" + fromTime + ", orderUser=" + orderUser + ", period=" + period + ", price="
				+ price + ", tacticsId=" + tacticsId + ", toTime=" + toTime + ", usableDate=" + usableDate
				+ ", venueId=" + venueId + ", verification=" + verification + "]";
	}

}

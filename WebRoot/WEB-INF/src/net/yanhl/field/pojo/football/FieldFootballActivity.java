package net.yanhl.field.pojo.football;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>网球活动</p>
 * <p><b>Description：</b></p>
 *
 * @author	闫洪磊
 */
public class FieldFootballActivity extends FieldActivity implements Serializable {

	private static final long serialVersionUID = 1L;

	public FieldFootballActivity() {
		super();
	}

	@Override
	public String toString() {
		return "FieldFootballActivity [id=" + id + ", activity=" + activity + ", authenticode=" + authenticode
				+ ", field=" + field + ", fieldName=" + fieldName + ", fieldOrder=" + fieldOrder + ", fieldType="
				+ fieldType + ", fromTime=" + fromTime + ", orderUser=" + orderUser + ", period=" + period + ", price="
				+ price + ", tacticsId=" + tacticsId + ", toTime=" + toTime + ", usableDate=" + usableDate
				+ ", venueId=" + venueId + ", verification=" + verification + "]";
	}

}

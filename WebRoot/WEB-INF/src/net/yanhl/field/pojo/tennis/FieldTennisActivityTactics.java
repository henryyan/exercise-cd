package net.yanhl.field.pojo.tennis;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>场地活动策略临时表--网球</p>
 * <p><b>Description：</b>保存从活动表中导出的数据</p>
 *
 * @author 闫洪磊
 * @since  1.0
 */
public class FieldTennisActivityTactics extends FieldActivity implements Serializable {

	private static final long serialVersionUID = -2345496157153060831L;
	private Long fieldId;

	public FieldTennisActivityTactics() {
		super();
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

}

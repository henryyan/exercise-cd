package net.yanhl.field.pojo.football;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>场地活动策略临时表--足球</p>
 * <p><b>Description：</b>保存从活动表中导出的数据</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 */
public class FieldFootballActivityTactics extends FieldActivity implements Serializable {
	
	private static final long serialVersionUID = -2345496157153060831L;
	private Long fieldId;

	public FieldFootballActivityTactics() {
		super();
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

}

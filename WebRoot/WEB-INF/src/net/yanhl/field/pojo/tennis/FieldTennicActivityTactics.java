package net.yanhl.field.pojo.tennis;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>场地活动策略临时表</p>
 * <p><b>Description：</b>保存从活动表中导出的数据</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091227
 */
public class FieldTennicActivityTactics extends FieldActivity implements Serializable {
	
	private static final long serialVersionUID = -2345496157153060831L;
	private Long fieldId;

	public FieldTennicActivityTactics() {
		super();
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

}

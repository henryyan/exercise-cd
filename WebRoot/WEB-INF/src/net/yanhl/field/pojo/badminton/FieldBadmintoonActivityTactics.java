package net.yanhl.field.pojo.badminton;

import java.io.Serializable;

import net.yanhl.field.pojo.FieldActivity;

/**
 * <p><b>Title：</b>场地活动策略临时表--羽毛球</p>
 * <p><b>Description：</b>保存从活动表中导出的数据</p>
 *
 * @author 闫洪磊
 * @since  1.0
 */
public class FieldBadmintoonActivityTactics extends FieldActivity implements Serializable {

	private static final long serialVersionUID = -2345496157153060831L;
	private Long fieldId;

	public FieldBadmintoonActivityTactics() {
		super();
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

}

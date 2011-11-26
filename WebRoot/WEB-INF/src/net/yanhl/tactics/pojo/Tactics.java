package net.yanhl.tactics.pojo;

import java.util.HashSet;
import java.util.Set;

import net.yanhl.base.BasePojo;
import net.yanhl.field.exception.FieldException;
import net.yanhl.field.util.FieldUtil;

import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b>策略实体类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20091205
 */
public class Tactics extends BasePojo implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long venueId;
	private String fieldType;
	private String tacticsName;
	private Boolean isModify;
	private String remark;
	private Set<TacticsDate> tacticsDates = new HashSet<TacticsDate>(0);
	private Set<TacticsPrice> tacticsPrices = new HashSet<TacticsPrice>(0);

	// Constructors

	/** default constructor */
	public Tactics() {
	}

	/** minimal constructor */
	public Tactics(Long id) {
		this.id = id;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVenueId() {
		return this.venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldTypeZh() throws FieldException {
		return FieldUtil.getFieldZhType(fieldType);
	}

	public String getTacticsName() {
		return this.tacticsName;
	}

	public void setTacticsName(String tacticsName) {
		this.tacticsName = tacticsName;
	}

	public boolean getIsModify() {
		if (isModify == null || !isModify) {
			return false;
		} else {
			return true;
		}
	}

	public void setIsModify(Boolean isModify) {
		this.isModify = isModify;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<TacticsDate> getTacticsDates() {
		return tacticsDates;
	}

	public void setTacticsDates(Set<TacticsDate> tacticsDates) {
		this.tacticsDates = tacticsDates;
	}

	public Set<TacticsPrice> getTacticsPrices() {
		return tacticsPrices;
	}

	public void setTacticsPrices(Set<TacticsPrice> tacticsPrices) {
		this.tacticsPrices = tacticsPrices;
	}

	public long getTacticsDatesSize() {
		return tacticsDates.size();
	}

	public long getTacticsPricesSize() {
		return tacticsPrices.size();
	}
	
	/**
	 * 本策略是否设置了策略日期和价格
	 * @return	设置了返回true，否则false
	 */
	public boolean hasDateAndPrice() {
		// 判断是否有策略日期
		boolean hasDateAndPrice = true;
		
		if (this.getTacticsDates().isEmpty() || this.getTacticsPrices().isEmpty()) {
			hasDateAndPrice = false;
		}
		return hasDateAndPrice;
	}

	@Override
	public String validate() {
		if (StringUtils.isEmpty(fieldType)) {
			return "场地类型不能为空";
		} else if (StringUtils.isEmpty(tacticsName)) {
			return "策略名称不能为空";
		}

		return null;
	}

	@Override
	public String toString() {
		return "Tactics [fieldType=" + fieldType + ", id=" + id + ", isModify=" + isModify
				+ ", remark=" + remark + ", tacticsName=" + tacticsName + ", venueId=" + venueId + "]";
	}



}
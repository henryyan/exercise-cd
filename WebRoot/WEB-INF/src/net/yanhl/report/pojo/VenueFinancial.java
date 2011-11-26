package net.yanhl.report.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * <p><b>Title：</b>场馆资费POJO</p>
 * <p><b>Table：</b>t_venue_financial_statement</p>
 * <p><b>Description：</b>保存对账务操作的统计信息</p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090116
 */
public class VenueFinancial implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long venueId;
	private Date TDate;
	private String amountType;
	private Integer currentFields;
	private Long currentFees;
	private Integer nextFields;
	private Long nextFees;
	private Long cash;
	private Date createdAt;

	/** default constructor */
	public VenueFinancial() {
	}

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

	public Date getTDate() {
		return this.TDate;
	}

	public void setTDate(Date TDate) {
		this.TDate = TDate;
	}

	public String getAmountType() {
		return this.amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public Integer getCurrentFields() {
		return this.currentFields;
	}

	public void setCurrentFields(Integer currentFields) {
		this.currentFields = currentFields;
	}

	public Long getCurrentFees() {
		return this.currentFees;
	}

	public void setCurrentFees(Long currentFees) {
		this.currentFees = currentFees;
	}

	public Integer getNextFields() {
		return this.nextFields;
	}

	public void setNextFields(Integer nextFields) {
		this.nextFields = nextFields;
	}

	public Long getNextFees() {
		return this.nextFees;
	}

	public void setNextFees(Long nextFees) {
		this.nextFees = nextFees;
	}

	public Long getCash() {
		return this.cash;
	}

	public void setCash(Long cash) {
		this.cash = cash;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "VenueFinancial [TDate=" + TDate + ", amountType=" + amountType + ", cash=" + cash
				+ ", createdAt=" + createdAt + ", currentFees=" + currentFees + ", currentFields="
				+ currentFields + ", id=" + id + ", nextFees=" + nextFees + ", nextFields="
				+ nextFields + ", venueId=" + venueId + "]";
	}

}
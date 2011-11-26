package net.yanhl.field.pojo;

import java.sql.Time;
import java.util.Date;

/**
 * FieldActivity entity. @author MyEclipse Persistence Tools
 */

public class FieldActivity implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	protected Long id;
	protected Long venueId;
	protected Long tacticsId;
	protected String fieldType;
	protected BaseField field;
	protected FieldOrder fieldOrder;
	protected String fieldName;
	protected String period;
	protected Integer price;
	protected Float paymentCommision;
	protected String authenticode;
	protected String verification;
	protected String activity;
	protected Time fromTime;
	protected Time toTime;
	protected String orderUser;
	protected Date usableDate;
	
	// 临时属性
	// 活动状态
	protected String activityStatus;

	// Constructors

	/** default constructor */
	public FieldActivity() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public Long getTacticsId() {
		return tacticsId;
	}

	public void setTacticsId(Long tacticsId) {
		this.tacticsId = tacticsId;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public BaseField getField() {
		return field;
	}

	public void setField(BaseField field) {
		this.field = field;
	}

	public FieldOrder getFieldOrder() {
		return this.fieldOrder;
	}

	public void setFieldOrder(FieldOrder fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Float getPaymentCommision() {
		return paymentCommision;
	}

	public void setPaymentCommision(Float paymentCommision) {
		this.paymentCommision = paymentCommision;
	}

	public String getAuthenticode() {
		return this.authenticode;
	}

	public void setAuthenticode(String authenticode) {
		this.authenticode = authenticode;
	}

	public String getVerification() {
		return this.verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Time getFromTime() {
		return this.fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Time getToTime() {
		return this.toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public String getOrderUser() {
		return this.orderUser;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
	}

	public Date getUsableDate() {
		return usableDate;
	}

	public void setUsableDate(Date usableDate) {
		this.usableDate = usableDate;
	}
	
	
	/*******临时属性*********/

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	/**
	 * 是否网站预定
	 * @return
	 */
	public Boolean getIsOrderSite() {
		if (fieldOrder != null) {
			return fieldOrder.getIsOrderSite();
		}
		return false;
	}

	@Override
	public String toString() {
		return "FieldActivity [activity=" + activity + ", authenticode=" + authenticode
				+ ", fieldName=" + fieldName + ", fieldOrder=" + fieldOrder + ", fieldType="
				+ fieldType + ", fromTime=" + fromTime + ", id=" + id + ", orderUser=" + orderUser
				+ ", period=" + period + ", price=" + price + ", tacticsId=" + tacticsId
				+ ", toTime=" + toTime + ", usableDate=" + usableDate + ", venueId=" + venueId
				+ ", verification=" + verification + "]";
	}

}
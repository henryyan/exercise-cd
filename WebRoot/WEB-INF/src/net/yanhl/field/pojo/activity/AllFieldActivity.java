package net.yanhl.field.pojo.activity;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

import net.yanhl.field.exception.FieldException;
import net.yanhl.field.pojo.FieldOrder;
import net.yanhl.field.util.FieldUtil;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 所有活动
 *
 * @author HenryYan
 *
 */
public class AllFieldActivity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String activity;
	private String authenticode;
	private Long fieldId;
	private String fieldName;
	private String fieldType;
	private Time fromTime;
	private Long id;
	private FieldOrder fieldOrder;
	private String orderUser;
	private String period;
	private Float price;
	private Long tacticsId;
	private Time toTime;
	private Date usableDate;
	private Long venueId;
	private String verification;
	private Float paymentCommision;

	public AllFieldActivity() {
	}

	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getAuthenticode() {
		return this.authenticode;
	}

	public void setAuthenticode(String authenticode) {
		this.authenticode = authenticode;
	}

	public Long getFieldId() {
		return this.fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Time getFromTime() {
		return this.fromTime;
	}

	public void setFromTime(Time fromTime) {
		this.fromTime = fromTime;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderUser() {
		return this.orderUser;
	}

	public FieldOrder getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(FieldOrder fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Long getTacticsId() {
		return this.tacticsId;
	}

	public void setTacticsId(Long tacticsId) {
		this.tacticsId = tacticsId;
	}

	public Time getToTime() {
		return this.toTime;
	}

	public void setToTime(Time toTime) {
		this.toTime = toTime;
	}

	public Date getUsableDate() {
		return this.usableDate;
	}

	public void setUsableDate(Date usableDate) {
		this.usableDate = usableDate;
	}

	public Long getVenueId() {
		return this.venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getVerification() {
		return this.verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public Float getPaymentCommision() {
		return paymentCommision;
	}

	public void setPaymentCommision(Float paymentCommision) {
		this.paymentCommision = paymentCommision;
	}
	
	public String getZhFieldType() {
		try {
			return FieldUtil.getFieldZhType(fieldType);
		} catch (FieldException e) {
			return "未知类型";
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
package net.yanhl.field.pojo;

import java.sql.Timestamp;

import net.yanhl.field.exception.FieldException;
import net.yanhl.field.util.FieldUtil;

import org.apache.commons.lang.StringUtils;

/**
 * 实体：场地活动订单
 *
 * @author HenryYan
 *
 */
public class FieldOrder implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long venueId;
	private Long cardId;
	private Long fieldId;
	private Long activityId;
	private Long accountOrderId;
	private Long cardUsageId;
	private String contact;
	private String userCode;
	private String phone;
	private Boolean paymentStatus;
	private Timestamp bookTime;
	private Timestamp paymentTime;
	private Double paymentSum;
	private Double standardPrice;
	private String paymentStyle;
	private Boolean patch;
	private String bookPlateform; 
	private Float paymentCommision;
	private String fieldType;
	private FieldActivity fieldActivity;

	// Constructors

	/** default constructor */
	public FieldOrder() {
		this.bookPlateform = "cd";
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

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getAccountOrderId() {
		return accountOrderId;
	}

	public void setAccountOrderId(Long accountOrderId) {
		this.accountOrderId = accountOrderId;
	}

	public Long getCardUsageId() {
		return cardUsageId;
	}

	public void setCardUsageId(Long cardUsageId) {
		this.cardUsageId = cardUsageId;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Timestamp getBookTime() {
		return this.bookTime;
	}

	public void setBookTime(Timestamp bookTime) {
		this.bookTime = bookTime;
	}

	public Timestamp getPaymentTime() {
		return this.paymentTime;
	}

	public void setPaymentTime(Timestamp paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Double getPaymentSum() {
		return this.paymentSum;
	}

	public void setPaymentSum(Double paymentSum) {
		this.paymentSum = paymentSum;
	}

	public Double getStandardPrice() {
		return this.standardPrice;
	}

	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public String getPaymentStyle() {
		return this.paymentStyle;
	}

	public void setPaymentStyle(String paymentStyle) {
		this.paymentStyle = paymentStyle;
	}

	public Float getPaymentCommision() {
		return paymentCommision;
	}

	public void setPaymentCommision(Float paymentCommision) {
		this.paymentCommision = paymentCommision;
	}

	public FieldActivity getFieldActivity() {
		return fieldActivity;
	}

	public void setFieldActivity(FieldActivity fieldActivity) {
		this.fieldActivity = fieldActivity;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldZhType() throws FieldException {
		return FieldUtil.getFieldZhType(fieldType);
	}

	public Boolean isPatch() {
		return patch;
	}

	public Boolean getPatch() {
		return patch;
	}

	public void setPatch(Boolean patch) {
		this.patch = patch;
	}

	public String getBookPlateform() {
		return bookPlateform;
	}

	public void setBookPlateform(String bookPlateform) {
		this.bookPlateform = bookPlateform;
	}

	/**
	 * 是否网站预定
	 * @return
	 */
	public Boolean getIsOrderSite() {
//		String defaultString = StringUtils.defaultString(getPaymentStyle());
//		if (defaultString.equals(PaymentStyle.快钱.name()) || defaultString.equals(PaymentStyle.支付宝.name())) {
//			return true;
//		} else {
//			return false;
//		}
		return StringUtils.defaultString(bookPlateform).equals("web-site");
	}

	@Override
	public String toString() {
		return "FieldOrder [bookTime=" + bookTime + ", cardId=" + cardId + ", contact=" + contact + ", fieldId="
				+ fieldId + ", id=" + id + ", patch=" + patch + ", paymentStatus=" + paymentStatus + ", paymentStyle="
				+ paymentStyle + ", paymentSum=" + paymentSum + ", paymentTime=" + paymentTime + ", phone=" + phone
				+ ", standardPrice=" + standardPrice + ", userCode=" + userCode + ", venueId=" + venueId + "]";
	}

}
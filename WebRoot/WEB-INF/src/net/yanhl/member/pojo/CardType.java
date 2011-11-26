package net.yanhl.member.pojo;

import net.yanhl.member.util.MemberUtil;

import org.apache.commons.lang.StringUtils;

/**
 * TCardType entity. @author MyEclipse Persistence Tools
 */

public class CardType implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -8295360373549960783L;
	private Long id;
	private Long venueId;
	private String typeName;
	private Long moneyAmount;
	private Integer periodMonth;
	private Long discountRate;
	private String discountTime;
	private String discountType;
	private Double discountPrice;
	private String describtion;
	private Float paymentCommision;

	// Constructors

	/** default constructor */
	public CardType() {
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public Long getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Long discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountTime() {
		return discountTime;
	}

	public void setDiscountTime(String discountTime) {
		this.discountTime = discountTime;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getMoneyAmount() {
		return this.moneyAmount;
	}

	public void setMoneyAmount(Long moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public String getDiscountType() {
		return discountType;
	}

	public String getDiscountZhType() {
		return MemberUtil.getDiscountType(discountType);
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getDiscountSession() {
		if(StringUtils.isEmpty(discountType) || moneyAmount == null || moneyAmount == 0
				|| discountPrice == null || discountPrice == 0) {
			return 0;
		}
		return (int) (moneyAmount/discountPrice);
	}

	public boolean getCommonDay() {
		return StringUtils.isNotEmpty(this.discountTime) && this.discountTime.startsWith("1");
	}

	public boolean getWeekDay() {
		return StringUtils.isNotEmpty(this.discountTime) &&this.discountTime.indexOf("1", 1) == 1;
	}

	public boolean getHoliDay() {
		return StringUtils.isNotEmpty(this.discountTime) &&this.discountTime.endsWith("1");
	}

	public Integer getPeriodMonth() {
		return periodMonth;
	}

	public void setPeriodMonth(Integer periodMonth) {
		this.periodMonth = periodMonth;
	}

	public String getDescribtion() {
		return describtion;
	}

	public void setDescribtion(String describtion) {
		this.describtion = describtion;
	}

	public Float getPaymentCommision() {
		return paymentCommision;
	}

	public void setPaymentCommision(Float paymentCommision) {
		this.paymentCommision = paymentCommision;
	}

	@Override
	public String toString() {
		return "CardType [discountPrice=" + discountPrice + ", discountRate=" + discountRate
				+ ", discountTime=" + discountTime + ", discountType=" + discountType + ", id="
				+ id + ", moneyAmount=" + moneyAmount + ", periodMonth=" + periodMonth
				+ ", typeName=" + typeName + ", venueId=" + venueId + "]";
	}

}
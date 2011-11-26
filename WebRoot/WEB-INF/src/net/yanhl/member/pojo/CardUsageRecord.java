package net.yanhl.member.pojo;

import java.util.Date;

/**
 * TCardUsageRecord entity. @author MyEclipse Persistence Tools
 */

public class CardUsageRecord implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5487549285256491301L;
	private Long id;
	private Long cardId;
	private String cardNo;
	private Long venueId;
	private Date usageDate;
	private String usageTimeSlice;
	private String signature;
	private Double optionTotal;
	private String usageType;

	// Constructors

	/** default constructor */
	public CardUsageRecord() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public Date getUsageDate() {
		return this.usageDate;
	}

	public void setUsageDate(Date usageDate) {
		this.usageDate = usageDate;
	}

	public String getUsageTimeSlice() {
		return this.usageTimeSlice;
	}

	public void setUsageTimeSlice(String usageTimeSlice) {
		this.usageTimeSlice = usageTimeSlice;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Double getOptionTotal() {
		return optionTotal;
	}

	public void setOptionTotal(Double optionTotal) {
		this.optionTotal = optionTotal;
	}

	public String getUsageType() {
		return usageType;
	}

	public void setUsageType(String usageType) {
		this.usageType = usageType;
	}

}
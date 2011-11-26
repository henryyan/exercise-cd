package net.yanhl.member.pojo;

import java.io.Serializable;
import java.util.Date;

import net.yanhl.util.DateUtil;

/**
 * MemberCard entity. @author MyEclipse Persistence Tools
 */

public class MemberCard extends BaseMemberCard implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1875771909716673804L;

	// Fields
	private Long id;
	private Long venueId;
	private String cardNumber;
	private String name;
	private Double balance;
	private String mobilePhone;
	private String idNo;
	private String address;
	private Date createDate;
	private CardType cardType;
	private Date periodValidity; // 有效日期
	private Date effectDate; // 生效日期

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// Constructors

	/** default constructor */
	public MemberCard() {
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

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public String getCreateDateZh() {
		return DateUtil.formatZh(this.createDate, DateUtil.TYPE_DATE);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getPeriodValidity() {
		return periodValidity;
	}

	public void setPeriodValidity(Date periodValidity) {
		this.periodValidity = periodValidity;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	@Override
	public String getCardStatus() {
		long millis = System.currentTimeMillis();
		if (this.getEffectDate() != null) {
			if (this.getEffectDate().getTime() > millis) {
				return "未生效";
			}
		}
		if (this.getPeriodValidity() != null) {
			if (this.getPeriodValidity().getTime() < millis) {
				return "过期";
			} else if (this.getPeriodValidity().getTime() >= millis) {
				return "正常";
			}
		}
		return "未知";
	}

}
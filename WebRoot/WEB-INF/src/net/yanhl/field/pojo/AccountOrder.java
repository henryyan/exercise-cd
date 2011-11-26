package net.yanhl.field.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 总订单
 *
 * @author HenryYan
 *
 */
public class AccountOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Double payTotal;
	private String contact;
	private String phone;
	private Date payTime;
	private String payPlatform;
	private String platformAccount;
	private Long userId;
	private Boolean paymentStatus;
	private Long venueId;
	private String bookPlateform; 
	
	
	public AccountOrder() {
		super();
		this.bookPlateform = "cd";
	}

	// 临时属性
	private Integer fieldOrderSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(Double payTotal) {
		this.payTotal = payTotal;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	public String getPlatformAccount() {
		return platformAccount;
	}

	public void setPlatformAccount(String platformAccount) {
		this.platformAccount = platformAccount;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getVenueId() {
		return venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public String getBookPlateform() {
		return bookPlateform;
	}

	public void setBookPlateform(String bookPlateform) {
		this.bookPlateform = bookPlateform;
	}

	/**
	 * 对应的活动订单数量
	 * @return
	 */
	public Integer getFieldOrderSize() {
		return fieldOrderSize;
	}

	public void setFieldOrderSize(Integer fieldOrderSize) {
		this.fieldOrderSize = fieldOrderSize;
	}

}

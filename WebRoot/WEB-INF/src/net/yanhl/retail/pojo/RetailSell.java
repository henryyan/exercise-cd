package net.yanhl.retail.pojo;

import java.sql.Date;

/**
 * 
 * 实体：零售商品
 * 
 * @author HenryYan
 *
 */
public class RetailSell implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long venueId;
	private Date sellDate;
	private Long retailGoodsId;
	private Integer amount;
	private String retailName;
	private Float retailPrice;
	private String memberUserName;
	private String memberCardNumber;

	public Long getId() {
		return id;
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

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	public Long getRetailGoodsId() {
		return retailGoodsId;
	}

	public void setRetailGoodsId(Long retailGoodsId) {
		this.retailGoodsId = retailGoodsId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getRetailName() {
		return retailName;
	}

	public void setRetailName(String retailName) {
		this.retailName = retailName;
	}

	public Float getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Float retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String getMemberUserName() {
		return memberUserName;
	}

	public void setMemberUserName(String memberUserName) {
		this.memberUserName = memberUserName;
	}

	public String getMemberCardNumber() {
		return memberCardNumber;
	}

	public void setMemberCardNumber(String memberCardNumber) {
		this.memberCardNumber = memberCardNumber;
	}

}

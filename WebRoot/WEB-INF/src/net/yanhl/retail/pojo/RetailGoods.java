package net.yanhl.retail.pojo;

import java.sql.Date;

/**
 * 
 * 实体：零售商品
 * 
 * @author HenryYan
 *
 */
public class RetailGoods implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long venueId;
	private Date createDate;
	private String retailName;
	private Float price;
	private String description;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRetailName() {
		return retailName;
	}

	public void setRetailName(String retailName) {
		this.retailName = retailName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

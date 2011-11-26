package net.yanhl.price.pojo;

import net.yanhl.field.util.FieldUtil;

/**
 * <p><b>Title：</b> 价格基础类</p>
 * <p><b>Description：</b>提供价格公共属性、方法</p>
 *
 * @author 闫洪磊
 * @since  1.0
 */
public abstract class BasicPricePojo {

	protected Long id;
	protected Long venueId;
	protected Integer price;
	protected Float paymentCommision;
	protected String fromTime;
	protected String toTime;

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

	public Integer getPrice() {
		return price;
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

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	/**
	 * 返回价格的场地类型，如：羽毛球、网球等
	 * 返回值用英文单词标示
	 * @see FieldUtil
	 * @return	价格的场地类型
	 */
	public abstract String getFieldType();

}

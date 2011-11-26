package net.yanhl.venue.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.yanhl.util.FileUtil;
import net.yanhl.util.MathUtils;

/**
 * 实体：场馆图片
 *
 * @author HenryYan
 *
 */
public class VenuePicture implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long venueId;
	private String pictureName;
	private String pictureRealName;
	private Long pictureSize;
	private String pictureRemark;
	private Date uploadDate;

	public VenuePicture() {
	}

	public Long getId() {
		return id;
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

	public String getPictureName() {
		return this.pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getPictureRealName() {
		return pictureRealName;
	}

	public void setPictureRealName(String pictureRealName) {
		this.pictureRealName = pictureRealName;
	}

	public Long getPictureSize() {
		return this.pictureSize;
	}

	public void setPictureSize(Long pictureSize) {
		this.pictureSize = pictureSize;
	}

	/**
	 * 以K或者M方式计算图片大小
	 * @return
	 */
	@Transient
	public String getPictureSizeShow() {
		String result = "";
		if (pictureSize / 1024 >= 1024) {
			Double mSize = pictureSize / 1024 / 1024d;
			double round = MathUtils.round(mSize, 2);
			result = round + "M";
		} else {
			Double kSize = pictureSize / 1024d;
			double round = MathUtils.round(kSize, 2);
			result = round + "K";
		}
		return result;
	}

	/**
	 * 获取图片的类型
	 * @return
	 */
	@Transient
	public String getPictureType() {
		return FileUtil.getFileType(pictureRealName);
	}

	@Column(name = "PICTURE_REMARK")
	public String getPictureRemark() {
		return this.pictureRemark;
	}

	public void setPictureRemark(String pictureRemark) {
		this.pictureRemark = pictureRemark;
	}

	@Column(name = "UPLOAD_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

}
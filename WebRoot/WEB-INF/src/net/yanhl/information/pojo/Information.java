package net.yanhl.information.pojo;

import java.sql.Timestamp;

/**
 * <p><b>Title:</b>信息颁布POJO</p>
 * <p><b>Table:</b>T_INFORMATION</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100319
 */
public class Information implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 6011808300370474781L;
	
	private Long id;
	private Long venueId;
	private String title;
	private String infoContent;
	private String category;
	private String infoLabel;
	private Timestamp createDate;
	private Timestamp modifyDate;

	// Constructors

	/** default constructor */
	public Information() {
	}

	/** minimal constructor */
	public Information(Long id) {
		this.id = id;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getInfoLabel() {
		return this.infoLabel;
	}

	public void setInfoLabel(String infoLabel) {
		this.infoLabel = infoLabel;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

}
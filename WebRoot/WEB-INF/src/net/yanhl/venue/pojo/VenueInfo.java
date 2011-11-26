package net.yanhl.venue.pojo;

import java.util.HashSet;
import java.util.Set;

import net.yanhl.field.pojo.type.VenueInfoFieldTypeLink;

@SuppressWarnings("serial")
public class VenueInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String venueName;
	private String phone;
	private String fax;
	private String adress;
	private String city;
	private String district;
	private String area;
	private String zipcode;
	private String openTime;
	private String closeTime;
	private String contact;
	private String cell;
	private String email;
	private String intraduction;
	private String photoUrl;
	private String authenticode;
	private Boolean verification;
	private Boolean sendSms;
	private Boolean isProtocol;
	private BusinessInfo businessInfo;

	private Set<VenueInfoFieldTypeLink> venueInfoFieldTypeLinks = new HashSet<VenueInfoFieldTypeLink>();

	/*  default constructor */
	public VenueInfo() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVenueName() {
		return this.venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAdress() {
		return this.adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCell() {
		return this.cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIntraduction() {
		return this.intraduction;
	}

	public void setIntraduction(String intraduction) {
		this.intraduction = intraduction;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Boolean getSendSms() {
		return sendSms;
	}

	public void setSendSms(Boolean sendSms) {
		this.sendSms = sendSms;
	}

	public BusinessInfo getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(BusinessInfo businessInfo) {
		this.businessInfo = businessInfo;
	}

	public void setAuthenticode(String authenticode) {
		this.authenticode = authenticode;
	}

	public String getAuthenticode() {
		return authenticode;
	}

	public void setVerification(Boolean verification) {
		this.verification = verification;
	}

	public Boolean getVerification() {
		return verification;
	}

	public Boolean getIsProtocol() {
		return isProtocol;
	}

	public void setIsProtocol(Boolean isProtocol) {
		this.isProtocol = isProtocol;
	}

	public Set<VenueInfoFieldTypeLink> getVenueInfoFieldTypeLinks() {
		return venueInfoFieldTypeLinks;
	}

	public void setVenueInfoFieldTypeLinks(Set<VenueInfoFieldTypeLink> venueInfoFieldTypeLinks) {
		this.venueInfoFieldTypeLinks = venueInfoFieldTypeLinks;
	}
	
	/**
	 * 判断场馆是否开启了制定场地类型
	 * @param fieldType	 场地类型
	 * @return	开启true，未开启false
	 */
	public boolean hasFieldType(String fieldType) {
		for (VenueInfoFieldTypeLink link : venueInfoFieldTypeLinks) {
			if (link.getFieldType().getTypeId().equals(fieldType)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "VenueInfo [adress=" + adress + ", area=" + area + ", authenticode=" + authenticode + ", businessInfo="
				+ businessInfo + ", cell=" + cell + ", city=" + city + ", closeTime=" + closeTime + ", contact="
				+ contact + ", district=" + district + ", email=" + email + ", fax=" + fax + ", id=" + id
				+ ", intraduction=" + intraduction + ", openTime=" + openTime + ", phone=" + phone + ", photoUrl="
				+ photoUrl + ", sendSms=" + sendSms + ", venueName=" + venueName + ", verification=" + verification
				+ ", zipcode=" + zipcode + "]";
	}
	
	
}
package net.yanhl.venue.pojo;

/**
 * TBusinessInfo entity. @author MyEclipse Persistence Tools
 */

public class BusinessInfo implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private VenueInfo venueInfo;
	private String name;
	private String bank;
	private String bankAccount;
	private String owner;
	private String ownerMobile;

	// Constructors

	/** default constructor */
	public BusinessInfo() {
	}

	/** minimal constructor */
	public BusinessInfo(VenueInfo TVenueInfo) {
		this.venueInfo = TVenueInfo;
	}

	/** full constructor */
	public BusinessInfo(VenueInfo TVenueInfo, String name, String bank,
			String bankAccount, String owner, String ownerMobile) {
		this.venueInfo = TVenueInfo;
		this.name = name;
		this.bank = bank;
		this.bankAccount = bankAccount;
		this.owner = owner;
		this.ownerMobile = ownerMobile;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VenueInfo getVenueInfo() {
		return venueInfo;
	}

	public void setVenueInfo(VenueInfo venueInfo) {
		this.venueInfo = venueInfo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerMobile() {
		return this.ownerMobile;
	}

	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
	}

}
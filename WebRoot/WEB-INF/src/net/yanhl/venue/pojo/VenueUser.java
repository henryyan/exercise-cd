package net.yanhl.venue.pojo;

/**
 * <p><b>Title：</b> 场馆用户对象</p>
 * <p><b>Description：</b></p>
 * 
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.200900528
 */
public class VenueUser implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private VenueInfo venueInfo;
	private String username;
	private String password;
	private String status;

	// Constructors

	/** default constructor */
	public VenueUser() {
	}

	/** full constructor */
	public VenueUser(VenueInfo TVenueInfo, String username, String password,
			String status) {
		this.venueInfo = TVenueInfo;
		this.username = username;
		this.password = password;
		this.status = status;
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

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
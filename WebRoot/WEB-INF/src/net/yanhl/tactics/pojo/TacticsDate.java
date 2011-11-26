package net.yanhl.tactics.pojo;

import java.sql.Date;

/**
 * TacticsDate entity. @author MyEclipse Persistence Tools
 */

public class TacticsDate implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long id;
	private Tactics tactics;
	private Long venueId;
	private Date fromDate;
	private Date toDate;

	// Constructors

	/** default constructor */
	public TacticsDate() {
	}

	/** minimal constructor */
	public TacticsDate(Long id) {
		this.id = id;
	}

	/** full constructor */
	public TacticsDate(Long id, Tactics tactics, Long venueId, Date fromDate, Date toDate) {
		this.id = id;
		this.tactics = tactics;
		this.venueId = venueId;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tactics getTactics() {
		return tactics;
	}

	public void setTactics(Tactics tactics) {
		this.tactics = tactics;
	}

	public Long getVenueId() {
		return this.venueId;
	}

	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
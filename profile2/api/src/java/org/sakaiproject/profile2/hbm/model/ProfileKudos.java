package org.sakaiproject.profile2.hbm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Model for a kudos score for a user - persistent
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class ProfileKudos implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userUuid;
	/**
	 * Calculated score out of 100, more accurate if you need better reporting.
	 */
	private BigDecimal percentage;
	
	/**
	 * Adjusted score used for display, less accurate, however some items are unattainable depending on who you are
	 * so this is always rounded up and is fairer. 
	 * 
	 * <p>This value is used for display.</p>
	 */
	private int score;
	private Date dateAdded;
	
	/** 
	 * Empty constructor
	 */
	public ProfileKudos() {		
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
}

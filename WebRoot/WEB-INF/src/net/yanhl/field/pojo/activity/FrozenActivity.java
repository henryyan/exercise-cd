package net.yanhl.field.pojo.activity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体：活动冻结
 *
 * @author HenryYan
 *
 */
public class FrozenActivity implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long activityId;
	private Date createAt;
	private Date updateAt;
	private String sessionId;
	private Long userId;
	private Integer state;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}

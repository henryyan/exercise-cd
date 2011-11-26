package net.yanhl.field.pojo;

import java.util.HashSet;
import java.util.Set;

import net.yanhl.field.exception.FieldException;
import net.yanhl.field.util.FieldUtil;
import net.yanhl.venue.pojo.VenueInfo;

/**
 * <p><b>Title：</b>基础场地信息</p>
 * <p><b>Description：</b>所以场地类均继承此类</p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20100118
 */
public class BaseField implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected Long id;
	protected VenueInfo venueInfo;
	protected java.sql.Date createDate;
	protected String fieldCode;
	protected String name;
	protected String envType;
	protected String status;
	protected Integer advance;
	protected Integer issueAdvance;
	protected java.sql.Date issueLastDate;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Set<? extends FieldActivity> fieldActivities = new HashSet();

	public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.sql.Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(java.sql.Date createDate) {
		this.createDate = createDate;
	}

	public VenueInfo getVenueInfo() {
        return this.venueInfo;
    }

    public void setVenueInfo(VenueInfo venueInfo) {
        this.venueInfo = venueInfo;
    }

	public String getFieldCode() {
		return this.fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnvType() {
		return this.envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAdvance() {
		return this.advance;
	}

	public void setAdvance(Integer advance) {
		this.advance = advance;
	}

	public Integer getIssueAdvance() {
		return issueAdvance;
	}

	public void setIssueAdvance(Integer issueAdvance) {
		this.issueAdvance = issueAdvance;
	}

	public Set<? extends FieldActivity> getFieldActivities() {
		return this.fieldActivities;
	}

	public void setFieldActivities(Set<? extends FieldActivity> fieldActivities) {
		this.fieldActivities = fieldActivities;
	}

	public java.sql.Date getIssueLastDate() {
		return issueLastDate;
	}

	public void setIssueLastDate(java.sql.Date issueLastDate) {
		this.issueLastDate = issueLastDate;
	}

	/**
	 * 获得场地中文类型
	 * @return 场地中文类型
	 * @throws FieldException 找不到类型对应的值
	 */
	public String getZhType() throws FieldException {
		return FieldUtil.getFieldZhTypeByPojo(this.getClass());
	}
	
	public String getFieldType() throws FieldException {
		return FieldUtil.getFieldTypeByPojo(this.getClass());
	}

}

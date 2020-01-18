package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 公告表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-20 10:13:19
 */
public class SysNoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long noticeId;
	//标题
	private String noticeTitle;
	//是否发布0-不发布，1-发布
	private Integer noticeStatus;
	//内容
	private String noticeContent;
	//发布时间
	private Date createTime;
	//发布人
	private Long createBy;
	//内容
	private String userName;

	/**
	 * 设置：主键
	 */
	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}
	/**
	 * 获取：主键
	 */
	public Long getNoticeId() {
		return noticeId;
	}
	/**
	 * 设置：标题
	 */
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	/**
	 * 获取：标题
	 */
	public String getNoticeTitle() {
		return noticeTitle;
	}
	/**
	 * 设置：是否发布0-不发布，1-发布
	 */
	public void setNoticeStatus(Integer noticeStatus) {
		this.noticeStatus = noticeStatus;
	}
	/**
	 * 获取：是否发布0-不发布，1-发布
	 */
	public Integer getNoticeStatus() {
		return noticeStatus;
	}
	/**
	 * 设置：内容
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * 获取：内容
	 */
	public String getNoticeContent() {
		return noticeContent;
	}
	/**
	 * 设置：发布时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：发布时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：发布人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：发布人
	 */
	public Long getCreateBy() {
		return createBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}

package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户认证报告(聚信立)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-31 21:54:19
 */
public class JxlReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Long id;
	//token
	private String token;
	//申请单ID
	private Long userId;
	//申请单ID
	private Long requestId;
	//用户报告内容(json)
	private String reportData;
	//用户移动运营商原始数据(json)
	private String rawData;
	//用户移动运营商报告(json)
	private String mobileData;
	//过期时间
	private Date expireTime;
	//更新时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 获取：token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * 设置：申请单ID
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	/**
	 * 获取：申请单ID
	 */
	public Long getRequestId() {
		return requestId;
	}
	/**
	 * 设置：用户报告内容(json)
	 */
	public void setReportData(String reportData) {
		this.reportData = reportData;
	}
	/**
	 * 获取：用户报告内容(json)
	 */
	public String getReportData() {
		return reportData;
	}
	/**
	 * 设置：用户移动运营商原始数据(json)
	 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	/**
	 * 获取：用户移动运营商原始数据(json)
	 */
	public String getRawData() {
		return rawData;
	}
	/**
	 * 设置：过期时间
	 */
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	/**
	 * 获取：过期时间
	 */
	public Date getExpireTime() {
		return expireTime;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	public String getMobileData() {
		return mobileData;
	}

	public void setMobileData(final String mobileData) {
		this.mobileData = mobileData;
	}
}

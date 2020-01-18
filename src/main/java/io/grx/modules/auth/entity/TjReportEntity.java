package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.auth.enums.TianjiState;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.VerifyStatus;


/**
 * 天机认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-04-13 14:52:36
 */
public class TjReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//用户ID
	private Long userId;
	//认证TASK ID
	private String taskId;
	private String searchId;
	//报告类型
	private TianjiType tianjiType;
	//报告类型
	private TianjiState tianjiState;
	//用户名称
	private String name;
	//身份证号码
	private String idNo;
	//手机号
	private String mobile;
	//报告路径json
	private String reportJsonPath;
	//报告路径html
	private String reportHtmlPath;
	//状态
	private VerifyStatus verifyStatus;
	//创建时间
	private Date createTime;
	//创建时间
	private Date updateTime;

	private boolean isExpired;

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
	 * 设置：商家编号
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	/**
	 * 获取：商家编号
	 */
	public String getMerchantNo() {
		return merchantNo;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：认证TASK ID
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：认证TASK ID
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * 设置：报告类型
	 */
	public void setTianjiType(TianjiType tianjiType) {
		this.tianjiType = tianjiType;
	}
	/**
	 * 获取：报告类型
	 */
	public TianjiType getTianjiType() {
		return tianjiType;
	}
	/**
	 * 设置：报告类型
	 */
	public void setTianjiState(TianjiState tianjiState) {
		this.tianjiState = tianjiState;
	}
	/**
	 * 获取：报告类型
	 */
	public TianjiState getTianjiState() {
		return tianjiState;
	}
	/**
	 * 设置：用户名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：用户名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：身份证号码
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/**
	 * 获取：身份证号码
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * 设置：手机号
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：报告路径json
	 */
	public void setReportJsonPath(String reportJsonPath) {
		this.reportJsonPath = reportJsonPath;
	}
	/**
	 * 获取：报告路径json
	 */
	public String getReportJsonPath() {
		return reportJsonPath;
	}
	/**
	 * 设置：报告路径html
	 */
	public void setReportHtmlPath(String reportHtmlPath) {
		this.reportHtmlPath = reportHtmlPath;
	}
	/**
	 * 获取：报告路径html
	 */
	public String getReportHtmlPath() {
		return reportHtmlPath;
	}
	/**
	 * 设置：状态
	 */
	public void setVerifyStatus(VerifyStatus verifyStatus) {
		this.verifyStatus = verifyStatus;
	}
	/**
	 * 获取：状态
	 */
	public VerifyStatus getVerifyStatus() {
		return verifyStatus;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：创建时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(final boolean expired) {
		isExpired = expired;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(final String searchId) {
		this.searchId = searchId;
	}
}

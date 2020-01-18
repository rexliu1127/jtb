package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.auth.enums.DsType;
import io.grx.modules.auth.enums.VerifyStatus;


/**
 * 电商认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthUserReportDsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//用户ID
	private Long userId;
	//认证TASK ID
	private String taskId;
	//电商类型  0：京东   1：苏宁
	private DsType dsType;
	//用户名称
	private String name;
	//身份证号码
	private String idNo;
	//手机号
	private String mobile;
	//联系人内容
	private String reportData;
	//状态
	private VerifyStatus verifyStatus;
	//创建时间
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(final String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(final String taskId) {
		this.taskId = taskId;
	}

	public DsType getDsType() {
		return dsType;
	}

	public void setDsType(final DsType dsType) {
		this.dsType = dsType;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(final String idNo) {
		this.idNo = idNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(final String reportData) {
		this.reportData = reportData;
	}

	public VerifyStatus getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(final VerifyStatus verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}
}

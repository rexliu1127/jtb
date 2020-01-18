package io.grx.modules.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.grx.modules.auth.enums.StaffType;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 客服人员
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-01 00:36:13
 */
public class AuthStaffEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Long staffId;
	//商家编号
	private String merchantNo;
	//客服类型: 0-微信 1-QQ
    @JsonIgnore
	private StaffType staffType = StaffType.WX;
	//客服号码
	private String staffNo;
	//客服二维码图片
	private String staffBarcode;
	//关联系统用户ID
	private Long processorId;
	//创建时间
	private Date createTime = new Date();
	//更新时间
	private Date updateTime = createTime;

	private String processorName;

	/**
	 * 设置：
	 */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}
	/**
	 * 获取：
	 */
	public Long getStaffId() {
		return staffId;
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
	 * 设置：客服类型: 0-微信 1-QQ
	 */
	public void setStaffType(StaffType staffType) {
		this.staffType = staffType;
	}
	/**
	 * 获取：客服类型: 0-微信 1-QQ
	 */
	public StaffType getStaffType() {
		return staffType;
	}
	/**
	 * 设置：客服号码
	 */
	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	/**
	 * 获取：客服号码
	 */
	public String getStaffNo() {
		return staffNo;
	}
	/**
	 * 设置：客服二维码图片
	 */
	public void setStaffBarcode(String staffBarcode) {
		this.staffBarcode = staffBarcode;
	}
	/**
	 * 获取：客服二维码图片
	 */
	public String getStaffBarcode() {
		return staffBarcode;
	}
	/**
	 * 设置：关联系统用户ID
	 */
	public void setProcessorId(Long processorId) {
		this.processorId = processorId;
	}
	/**
	 * 获取：关联系统用户ID
	 */
	public Long getProcessorId() {
		return processorId;
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

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}
}

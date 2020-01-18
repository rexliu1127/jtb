package io.grx.modules.opt.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import io.grx.modules.sys.entity.BaseEntity;
import java.util.List;

/**
 * 渠道
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
public class ChannelEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	//
	private Long channelId;
	//渠道名称
	@NotBlank(message="渠道名称不能为空")
	private String name;
	//渠道key
	private String channelKey;
	//负责人ID
	private Long ownerUserId;
	//审核者ID
	private Long auditorUserId;

	private String imagePath;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/**
	 * 部门ID
	 */
	@NotNull(message="部门不能为空")
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;
	private String ownerName;
	private String auditorName;
	//审核人员名称
	private String auditorUserName;
	//logo地址
	private String logoPath;
	//产品名称
	private String productName;
	
	/**
	 * APP进件展示的金额 | 隔开
	 */
	private String appMoneyList;
	
	public String getAppMoneyList() {
		return appMoneyList;
	}
	public void setAppMoneyList(String appMoneyList) {
		this.appMoneyList = appMoneyList;
	}

	/**
	 * 审核人员ID列表
	 */
	private List<Long> auditorIdList;
	/**
	 * 设置：
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	/**
	 * 获取：
	 */
	public Long getChannelId() {
		return channelId;
	}
	/**
	 * 设置：渠道名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：渠道名称
	 */
	public String getName() {
		return name;
	}

	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(final String channelKey) {
		this.channelKey = channelKey;
	}

	/**
	 * 设置：审核者ID
	 */
	public void setAuditorUserId(Long auditorUserId) {
		this.auditorUserId = auditorUserId;
	}
	/**
	 * 获取：审核者ID
	 */
	public Long getAuditorUserId() {
		return auditorUserId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(final Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(final String deptName) {
		this.deptName = deptName;
	}

	public Long getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Long ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public List<Long> getAuditorIdList() {
		return auditorIdList;
	}

	public void setAuditorIdList(final List<Long> auditorIdList) {
		this.auditorIdList = auditorIdList;
	}

	public void setAuditorUserName(String auditorUserName) {
		this.auditorUserName = auditorUserName;
	}

	public String getAuditorUserName() {
		return auditorUserName;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}

package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.ContactType;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.VerifyStatus;


/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
public class AuthRequestEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Long requestId;
	private String requestUuid;
	//商家编号
	private String merchantNo;

	/**
	 * 部门ID
	 */
	private Long deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	//申请用户ID
	private Long userId;
	//处理系统用户ID
	private Long processorId;
    //客服系统用户ID
    private Long assigneeId;
	//渠道ID
	private Long channelId;
	//用户名称
	private String name;
	//身份证号码
	private String idNo;
	//身份证号码
	private String qqNo;
	private String gpsAddr;
	//手机服务密码
	private String mobilePass;
	//联系人1类型
	private ContactType contact1Type;
	//联系人1名字
	private String contact1Name;
	//联系人1电话
	private String contact1Mobile;
	//联系人2类型
	private ContactType contact2Type;
	//联系人2名字
	private String contact2Name;
	//联系人2电话
	private String contact2Mobile;
	//身份证正面url
	private String idUrl1;
	//身份证反面url
	private String idUrl2;
	//手持身份证url
	private String idUrl3;
	//状态
	private RequestStatus status;
	//认证token
	private String verifyToken;
	// 认证 status
	private VerifyStatus verifyStatus;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	// 身份证三要素是否匹配
	private Boolean phoneChecked;
	// 身份证是否跟运营商匹配
	private boolean idNoMatched;
	// 名字是否跟运营商匹配
	private boolean nameMatched;

	private Integer contact1CallCount;

	private Integer contact2CallCount;

	private AuthVendorType vendorType;

    private String wechatNo;
    private String companyName;
    private String companyAddr;
    private String companyTel;

    //联系人3类型
    private ContactType contact3Type = ContactType.RELATIVE;
    //联系人3名字
    private String contact3Name;
    //联系人3电话
    private String contact3Mobile;
	//经度
	private String latitude;
	//纬度
	private String longitude;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * 设置：
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	/**
	 * 获取：
	 */
	public Long getRequestId() {
		return requestId;
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
	 * 设置：申请用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：申请用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：处理系统用户ID
	 */
	public void setProcessorId(Long processorId) {
		this.processorId = processorId;
	}
	/**
	 * 获取：处理系统用户ID
	 */
	public Long getProcessorId() {
		return processorId;
	}
	/**
	 * 设置：渠道ID
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	/**
	 * 获取：渠道ID
	 */
	public Long getChannelId() {
		return channelId;
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
	 * 设置：身份证号码
	 */
	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}
	/**
	 * 获取：身份证号码
	 */
	public String getQqNo() {
		return qqNo;
	}
	/**
	 * 设置：手机服务密码
	 */
	public void setMobilePass(String mobilePass) {
		this.mobilePass = mobilePass;
	}
	/**
	 * 获取：手机服务密码
	 */
	public String getMobilePass() {
		return mobilePass;
	}
	/**
	 * 设置：联系人1类型
	 */
	public void setContact1Type(ContactType contact1Type) {
		this.contact1Type = contact1Type;
	}
	/**
	 * 获取：联系人1类型
	 */
	public ContactType getContact1Type() {
		return contact1Type;
	}
	/**
	 * 设置：联系人1名字
	 */
	public void setContact1Name(String contact1Name) {
		this.contact1Name = contact1Name;
	}
	/**
	 * 获取：联系人1名字
	 */
	public String getContact1Name() {
		return contact1Name;
	}
	/**
	 * 设置：联系人1电话
	 */
	public void setContact1Mobile(String contact1Mobile) {
		this.contact1Mobile = contact1Mobile;
	}
	/**
	 * 获取：联系人1电话
	 */
	public String getContact1Mobile() {
		return contact1Mobile;
	}
	/**
	 * 设置：联系人2类型
	 */
	public void setContact2Type(ContactType contact2Type) {
		this.contact2Type = contact2Type;
	}
	/**
	 * 获取：联系人2类型
	 */
	public ContactType getContact2Type() {
		return contact2Type;
	}
	/**
	 * 设置：联系人2名字
	 */
	public void setContact2Name(String contact2Name) {
		this.contact2Name = contact2Name;
	}
	/**
	 * 获取：联系人2名字
	 */
	public String getContact2Name() {
		return contact2Name;
	}
	/**
	 * 设置：联系人2电话
	 */
	public void setContact2Mobile(String contact2Mobile) {
		this.contact2Mobile = contact2Mobile;
	}
	/**
	 * 获取：联系人2电话
	 */
	public String getContact2Mobile() {
		return contact2Mobile;
	}
	/**
	 * 设置：身份证正面url
	 */
	public void setIdUrl1(String idUrl1) {
		this.idUrl1 = idUrl1;
	}
	/**
	 * 获取：身份证正面url
	 */
	public String getIdUrl1() {
		return idUrl1;
	}
	/**
	 * 设置：身份证反面url
	 */
	public void setIdUrl2(String idUrl2) {
		this.idUrl2 = idUrl2;
	}
	/**
	 * 获取：身份证反面url
	 */
	public String getIdUrl2() {
		return idUrl2;
	}
	/**
	 * 设置：手持身份证url
	 */
	public void setIdUrl3(String idUrl3) {
		this.idUrl3 = idUrl3;
	}
	/**
	 * 获取：手持身份证url
	 */
	public String getIdUrl3() {
		return idUrl3;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public RequestStatus getStatus() {
		return status;
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

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(final String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public VerifyStatus getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(final VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

	public boolean isIdNoMatched() {
		return idNoMatched;
	}

	public void setIdNoMatched(final boolean idNoMatched) {
		this.idNoMatched = idNoMatched;
	}

	public boolean isNameMatched() {
		return nameMatched;
	}

	public void setNameMatched(final boolean nameMatched) {
		this.nameMatched = nameMatched;
	}

	public Integer getContact1CallCount() {
		return contact1CallCount;
	}

	public void setContact1CallCount(final Integer contact1CallCount) {
		this.contact1CallCount = contact1CallCount;
	}

	public Integer getContact2CallCount() {
		return contact2CallCount;
	}

	public void setContact2CallCount(final Integer contact2CallCount) {
		this.contact2CallCount = contact2CallCount;
	}

    public AuthVendorType getVendorType() {
        return vendorType;
    }

    public void setVendorType(final AuthVendorType vendorType) {
        this.vendorType = vendorType;
    }

	public Boolean getPhoneChecked() {
		return phoneChecked;
	}

	public void setPhoneChecked(final Boolean phoneChecked) {
		this.phoneChecked = phoneChecked;
	}

	public String getGpsAddr() {
		return gpsAddr;
	}

	public void setGpsAddr(final String gpsAddr) {
		this.gpsAddr = gpsAddr;
	}

    public String getWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(final String wechatNo) {
        this.wechatNo = wechatNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(final String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(final String companyTel) {
        this.companyTel = companyTel;
    }

    public ContactType getContact3Type() {
        return contact3Type;
    }

    public void setContact3Type(final ContactType contact3Type) {
        this.contact3Type = contact3Type;
    }

    public String getContact3Name() {
        return contact3Name;
    }

    public void setContact3Name(final String contact3Name) {
        this.contact3Name = contact3Name;
    }

    public String getContact3Mobile() {
        return contact3Mobile;
    }

    public void setContact3Mobile(final String contact3Mobile) {
        this.contact3Mobile = contact3Mobile;
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
}

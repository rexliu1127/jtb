package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.auth.enums.ContactType;


/**
 * 认证用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
public class AuthUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long userId;
	//商家编号
	private String merchantNo;
	//菜单名称
	private String mobile;
	private Long channelId;
	//用户名称
	private String name;
	// 微信ID
	private String wechatId;
	// 头像url
	private String headImageUrl;
	//身份证号码
	private String idNo;
	//身份证号码
	private String qqNo;
	//手机服务密码
	private String mobilePass;
	//联系人1类型
	private ContactType contact1Type = ContactType.SPOUSE;
	//联系人1名字
	private String contact1Name;
	//联系人1电话
	private String contact1Mobile;
	//联系人2类型
	private ContactType contact2Type = ContactType.PARENT;
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
	//创建时间
	private Date createTime = new Date();

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
	//公司职位
	private String companyJob;
	//薪资
	private String salary;
	//芝麻分
	private String sesamePoints;

	//淘宝芝麻分
	private String taobaoSesamePoints;

	private String machineId;

	private boolean authStatus;
	/**
	 * 运营商状态
	 */
	private boolean authOperatorStatus;

	private String authReportUrl;

	private String authReportJsonUrl;

	private String authTaskId;

	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：菜单名称
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：菜单名称
	 */
	public String getMobile() {
		return mobile;
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

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(final Long channelId) {
		this.channelId = channelId;
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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCompanyJob() {
		return companyJob;
	}

	public void setCompanyJob(String companyJob) {
		this.companyJob = companyJob;
	}

	public String getSesamePoints() {
		return sesamePoints;
	}

	public void setSesamePoints(String sesamePoints) {
		this.sesamePoints = sesamePoints;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public boolean isAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(boolean authStatus) {
		this.authStatus = authStatus;
	}

	public boolean isAuthOperatorStatus() {
		return authOperatorStatus;
	}

	public void setAuthOperatorStatus(boolean authOperatorStatus) {
		this.authOperatorStatus = authOperatorStatus;
	}

	public String getAuthReportUrl() {
		return authReportUrl;
	}

	public void setAuthReportUrl(String authReportUrl) {
		this.authReportUrl = authReportUrl;
	}

	public String getAuthTaskId() {
		return authTaskId;
	}

	public void setAuthTaskId(String authTaskId) {
		this.authTaskId = authTaskId;
	}

	public String getAuthReportJsonUrl() {
		return authReportJsonUrl;
	}

	public void setAuthReportJsonUrl(String authReportJsonUrl) {
		this.authReportJsonUrl = authReportJsonUrl;
	}

	public String getTaobaoSesamePoints() {
		return taobaoSesamePoints;
	}

	public void setTaobaoSesamePoints(String taobaoSesamePoints) {
		this.taobaoSesamePoints = taobaoSesamePoints;
	}

	@Override
	public String toString() {
		return "AuthUserEntity{" +
				"userId=" + userId +
				", merchantNo='" + merchantNo + '\'' +
				", mobile='" + mobile + '\'' +
				", channelId=" + channelId +
				", name='" + name + '\'' +
				", wechatId='" + wechatId + '\'' +
				", headImageUrl='" + headImageUrl + '\'' +
				", idNo='" + idNo + '\'' +
				", qqNo='" + qqNo + '\'' +
				", mobilePass='" + mobilePass + '\'' +
				", contact1Type=" + contact1Type +
				", contact1Name='" + contact1Name + '\'' +
				", contact1Mobile='" + contact1Mobile + '\'' +
				", contact2Type=" + contact2Type +
				", contact2Name='" + contact2Name + '\'' +
				", contact2Mobile='" + contact2Mobile + '\'' +
				", idUrl1='" + idUrl1 + '\'' +
				", idUrl2='" + idUrl2 + '\'' +
				", idUrl3='" + idUrl3 + '\'' +
				", createTime=" + createTime +
				", wechatNo='" + wechatNo + '\'' +
				", companyName='" + companyName + '\'' +
				", companyAddr='" + companyAddr + '\'' +
				", companyTel='" + companyTel + '\'' +
				", contact3Type=" + contact3Type +
				", contact3Name='" + contact3Name + '\'' +
				", contact3Mobile='" + contact3Mobile + '\'' +
				", companyJob='" + companyJob + '\'' +
				", salary='" + salary + '\'' +
				", sesamePoints='" + sesamePoints + '\'' +
				", taobaoSesamePoints='" + taobaoSesamePoints + '\'' +
				", machineId='" + machineId + '\'' +
				", authStatus=" + authStatus +
				", authOperatorStatus=" + authOperatorStatus +
				", authReportUrl='" + authReportUrl + '\'' +
				", authReportJsonUrl='" + authReportJsonUrl + '\'' +
				", authTaskId='" + authTaskId + '\'' +
				'}';
	}
}

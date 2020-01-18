package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 系统用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 00:49:35
 */
public class TxUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Long userId;
	//商家编号
	private String merchantNo;
	//微信openId
	private String wechatId;
	//微信unionId
	private String unionId;
	//微信昵称
	private String nickName;
	//微信头像url
	private String headImgUrl;
	//手机号
	private String mobile;
	//真实姓名
	private String name;
	//真实姓名拼音首字母
	private String namePinyin;
	//身份证号
	private String idNo;
	//银行帐号
	private String bankAccount;
	//银行名称
	private String bankName;
	// 签名图片path
	private String signImgPath;
	//创建者ID
	private Long createUserId;
	//创建时间
	private Date createTime = new Date();

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
	 * 设置：微信unionId
	 */
	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	/**
	 * 获取：微信unionId
	 */
	public String getWechatId() {
		return wechatId;
	}
	/**
	 * 设置：微信昵称
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * 获取：微信昵称
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * 设置：微信头像url
	 */
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	/**
	 * 获取：微信头像url
	 */
	public String getHeadImgUrl() {
		return headImgUrl;
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
	 * 设置：真实姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：真实姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：身份证号
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/**
	 * 获取：身份证号
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * 设置：银行帐号
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * 获取：银行帐号
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * 设置：银行名称
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * 获取：银行名称
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * 设置：创建者ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：创建者ID
	 */
	public Long getCreateUserId() {
		return createUserId;
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

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(final String unionId) {
		this.unionId = unionId;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(final String namePinyin) {
		this.namePinyin = namePinyin;
	}

    public String getSignImgPath() {
        return signImgPath;
    }

    public void setSignImgPath(String signImgPath) {
        this.signImgPath = signImgPath;
    }
}

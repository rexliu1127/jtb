package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   全知-电商报告(淘宝支付宝聚合)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthTaobaoPayVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//淘宝帐户名
	private String tbUserName;
	//淘宝买家级别
	private String tbVipLevel;
	//邮 箱
	private String tbemail;
	//淘气值
	private String taoScore;
	//真实姓名
	private String tbRealName;

	//绑定手机
	private String tbMobile;
	//好评率
	private String tbFavorableRate;
	//性别值
	private String tbgender;
	//是否实名
	private String  tbidentityStatus;
	//芝麻信用分
	private String  tbzmScore;

	//支付宝余额
	private String  alipayBalance;
	//余额宝余额
	private String alipayYuebao;
	//花呗总额度
	private String  alipayhbTotalLimit;
	//支付宝帐户名
	private String alipayUserName;
	//历史累计收益
	private String alipayYebTotalEarnings;
	//花呗可用额度
	private String alipayhbAvailableLimit;

	//支付宝绑定银行卡
	private List<AlipayBankcardsVO> alipayBankcardsList;
	//收货地址
	private List<TaobaoAddressVO>  taobaoAddressList;
	//订单信息
	private List<TrandeInfoVO>   trandeInfoList;


	public String getTbUserName() {
		return tbUserName;
	}

	public void setTbUserName(String tbUserName) {
		this.tbUserName = tbUserName;
	}

	public String getTbVipLevel() {
		return tbVipLevel;
	}

	public void setTbVipLevel(String tbVipLevel) {
		this.tbVipLevel = tbVipLevel;
	}

	public String getTbemail() {
		return tbemail;
	}

	public void setTbemail(String tbemail) {
		this.tbemail = tbemail;
	}

	public String getTaoScore() {
		return taoScore;
	}

	public void setTaoScore(String taoScore) {
		this.taoScore = taoScore;
	}

	public String getTbRealName() {
		return tbRealName;
	}

	public void setTbRealName(String tbRealName) {
		this.tbRealName = tbRealName;
	}

	public String getAlipayBalance() {
		return alipayBalance;
	}

	public void setAlipayBalance(String alipayBalance) {
		this.alipayBalance = alipayBalance;
	}

	public String getTbMobile() {
		return tbMobile;
	}

	public void setTbMobile(String tbMobile) {
		this.tbMobile = tbMobile;
	}

	public String getTbFavorableRate() {
		return tbFavorableRate;
	}

	public void setTbFavorableRate(String tbFavorableRate) {
		this.tbFavorableRate = tbFavorableRate;
	}

	public String getTbgender() {
		return tbgender;
	}

	public void setTbgender(String tbgender) {
		this.tbgender = tbgender;
	}

	public String getAlipayYuebao() {
		return alipayYuebao;
	}

	public void setAlipayYuebao(String alipayYuebao) {
		this.alipayYuebao = alipayYuebao;
	}

	public String getTbidentityStatus() {
		return tbidentityStatus;
	}

	public void setTbidentityStatus(String tbidentityStatus) {
		this.tbidentityStatus = tbidentityStatus;
	}

	public String getTbzmScore() {
		return tbzmScore;
	}

	public void setTbzmScore(String tbzmScore) {
		this.tbzmScore = tbzmScore;
	}

	public String getAlipayhbTotalLimit() {
		return alipayhbTotalLimit;
	}

	public void setAlipayhbTotalLimit(String alipayhbTotalLimit) {
		this.alipayhbTotalLimit = alipayhbTotalLimit;
	}

	public String getAlipayUserName() {
		return alipayUserName;
	}

	public void setAlipayUserName(String alipayUserName) {
		this.alipayUserName = alipayUserName;
	}

	public String getAlipayYebTotalEarnings() {
		return alipayYebTotalEarnings;
	}

	public void setAlipayYebTotalEarnings(String alipayYebTotalEarnings) {
		this.alipayYebTotalEarnings = alipayYebTotalEarnings;
	}

	public String getAlipayhbAvailableLimit() {
		return alipayhbAvailableLimit;
	}

	public void setAlipayhbAvailableLimit(String alipayhbAvailableLimit) {
		this.alipayhbAvailableLimit = alipayhbAvailableLimit;
	}

	public List<AlipayBankcardsVO> getAlipayBankcardsList() {
		return alipayBankcardsList;
	}

	public void setAlipayBankcardsList(List<AlipayBankcardsVO> alipayBankcardsList) {
		this.alipayBankcardsList = alipayBankcardsList;
	}

	public List<TaobaoAddressVO> getTaobaoAddressList() {
		return taobaoAddressList;
	}

	public void setTaobaoAddressList(List<TaobaoAddressVO> taobaoAddressList) {
		this.taobaoAddressList = taobaoAddressList;
	}

	public List<TrandeInfoVO> getTrandeInfoList() {
		return trandeInfoList;
	}

	public void setTrandeInfoList(List<TrandeInfoVO> trandeInfoList) {
		this.trandeInfoList = trandeInfoList;
	}
}

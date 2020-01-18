package io.grx.pay.util;

public class AllinPayTranxConfig {
//    public  String transxUrl = "https://113.108.182.3/aipg/ProcessServlet";
    public  String transxUrl = "https://tlt.allinpay.com/aipg/ProcessServlet";
    /**
     * XML交易参数
     */
    public   String acctName = "张国光";
    public   String acctNo = "6225882516636351";
    public   String amount = "100000";//交易金额
    public   String bankcode = "0105";//银行代码

    public   String cerPath= "config\\20058400001958504.cer";
    public   String merchantId = "200584000019585";

    //商户号：200604000000445
    public   String password = "111111";
    //商户证书信息
    public   String pfxPassword = "111111";
    public   String pfxPath= "config\\20058400001958504.p12";
    //ceshi
	/*public   String cerPath= "config\\20080100000000104.cer";
	public   String merchantId = "200801000000001";


	//商户号：200604000000445
	public   String password = "111111";
	//商户证书信息
	public   String pfxPassword = "111111";
	public   String pfxPath= "config\\20080100000000104.p12";*/





    public   String sum = "200000";//交易总金额
    public   String tel = "13434343434";
    public   String tltcerPath= "config\\allinpay-pds.cer";
    public   String userName = "20058400001958504";
    public String getAcctName() {
        return acctName;
    }
    public String getAcctNo() {
        return acctNo;
    }
    public String getAmount() {
        return amount;
    }
    public String getBankcode() {
        return bankcode;
    }
    public String getCerPath() {
        return cerPath;
    }
    public String getMerchantId() {
        return merchantId;
    }
    public String getPassword() {
        return password;
    }
    public String getPfxPassword() {
        return pfxPassword;
    }
    public String getPfxPath() {
        return pfxPath;
    }
    public String getSum() {
        return sum;
    }
    public String getTel() {
        return tel;
    }
    public String getTltcerPath() {
        return tltcerPath;
    }
    public String getUserName() {
        return userName;
    }
    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }
    public void setCerPath(String cerPath) {
        this.cerPath = cerPath;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPfxPassword(String pfxPassword) {
        this.pfxPassword = pfxPassword;
    }
    public void setPfxPath(String pfxPath) {
        this.pfxPath = pfxPath;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setTltcerPath(String tltcerPath) {
        this.tltcerPath = tltcerPath;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}

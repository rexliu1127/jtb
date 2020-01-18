package io.grx.auth.entity;

public class AuthConfig {
    public static final int HIDE = 0;
    public static final int MUST = 1;
    public static final int OPTIONAL = 2;
    public static final int EXTRA = 3;

    private boolean qqNo = true;
    private boolean wechatNo = true;
    private boolean companyName = true;
    private boolean companyJob = true;
    private boolean companyAddr = true;
    private boolean companyTel = true;
    private boolean salary = true;
    private boolean sesamePoints = true;

    private int relationCount = 3;

    private int mobile = MUST;
    private int contact = OPTIONAL;
    private int alipay = OPTIONAL;
    private int taobao = OPTIONAL;
    private int jingdong = OPTIONAL;
    private int mifang = EXTRA;
    private int jiedaibao = EXTRA;
    private int wuyoujietiao = EXTRA;
    private int jinjiedao = EXTRA;
    private int youpinzheng = EXTRA;
    private int insure = EXTRA;
    private int fund = EXTRA;
    private int huluobo = EXTRA;   //无需授权即可获得无忧借条  借贷宝  今借到等借条信息

    public int getHuluobo() {
        return huluobo;
    }

    public void setHuluobo(int huluobo) {
        this.huluobo = huluobo;
    }

    public boolean isQqNo() {
        return qqNo;
    }

    public void setQqNo(boolean qqNo) {
        this.qqNo = qqNo;
    }

    public boolean isWechatNo() {
        return wechatNo;
    }

    public void setWechatNo(boolean wechatNo) {
        this.wechatNo = wechatNo;
    }

    public boolean isCompanyName() {
        return companyName;
    }

    public void setCompanyName(boolean companyName) {
        this.companyName = companyName;
    }

    public boolean isCompanyJob() {
        return companyJob;
    }

    public void setCompanyJob(boolean companyJob) {
        this.companyJob = companyJob;
    }

    public boolean isCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(boolean companyAddr) {
        this.companyAddr = companyAddr;
    }

    public boolean isCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(boolean companyTel) {
        this.companyTel = companyTel;
    }

    public boolean isSalary() {
        return salary;
    }

    public void setSalary(boolean salary) {
        this.salary = salary;
    }

    public boolean isSesamePoints() {
        return sesamePoints;
    }

    public void setSesamePoints(boolean sesamePoints) {
        this.sesamePoints = sesamePoints;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public int getAlipay() {
        return alipay;
    }

    public void setAlipay(int alipay) {
        this.alipay = alipay;
    }

    public int getTaobao() {
        return taobao;
    }

    public void setTaobao(int taobao) {
        this.taobao = taobao;
    }

    public int getJingdong() {
        return jingdong;
    }

    public void setJingdong(int jingdong) {
        this.jingdong = jingdong;
    }

    public int getMifang() {
        return mifang;
    }

    public void setMifang(int mifang) {
        this.mifang = mifang;
    }

    public int getJiedaibao() {
        return jiedaibao;
    }

    public void setJiedaibao(int jiedaibao) {
        this.jiedaibao = jiedaibao;
    }

    public int getWuyoujietiao() {
        return wuyoujietiao;
    }

    public void setWuyoujietiao(int wuyoujietiao) {
        this.wuyoujietiao = wuyoujietiao;
    }

    public int getJinjiedao() {
        return jinjiedao;
    }

    public void setJinjiedao(int jinjiedao) {
        this.jinjiedao = jinjiedao;
    }

    public int getYoupinzheng() {
        return youpinzheng;
    }

    public void setYoupinzheng(int youpinzheng) {
        this.youpinzheng = youpinzheng;
    }

    public int getInsure() {
        return insure;
    }

    public void setInsure(int insure) {
        this.insure = insure;
    }

    public int getFund() {
        return fund;
    }

    public void setFund(int fund) {
        this.fund = fund;
    }

    public int getRelationCount() {
        return relationCount;
    }

    public void setRelationCount(int relationCount) {
        this.relationCount = relationCount;
    }
}

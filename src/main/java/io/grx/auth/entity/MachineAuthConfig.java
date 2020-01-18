package io.grx.auth.entity;

import java.math.BigDecimal;

public class MachineAuthConfig {
    public static final BigDecimal DEFAULTVALUEBIGDECIMAL = new BigDecimal(0);
    public static final int DEFAULTVALUEINT = 0;
    public static final int UNCHECKED = 0;
    public static final int REALNAME = 1;
    public static final int HALFYEAR = 2;
    public static final int ONEYEAR = 3;


    private String ages;

    private boolean beijing = false;
    private boolean tianjin = false;
    private boolean hebei = false;
    private boolean shanxijin = false;
    private boolean neimenggu = false;
    private boolean liaoning = false;
    private boolean jilin = false;
    private boolean heilongjiang = false;
    private boolean shanghai = false;
    private boolean jingsu = false;
    private boolean zhejiang = false;
    private boolean anhui = false;
    private boolean fujian = false;
    private boolean jiangxi = false;
    private boolean shandong = false;
    private boolean henan = false;
    private boolean hubei = false;
    private boolean hunan = false;
    private boolean guangdong = false;
    private boolean guangxi = false;
    private boolean hainan = false;
    private boolean chongqing = false;
    private boolean sichuan = false;
    private boolean guizhou = false;
    private boolean yunnan = false;
    private boolean xizang = false;
    private boolean shanxi = false;
    private boolean gansu = false;
    private boolean qinghai = false;
    private boolean ningxia = false;
    private boolean xinjiang = false;
    private boolean taiwan = false;
    private boolean xianggang = false;
    private boolean aomen = false;

    //今借到超过查询的负债金额
    private BigDecimal amountOfDebt = DEFAULTVALUEBIGDECIMAL;
    //今借到超过查询的逾期金额
    private BigDecimal overdueAmount = DEFAULTVALUEBIGDECIMAL;
    //今借到借款订单数
    private int loanOrderAmount = DEFAULTVALUEINT;
    //今借到待还款订单数
    private int orderAmountToBePaid = DEFAULTVALUEINT;
    //米兜凭证是否超过查询的负债金额
    private BigDecimal miDouAmountOfDebt = DEFAULTVALUEBIGDECIMAL;
    //米兜凭证是否超过查询的逾期金额
    private BigDecimal miDouOverdueAmount = DEFAULTVALUEBIGDECIMAL;
    //米兜凭证借条订单数
    private int miDouLoanOrderAmount = DEFAULTVALUEINT;
    //米兜凭证待还款订单数
    private int miDouOrderAmountToBePaid = DEFAULTVALUEINT;
    //米兜凭证今日待还款订单数
    private int miDouTodayOrderAmountToBePaid = DEFAULTVALUEINT;
    //米兜凭证当日新增借条数量
    private int miDouLoanOrderTodaysNewNumber = DEFAULTVALUEINT;
    //芝麻分是否超过当前查询
    private int sesamePoints = DEFAULTVALUEINT;
    //芝麻信用逾期未处理
    private boolean sesamePointsIsOverdue = true;
    //借贷宝是否超过查询的负债金额
    private BigDecimal jieDaiBaoAmountOfDebt = DEFAULTVALUEBIGDECIMAL;
    //借贷宝是否超过查询的逾期金额
    private BigDecimal jieDaiBaoOverdueAmount = DEFAULTVALUEBIGDECIMAL;
    //借贷宝借款订单数
    private int jieDaiBaoLoanOrderAmount = DEFAULTVALUEINT;
    //借贷宝待还款订单数
    private int jieDaiBaoOrderAmountToBePaid = DEFAULTVALUEINT;
    //运营商是否认证
    private boolean operatorIsCertification = true;
    //互通电话个数
    private int numberOfCalls = DEFAULTVALUEINT;
    //手机实名时间
    private int realNameTime = REALNAME;
    //非活跃天数
    private int unActiveDays = DEFAULTVALUEINT;
    //前十联系人最近一个月命中网贷人数
    private int topTenNetCredits = DEFAULTVALUEINT;
    //前十联系人匹配成功人数
    private int matchSuccessfuls = DEFAULTVALUEINT;
    //近一月疑似被催贷
    private boolean beingCalledBorrow = true;
    //京东信息认证
    private boolean jingDongInformationAuthentication = true;
    //淘宝信息认证
    private boolean taobaoInformationAuthentication = true;
    //花呗额度
    private BigDecimal spendBaiLines = DEFAULTVALUEBIGDECIMAL;
    //社保认证
    private boolean socialSecurityAuthentication = true;
    //公积金认证
    private boolean providentFundCertification = true;

    public String getAges() {
        return ages;
    }

    public void setAges(String ages) {
        this.ages = ages;
    }

    public boolean isBeijing() {
        return beijing;
    }

    public void setBeijing(boolean beijing) {
        this.beijing = beijing;
    }

    public boolean isTianjin() {
        return tianjin;
    }

    public void setTianjin(boolean tianjin) {
        this.tianjin = tianjin;
    }

    public boolean isHebei() {
        return hebei;
    }

    public void setHebei(boolean hebei) {
        this.hebei = hebei;
    }

    public boolean isShanxijin() {
        return shanxijin;
    }

    public void setShanxijin(boolean shanxijin) {
        this.shanxijin = shanxijin;
    }

    public boolean isNeimenggu() {
        return neimenggu;
    }

    public void setNeimenggu(boolean neimenggu) {
        this.neimenggu = neimenggu;
    }

    public boolean isLiaoning() {
        return liaoning;
    }

    public void setLiaoning(boolean liaoning) {
        this.liaoning = liaoning;
    }

    public boolean isJilin() {
        return jilin;
    }

    public void setJilin(boolean jilin) {
        this.jilin = jilin;
    }

    public boolean isHeilongjiang() {
        return heilongjiang;
    }

    public void setHeilongjiang(boolean heilongjiang) {
        this.heilongjiang = heilongjiang;
    }

    public boolean isShanghai() {
        return shanghai;
    }

    public void setShanghai(boolean shanghai) {
        this.shanghai = shanghai;
    }

    public boolean isJingsu() {
        return jingsu;
    }

    public void setJingsu(boolean jingsu) {
        this.jingsu = jingsu;
    }

    public boolean isZhejiang() {
        return zhejiang;
    }

    public void setZhejiang(boolean zhejiang) {
        this.zhejiang = zhejiang;
    }

    public boolean isAnhui() {
        return anhui;
    }

    public void setAnhui(boolean anhui) {
        this.anhui = anhui;
    }

    public boolean isFujian() {
        return fujian;
    }

    public void setFujian(boolean fujian) {
        this.fujian = fujian;
    }

    public boolean isJiangxi() {
        return jiangxi;
    }

    public void setJiangxi(boolean jiangxi) {
        this.jiangxi = jiangxi;
    }

    public boolean isShandong() {
        return shandong;
    }

    public void setShandong(boolean shandong) {
        this.shandong = shandong;
    }

    public boolean isHenan() {
        return henan;
    }

    public void setHenan(boolean henan) {
        this.henan = henan;
    }

    public boolean isHubei() {
        return hubei;
    }

    public void setHubei(boolean hubei) {
        this.hubei = hubei;
    }

    public boolean isHunan() {
        return hunan;
    }

    public void setHunan(boolean hunan) {
        this.hunan = hunan;
    }

    public boolean isGuangdong() {
        return guangdong;
    }

    public void setGuangdong(boolean guangdong) {
        this.guangdong = guangdong;
    }

    public boolean isGuangxi() {
        return guangxi;
    }

    public void setGuangxi(boolean guangxi) {
        this.guangxi = guangxi;
    }

    public boolean isHainan() {
        return hainan;
    }

    public void setHainan(boolean hainan) {
        this.hainan = hainan;
    }

    public boolean isChongqing() {
        return chongqing;
    }

    public void setChongqing(boolean chongqing) {
        this.chongqing = chongqing;
    }

    public boolean isSichuan() {
        return sichuan;
    }

    public void setSichuan(boolean sichuan) {
        this.sichuan = sichuan;
    }

    public boolean isGuizhou() {
        return guizhou;
    }

    public void setGuizhou(boolean guizhou) {
        this.guizhou = guizhou;
    }

    public boolean isYunnan() {
        return yunnan;
    }

    public void setYunnan(boolean yunnan) {
        this.yunnan = yunnan;
    }

    public boolean isXizang() {
        return xizang;
    }

    public void setXizang(boolean xizang) {
        this.xizang = xizang;
    }

    public boolean isShanxi() {
        return shanxi;
    }

    public void setShanxi(boolean shanxi) {
        this.shanxi = shanxi;
    }

    public boolean isGansu() {
        return gansu;
    }

    public void setGansu(boolean gansu) {
        this.gansu = gansu;
    }

    public boolean isQinghai() {
        return qinghai;
    }

    public void setQinghai(boolean qinghai) {
        this.qinghai = qinghai;
    }

    public boolean isNingxia() {
        return ningxia;
    }

    public void setNingxia(boolean ningxia) {
        this.ningxia = ningxia;
    }

    public boolean isXinjiang() {
        return xinjiang;
    }

    public void setXinjiang(boolean xinjiang) {
        this.xinjiang = xinjiang;
    }

    public boolean isTaiwan() {
        return taiwan;
    }

    public void setTaiwan(boolean taiwan) {
        this.taiwan = taiwan;
    }

    public boolean isXianggang() {
        return xianggang;
    }

    public void setXianggang(boolean xianggang) {
        this.xianggang = xianggang;
    }

    public boolean isAomen() {
        return aomen;
    }

    public void setAomen(boolean aomen) {
        this.aomen = aomen;
    }

    public BigDecimal getAmountOfDebt() {
        return amountOfDebt;
    }

    public void setAmountOfDebt(BigDecimal amountOfDebt) {
        this.amountOfDebt = amountOfDebt;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public int getLoanOrderAmount() {
        return loanOrderAmount;
    }

    public void setLoanOrderAmount(int loanOrderAmount) {
        this.loanOrderAmount = loanOrderAmount;
    }

    public int getOrderAmountToBePaid() {
        return orderAmountToBePaid;
    }

    public void setOrderAmountToBePaid(int orderAmountToBePaid) {
        this.orderAmountToBePaid = orderAmountToBePaid;
    }

    public BigDecimal getMiDouAmountOfDebt() {
        return miDouAmountOfDebt;
    }

    public void setMiDouAmountOfDebt(BigDecimal miDouAmountOfDebt) {
        this.miDouAmountOfDebt = miDouAmountOfDebt;
    }

    public BigDecimal getMiDouOverdueAmount() {
        return miDouOverdueAmount;
    }

    public void setMiDouOverdueAmount(BigDecimal miDouOverdueAmount) {
        this.miDouOverdueAmount = miDouOverdueAmount;
    }

    public int getMiDouLoanOrderAmount() {
        return miDouLoanOrderAmount;
    }

    public void setMiDouLoanOrderAmount(int miDouLoanOrderAmount) {
        this.miDouLoanOrderAmount = miDouLoanOrderAmount;
    }

    public int getMiDouOrderAmountToBePaid() {
        return miDouOrderAmountToBePaid;
    }

    public void setMiDouOrderAmountToBePaid(int miDouOrderAmountToBePaid) {
        this.miDouOrderAmountToBePaid = miDouOrderAmountToBePaid;
    }

    public int getMiDouTodayOrderAmountToBePaid() {
        return miDouTodayOrderAmountToBePaid;
    }

    public void setMiDouTodayOrderAmountToBePaid(int miDouTodayOrderAmountToBePaid) {
        this.miDouTodayOrderAmountToBePaid = miDouTodayOrderAmountToBePaid;
    }

    public int getMiDouLoanOrderTodaysNewNumber() {
        return miDouLoanOrderTodaysNewNumber;
    }

    public void setMiDouLoanOrderTodaysNewNumber(int miDouLoanOrderTodaysNewNumber) {
        this.miDouLoanOrderTodaysNewNumber = miDouLoanOrderTodaysNewNumber;
    }

    public int getSesamePoints() {
        return sesamePoints;
    }

    public void setSesamePoints(int sesamePoints) {
        this.sesamePoints = sesamePoints;
    }

    public boolean isSesamePointsIsOverdue() {
        return sesamePointsIsOverdue;
    }

    public void setSesamePointsIsOverdue(boolean sesamePointsIsOverdue) {
        this.sesamePointsIsOverdue = sesamePointsIsOverdue;
    }

    public BigDecimal getJieDaiBaoAmountOfDebt() {
        return jieDaiBaoAmountOfDebt;
    }

    public void setJieDaiBaoAmountOfDebt(BigDecimal jieDaiBaoAmountOfDebt) {
        this.jieDaiBaoAmountOfDebt = jieDaiBaoAmountOfDebt;
    }

    public BigDecimal getJieDaiBaoOverdueAmount() {
        return jieDaiBaoOverdueAmount;
    }

    public void setJieDaiBaoOverdueAmount(BigDecimal jieDaiBaoOverdueAmount) {
        this.jieDaiBaoOverdueAmount = jieDaiBaoOverdueAmount;
    }

    public int getJieDaiBaoLoanOrderAmount() {
        return jieDaiBaoLoanOrderAmount;
    }

    public void setJieDaiBaoLoanOrderAmount(int jieDaiBaoLoanOrderAmount) {
        this.jieDaiBaoLoanOrderAmount = jieDaiBaoLoanOrderAmount;
    }

    public int getJieDaiBaoOrderAmountToBePaid() {
        return jieDaiBaoOrderAmountToBePaid;
    }

    public void setJieDaiBaoOrderAmountToBePaid(int jieDaiBaoOrderAmountToBePaid) {
        this.jieDaiBaoOrderAmountToBePaid = jieDaiBaoOrderAmountToBePaid;
    }

    public boolean isOperatorIsCertification() {
        return operatorIsCertification;
    }

    public void setOperatorIsCertification(boolean operatorIsCertification) {
        this.operatorIsCertification = operatorIsCertification;
    }

    public int getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(int numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public int getRealNameTime() {
        return realNameTime;
    }

    public void setRealNameTime(int realNameTime) {
        this.realNameTime = realNameTime;
    }

    public int getUnActiveDays() {
        return unActiveDays;
    }

    public void setUnActiveDays(int unActiveDays) {
        this.unActiveDays = unActiveDays;
    }

    public int getTopTenNetCredits() {
        return topTenNetCredits;
    }

    public void setTopTenNetCredits(int topTenNetCredits) {
        this.topTenNetCredits = topTenNetCredits;
    }

    public int getMatchSuccessfuls() {
        return matchSuccessfuls;
    }

    public void setMatchSuccessfuls(int matchSuccessfuls) {
        this.matchSuccessfuls = matchSuccessfuls;
    }

    public boolean isBeingCalledBorrow() {
        return beingCalledBorrow;
    }

    public void setBeingCalledBorrow(boolean beingCalledBorrow) {
        this.beingCalledBorrow = beingCalledBorrow;
    }

    public boolean isJingDongInformationAuthentication() {
        return jingDongInformationAuthentication;
    }

    public void setJingDongInformationAuthentication(boolean jingDongInformationAuthentication) {
        this.jingDongInformationAuthentication = jingDongInformationAuthentication;
    }

    public boolean isTaobaoInformationAuthentication() {
        return taobaoInformationAuthentication;
    }

    public void setTaobaoInformationAuthentication(boolean taobaoInformationAuthentication) {
        this.taobaoInformationAuthentication = taobaoInformationAuthentication;
    }

    public BigDecimal getSpendBaiLines() {
        return spendBaiLines;
    }

    public void setSpendBaiLines(BigDecimal spendBaiLines) {
        this.spendBaiLines = spendBaiLines;
    }

    public boolean isSocialSecurityAuthentication() {
        return socialSecurityAuthentication;
    }

    public void setSocialSecurityAuthentication(boolean socialSecurityAuthentication) {
        this.socialSecurityAuthentication = socialSecurityAuthentication;
    }

    public boolean isProvidentFundCertification() {
        return providentFundCertification;
    }

    public void setProvidentFundCertification(boolean providentFundCertification) {
        this.providentFundCertification = providentFundCertification;
    }
}

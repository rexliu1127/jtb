package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.enums.UsageType;


/**
 * 交易基本资料
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-29 09:40:01
 */
public class TxBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Long txId;
	//交易全局ID
	private String txUuid;
	//商家编号
	private String merchantNo;
	//借款金额
	private Integer amount;
	//剩余金额
	private Integer outstandingAmount;
	//开始日期
	private Date beginDate;
	//还款日期
	private Date endDate;
	//利率*100
	private Integer rate;
	//预计利息
	private BigDecimal interest;
	private BigDecimal outstandingInterest;
	//费用
	private BigDecimal feeAmount = BigDecimal.ZERO;
	//费用
	private String remark;
	//借款用途类型
	private UsageType usageType;
	//借款用途补充说明
	private String usageRemark;
	//借款人姓名
	private String borrowerName;
	//出借人姓名
	private String lenderName;
	//借款人ID
	private Long borrowerUserId;
	//出借人ID
	private Long lenderUserId;
	//创建者ID
	private Long createUserId;
	//状态
	private TxStatus status = TxStatus.UNKNOWN;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;

	private Date overdueDate;
	private Date repayDate;

	private String borrowerSignImgPath; // 借款人签名
	private String lenderSignImgPath; // 借款人签名

	/**
	 * 设置：
	 */
	public void setTxId(Long txId) {
		this.txId = txId;
	}
	/**
	 * 获取：
	 */
	public Long getTxId() {
		return txId;
	}
	/**
	 * 设置：交易全局ID
	 */
	public void setTxUuid(String txUuid) {
		this.txUuid = txUuid;
	}
	/**
	 * 获取：交易全局ID
	 */
	public String getTxUuid() {
		return txUuid;
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
	 * 设置：借款金额
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：借款金额
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 设置：开始日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * 获取：开始日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * 设置：还款日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：还款日期
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * 设置：利率*100
	 */
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	/**
	 * 获取：利率*100
	 */
	public Integer getRate() {
		return rate;
	}
	/**
	 * 设置：预计利息
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	/**
	 * 获取：预计利息
	 */
	public BigDecimal getInterest() {
		return interest;
	}
	/**
	 * 设置：费用
	 */
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	/**
	 * 获取：费用
	 */
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	/**
	 * 设置：费用
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：费用
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：借款用途类型
	 */
	public void setUsageType(UsageType usageType) {
		this.usageType = usageType;
	}
	/**
	 * 获取：借款用途类型
	 */
	public UsageType getUsageType() {
		return usageType;
	}
	/**
	 * 设置：借款用途补充说明
	 */
	public void setUsageRemark(String usageRemark) {
		this.usageRemark = usageRemark;
	}
	/**
	 * 获取：借款用途补充说明
	 */
	public String getUsageRemark() {
		return usageRemark;
	}
	/**
	 * 设置：借款人姓名
	 */
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	/**
	 * 获取：借款人姓名
	 */
	public String getBorrowerName() {
		return borrowerName;
	}
	/**
	 * 设置：出借人姓名
	 */
	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}
	/**
	 * 获取：出借人姓名
	 */
	public String getLenderName() {
		return lenderName;
	}
	/**
	 * 设置：借款人ID
	 */
	public void setBorrowerUserId(Long borrowerUserId) {
		this.borrowerUserId = borrowerUserId;
	}
	/**
	 * 获取：借款人ID
	 */
	public Long getBorrowerUserId() {
		return borrowerUserId;
	}
	/**
	 * 设置：出借人ID
	 */
	public void setLenderUserId(Long lenderUserId) {
		this.lenderUserId = lenderUserId;
	}
	/**
	 * 获取：出借人ID
	 */
	public Long getLenderUserId() {
		return lenderUserId;
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
	 * 设置：状态
	 */
	public void setStatus(TxStatus status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public TxStatus getStatus() {
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(final Date updateTime) {
		this.updateTime = updateTime;
	}

    public Date getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(final Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(final Date repayDate) {
        this.repayDate = repayDate;
    }

	public Integer getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(final Integer outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

    public BigDecimal getOutstandingInterest() {
        return outstandingInterest;
    }

    public void setOutstandingInterest(final BigDecimal outstandingInterest) {
        this.outstandingInterest = outstandingInterest;
    }

    public String getBorrowerSignImgPath() {
        return borrowerSignImgPath;
    }

    public void setBorrowerSignImgPath(String borrowerSignImgPath) {
        this.borrowerSignImgPath = borrowerSignImgPath;
    }

    public String getLenderSignImgPath() {
        return lenderSignImgPath;
    }

    public void setLenderSignImgPath(String lenderSignImgPath) {
        this.lenderSignImgPath = lenderSignImgPath;
    }
}

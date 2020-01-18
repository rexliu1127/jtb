package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--黑名单检查
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class BlackCheckInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//是否出现
	private boolean arised;
	//黑名单机构类型
	private List<String> blackType;
	//个数
	private Integer number;
	//黑名单机构类型字符串
	private String blackTypeStr;

	public String getBlackTypeStr() {
		return blackTypeStr;
	}

	public void setBlackTypeStr(String blackTypeStr) {
		this.blackTypeStr = blackTypeStr;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public boolean isArised() {
		return arised;
	}

	public void setArised(boolean arised) {
		this.arised = arised;
	}

	public List<String> getBlackType() {
		return blackType;
	}

	public void setBlackType(List<String> blackType) {
		this.blackType = blackType;
	}
}

package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   运营商报告--联系人地区分布
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class ContactsDistributionInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//具体地点
	private String area;
	//具体比例
	private Float ratio;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}
}

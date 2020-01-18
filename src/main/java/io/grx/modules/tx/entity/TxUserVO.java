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
public class TxUserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String label;// 用户名/账号

	private String value; //姓名



	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

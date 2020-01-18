package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   灯塔-胡萝卜获取无忧信息
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthWuYouVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String idcard;
	private String result;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

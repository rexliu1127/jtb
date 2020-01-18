package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   米兜综合报告--有盾
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class YouDunGraphDataVO implements Serializable {
	private static final long serialVersionUID = 1L;

	////关联图谱设备对应状态 0是表示用户本身 1=正常用户 2=商户黑名单 3=有盾&商户黑名单 4=有盾黑名单 指向“用户”
	private Integer category;
	private String name;

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

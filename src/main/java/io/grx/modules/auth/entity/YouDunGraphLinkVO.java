package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   米兜综合报告--有盾
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class YouDunGraphLinkVO implements Serializable {
	private static final long serialVersionUID = 1L;

	////关联图谱设备对应状态 0是表示用户本身 1=正常用户 2=商户黑名单 3=有盾&商户黑名单 4=有盾黑名单 指向“用户”
	private String source;
	private String name;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

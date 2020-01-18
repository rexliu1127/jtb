package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 渠道与审核人员对应关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 17:58:41
 */
public class SysChannelUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//渠道ID
	private Long channelId;
	//用户ID
	private Long userId;

	//用户名称
	private String userName;
	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：渠道ID
	 */
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	/**
	 * 获取：渠道ID
	 */
	public Long getChannelId() {
		return channelId;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：用户姓名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户姓名
	 */
	public String getUserName() {
		return userName;
	}
}

package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 渠道用户与渠道对应关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-16 23:57:03
 */
public class SysUserChannelEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用户ID
	private Long userId;
	//渠道ID
	private Long channelId;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
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
}

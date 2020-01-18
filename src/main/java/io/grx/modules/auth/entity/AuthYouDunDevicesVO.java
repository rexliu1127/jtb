package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   有盾-设备详情
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthYouDunDevicesVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//设备名称
	private String deviceName;

	//设备ID
	private String deviceId;

	//最近使用时间
	private String lastUseData;

	//设备是否使用代理
	private String usingProxyPort;

	//设备越狱
	private String isRooted;

	//设备关联用户数
	private String  linkIdCount;

	//网络类型
	private String network;

	//是否安装作弊软件
	private Integer cheatsDevice;

	//借贷APP安装数量
	private String appCount;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getLastUseData() {
		return lastUseData;
	}

	public void setLastUseData(String lastUseData) {
		this.lastUseData = lastUseData;
	}

	public String getUsingProxyPort() {
		return usingProxyPort;
	}

	public void setUsingProxyPort(String usingProxyPort) {
		this.usingProxyPort = usingProxyPort;
	}

	public String getIsRooted() {
		return isRooted;
	}

	public void setIsRooted(String isRooted) {
		this.isRooted = isRooted;
	}

	public String getLinkIdCount() {
		return linkIdCount;
	}

	public void setLinkIdCount(String linkIdCount) {
		this.linkIdCount = linkIdCount;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public Integer getCheatsDevice() {
		return cheatsDevice;
	}

	public void setCheatsDevice(Integer cheatsDevice) {
		this.cheatsDevice = cheatsDevice;
	}

	public String getAppCount() {
		return appCount;
	}

	public void setAppCount(String appCount) {
		this.appCount = appCount;
	}
}

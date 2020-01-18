package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   运营商报告--通讯城市分析
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class TripInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//通讯地点
	private String tripLeave;
	//目的地
	private String tripDest;
	//时间窗口
	private String tripType;

	public String getTripLeave() {
		return tripLeave;
	}

	public void setTripLeave(String tripLeave) {
		this.tripLeave = tripLeave;
	}

	public String getTripDest() {
		return tripDest;
	}

	public void setTripDest(String tripDest) {
		this.tripDest = tripDest;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}
}

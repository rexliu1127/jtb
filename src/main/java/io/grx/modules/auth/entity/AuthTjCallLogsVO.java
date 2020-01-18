package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--通话记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthTjCallLogsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//业务类型
	private String month;

	//总条数
	private Integer total_size;

	//通话记录
	private List<AuthTjCallLogVO> authTjCallLogs;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getTotal_size() {
		return total_size;
	}

	public void setTotal_size(Integer total_size) {
		this.total_size = total_size;
	}

	public List<AuthTjCallLogVO> getAuthTjCallLogs() {
		return authTjCallLogs;
	}

	public void setAuthTjCallLogs(List<AuthTjCallLogVO> authTjCallLogs) {
		this.authTjCallLogs = authTjCallLogs;
	}
}

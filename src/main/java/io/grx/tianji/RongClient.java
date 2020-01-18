package io.grx.tianji;

import java.util.Map;

/**
 * 融客户端接口
 * @author 融360-TJ
 *
 */
public interface RongClient {
    
	/**
	 * 顶层的执行方法
	 * @param params 参数
	 * @return String 响应字符串
	 * @throws Exception 异常
	 */
    public String execute(Map<String, String> params) throws Exception;
    
}

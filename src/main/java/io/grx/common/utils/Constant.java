package io.grx.common.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 常量
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

	/** 默认商户号 */
	public static final String DEFAULT_MERCHANT_NO = "00";

	/** Encoding: UTF-8 */
	public static final String ENCODING_UTF8 = "UTF-8";


	public static final String SEPARATOR_COMMA = ",";

	public static final Long CS_ROLE_ID = 5L;

    public static final Long ADMIN_ROLE_ID = 2L;

	public static final Long DATA_PRIVACY_ROLE_ID = 6L;

	public static final List<String> CHANNEL_USER_PERMS = Arrays.asList("opt:channel:order_list", "auth:request:info", "auth:user:list");

    public static final List<String> EXPORT_PERMS = Arrays.asList("auth:request:export", "auth:user:export", "auth:user:info", "auth:request:info");

    public static final List<Long> CHANNEL_MENU_ID_LIST = Arrays.asList(0L, 31L, 51L, 77L, 78L, 79L);

    public static final String PAGE_EXT = ".html";

    public static final String KEY_WITHDRAWAL_FEE_RATE = "WITHDRAWAL_FEE_RATE";

    public static final String KEY_WITHDRAWAL_MIN_FEE = "WITHDRAWAL_MIN_FEE";

    public static final String KEY_WITHDRAWAL_MAX_FEE = "WITHDRAWAL_MAX_FEE";

    public static final String KEY_WITHDRAWAL_MIN_AMOUNT = "WITHDRAWAL_MIN_AMOUNT";

    public static final String COOKIE_MACHINE_ID= "machineId";

    public static final String KEY_CHANNEL = "CHANNEL";

    public static final Long FUN_TYPE_DUOTOU = 1L;   //计费类型  多头:1
    
    /**
     * CPA 单价配置key
     */
    public static final String KEY_FLOW_CPA = "FLOW_CPA";
    /**
     * 流量商户号配置
     */
    public static final String KEY_FLOW_MERCHANT_NO = "FLOW_MERCHANT_NO";
    
    /**
     * 流量并发数
     */
    public static final String KEY_FLOW_CONCURRENT = "FLOW_CONCURRENT";

	/**
	 * 菜单类型
	 * 
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}

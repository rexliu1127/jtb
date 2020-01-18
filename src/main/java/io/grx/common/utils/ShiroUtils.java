package io.grx.common.utils;

import io.grx.modules.opt.entity.ChannelEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grx.common.exception.RRException;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.tx.entity.TxUserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Shiro工具类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月12日 上午9:49:19
 */
public class ShiroUtils {
    protected static final Logger logger = LoggerFactory.getLogger(ShiroUtils.class);

	private static final String KEY_TX_USER = "TX_USER";
    private static final String KEY_AUTH_USER = "AUTH_USER";
	private static final String KEY_MERCHANT_NO = "_MERCHANT_NO";

	private static boolean isInitiated;

	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static SysUserEntity getUserEntity() {
		return (SysUserEntity)SecurityUtils.getSubject().getPrincipal();
	}

	public static Long getUserId() {
		try {
			SysUserEntity userEntity = getUserEntity();
			if (userEntity != null) {
				return userEntity.getUserId();
			}
		} catch (Throwable t) {

		}
		return null;
	}

	public static String getMerchantNo() {
		try {
			SysUserEntity userEntity = getUserEntity();
			if (userEntity != null) {
				return userEntity.getMerchantNo();
			}

			AuthUserEntity authUserEntity = getAuthUser();
			if (authUserEntity != null) {
				return authUserEntity.getMerchantNo();
			}

			ChannelEntity channelEntity = (ChannelEntity) getSessionAttribute(Constant.KEY_CHANNEL);
			if (channelEntity != null) {
				return channelEntity.getMerchantNo();
			}

			String merchantNo = (String) getSessionAttribute(KEY_MERCHANT_NO);
			if (StringUtils.isNotBlank(merchantNo)) {
				return merchantNo;
			}
		} catch (Throwable e) {
			logger.warn("Cannot get merchantNo: {}", e.getMessage());
		}

		return Constant.DEFAULT_MERCHANT_NO;
    }

    public static boolean isSuperAdmin() {
//		SysUserEntity userEntity = getUserEntity();
//		return userEntity != null && StringUtils.equalsIgnoreCase(
//				userEntity.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO);
		return false;
	}

	public static void setMerchantNo(String merchantNo) {
        setSessionAttribute(KEY_MERCHANT_NO, merchantNo);
    }

	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static String getKaptcha(String key) {
		return getKaptcha(key, true);
	}

	public static String getKaptcha(String key, boolean remove) {
		Object kaptcha = getSessionAttribute(key);
		if(kaptcha == null){
			throw new RRException("验证码已失效");
		}
		if (remove) {
			getSession().removeAttribute(key);
		}
		return kaptcha.toString();
	}


	public static void setTxUser(TxUserEntity userEntity) {
		setSessionAttribute(KEY_TX_USER, userEntity);
	}

	public static TxUserEntity getTxUser() {
		return (TxUserEntity) getSessionAttribute(KEY_TX_USER);
	}

	public static void setAuthUser(AuthUserEntity userEntity) {
        setSessionAttribute(KEY_AUTH_USER, userEntity);
	}

    public static AuthUserEntity getAuthUser() {
        return (AuthUserEntity) getSessionAttribute(KEY_AUTH_USER);
    }

    public static ChannelEntity getAuthChannel() {
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		return (ChannelEntity) request.getAttribute(Constant.KEY_CHANNEL);
	}

	public static Long getAuthChannelId() {
		ChannelEntity channelEntity = getAuthChannel();
		if (channelEntity != null) {
			return channelEntity.getChannelId();
		}
		return null;
	}

	public static String getMachineId() {
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		return (String) request.getAttribute(Constant.COOKIE_MACHINE_ID);
	}

    public static boolean isInitiated() {
	    if (!isInitiated) {
            try {
                getSubject();
                isInitiated = true;
            } catch (Throwable t) {
                isInitiated = false;
            }
        }
		return isInitiated;
	}
}

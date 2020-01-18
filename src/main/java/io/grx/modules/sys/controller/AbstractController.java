package io.grx.modules.sys.controller;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import io.grx.common.utils.ShiroUtils;
        import io.grx.modules.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }

    protected Long getUserId() {
        return ShiroUtils.getUserId();
    }

    protected String getMerchantNo() {
        return ShiroUtils.getMerchantNo();
    }

    protected Long getDeptId() {
        return getUser().getDeptId();
    }
}

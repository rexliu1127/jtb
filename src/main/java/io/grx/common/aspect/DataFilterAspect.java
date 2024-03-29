package io.grx.common.aspect;

import java.util.Map;

import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysMerchantService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.grx.common.annotation.DataFilter;
import io.grx.common.exception.RRException;
import io.grx.common.utils.Constant;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysDeptService;

/**
 * 数据过滤，切面处理类
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017/9/17 15:02
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysMerchantService sysMerchantService;

    @Pointcut("@annotation(io.grx.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if(params != null && params instanceof Map){
            SysUserEntity user = ShiroUtils.getUserEntity();

            SysMerchantEntity merchantEntity = sysMerchantService.queryObject(user.getMerchantNo());

            //如果不是超级管理员，则只能查询本部门及子部门数据
            if(user.getUserId() != Constant.SUPER_ADMIN
                    && !user.getUserId().equals(merchantEntity.getAdminUserId())){
                Map map = (Map)params;
                map.put("filterSql", getFilterSQL(user, point));
            }

            return ;
        }

        throw new RRException("要实现数据权限接口的参数，只能是Map类型，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getFilterSQL(SysUserEntity user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        //获取子部门ID
        String subDeptIds = sysDeptService.getSubDeptIdList(user.getDeptId());

        StringBuilder filterSql = new StringBuilder();
        filterSql.append("and (");
        filterSql.append(tableAlias).append("dept_id in(").append(subDeptIds).append(")");

        //没有本部门数据权限，也能查询本人数据
        if(dataFilter.user()){
            String userFieldId = dataFilter.userFieldId();
            if (StringUtils.isBlank(userFieldId)) {
                userFieldId = tableAlias + "user_id";
            }
            filterSql.append(" or ").append(userFieldId).append("=").append(user.getUserId());
        }
        filterSql.append(")");

        return filterSql.toString();
    }
}

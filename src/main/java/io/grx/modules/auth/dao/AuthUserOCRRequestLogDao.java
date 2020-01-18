package io.grx.modules.auth.dao;

import io.grx.modules.auth.entity.AuthUserOCRRequestLogEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

@Mapper
public interface AuthUserOCRRequestLogDao extends BaseDao<AuthUserOCRRequestLogEntity> {

    AuthUserOCRRequestLogEntity queryByOrderId(@Param(value="orderId") String orderId);
}

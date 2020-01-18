package io.grx.modules.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.auth.dto.AuthStatVO;
import io.grx.modules.auth.dto.AuthUserStatVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 认证用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
@Mapper
public interface AuthUserDao extends BaseDao<AuthUserEntity> {

    AuthUserEntity queryByMobile(@Param(value="mobile") String mobile, @Param(value="channelId") Long channelId);

    AuthUserEntity queryByIdNo(@Param(value="idNo") String idNo);

    int queryStatTotal(Map<String, Object> map);

    List<AuthUserStatVO> queryStatList(Map<String, Object> map);

    AuthStatVO queryRequestStat();

    List<AuthUserEntity> queryAllChannelUsersByMobile(String mobile);

    AuthUserEntity queryByMerchantNoAndMobile(@Param(value="mobile") String mobile, @Param(value="merchantNo") String merchantNo);

    List<AuthUserEntity> queryAuthTaskId(@Param(value="taskId") String taskId);

    AuthUserEntity queryAuthByYiXiangReportTaskId(@Param(value="taskId") String taskId);


}

package io.grx.modules.auth.dao;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dto.AuthSummary;
import io.grx.modules.opt.dto.ChannelStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
@Mapper
public interface AuthRequestDao extends BaseDao<AuthRequestEntity> {

    List<AuthRequestEntity> queryByUserAndStatus(@Param(value = "userId") Long userId,
                                                 @Param(value = "statuses") RequestStatus... status);

    AuthRequestEntity queryByUuid(String uuid);

    int queryRequestCount(@Param(value = "userId") Long userId, @Param(value = "requestId") Long requestId);

    List<AuthRequestEntity> queryByVerifyStatus(@Param(value = "statuses") VerifyStatus... status);

    AuthRequestEntity queryLatestByUserId(@Param(value = "userId") Long userId, @Param(value = "channelId") Long
            channelId);

    AuthRequestEntity queryLatestByIdNo(@Param(value = "idNo") String idNo, @Param(value = "channelId") Long
            channelId);

    AuthRequestEntity queryByToken(String token);

    List<AuthRequestEntity> queryPendingVerifyRequests();

    boolean idNoHasRequest(@Param(value = "idNo") String idNo, @Param(value = "inRecentDays") int inRecentDays,
                           @Param(value = "deptIds") Long... deptId);

    AuthSummary querySummary(Map<String, Object> params);

    List<ChannelStatVO> queryChannelStatList(Map<String, Object> params);

    int queryChannelStatTotal(Map<String, Object> params);
    
    Long getLastAuditUserId(@Param("channelId")Long channelId,@Param("merchantNo")String merchantNo);
    
    List<AuthRequestEntity> queryFlowCopyList(@Param("merchantNo")String merchantNo,@Param("status")Integer status,@Param("startTime")String startTime);
    
    List<AuthRequestEntity> queryFlowChangeList(@Param("merchantNo")String merchantNo,@Param("createTime")String createTime);

	int queryRejectCountByIdNo(@Param("merchantNo")String merchantNo,@Param("idNo") String idNo);
}

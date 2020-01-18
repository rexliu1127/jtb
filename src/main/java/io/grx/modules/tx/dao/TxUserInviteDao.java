package io.grx.modules.tx.dao;

import io.grx.modules.tx.dto.TxUserInviteVO;
import io.grx.modules.tx.entity.TxUserInviteEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户邀请记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 14:34:31
 */
@Mapper
public interface TxUserInviteDao extends BaseDao<TxUserInviteEntity> {

    List<TxUserInviteVO> queryUserInviteList(@Param(value = "userId") Long userId,
                                             @Param(value = "level") Integer level);

    TxUserInviteEntity queryByUseId(Long userId);

    int getTeamUserCount(Long userId);
}

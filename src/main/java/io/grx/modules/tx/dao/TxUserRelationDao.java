package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.tx.entity.TxUserRelationEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 交易用户关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 22:44:07
 */
@Mapper
public interface TxUserRelationDao extends BaseDao<TxUserRelationEntity> {

    int hasRelation(@Param(value = "userId") Long userId, @Param(value = "friendUserId") Long friendUserId);

    int getFriendTotal(Long userId);
}

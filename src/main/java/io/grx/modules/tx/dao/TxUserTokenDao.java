package io.grx.modules.tx.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxUserTokenEntity;

/**
 * 借条用户Token
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-27 22:13:13
 */
@Mapper
public interface TxUserTokenDao extends BaseDao<TxUserTokenEntity> {

    TxUserTokenEntity queryByToken(String token);

    TxUserTokenEntity queryByUser(Long userId);
}

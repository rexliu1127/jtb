package io.grx.modules.tx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxExtensionEntity;

/**
 * 展期记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
@Mapper
public interface TxExtensionDao extends BaseDao<TxExtensionEntity> {

    TxExtensionEntity getLastExtensionByTx(long txId);


    List<TxExtensionEntity> getExtensionsByTx(long txId);
}

package io.grx.modules.tx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxComplainResultEntity;

/**
 * 借条申诉处理结果
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
@Mapper
public interface TxComplainResultDao extends BaseDao<TxComplainResultEntity> {

    List<TxComplainResultEntity> queryByComplainId(Long complainId);
}

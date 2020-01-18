package io.grx.modules.tx.dao;

import io.grx.modules.tx.dto.TxUserWithdrawalVO;
import io.grx.modules.tx.entity.TxUserWithdrawEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户提现记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-29 18:10:26
 */
@Mapper
public interface TxUserWithdrawDao extends BaseDao<TxUserWithdrawEntity> {

    double sumUserWithdrawal(Long userId);

    List<TxUserWithdrawalVO> queryAdminList(Map<String, Object> params);
}

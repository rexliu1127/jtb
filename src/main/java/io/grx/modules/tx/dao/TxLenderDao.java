package io.grx.modules.tx.dao;

import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 出借人
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 14:48:57
 */
@Mapper
public interface TxLenderDao extends BaseDao<TxLenderEntity> {

    TxLenderEntity queryByMobile(String mobile);

    List<String> getAllLenderMobiles();

    List<TxLenderEntity>  queryListByMerchantNo(Map<String, Object> map);
}

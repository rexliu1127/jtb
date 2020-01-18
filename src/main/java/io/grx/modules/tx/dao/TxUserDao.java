package io.grx.modules.tx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import io.grx.modules.sys.dao.BaseDao;
import io.grx.modules.tx.entity.TxUserEntity;

/**
 * 系统用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 00:49:35
 */
@Mapper
public interface TxUserDao extends BaseDao<TxUserEntity> {

    TxUserEntity queryByWechatId(@Param(value = "wechatId") String wechatId);
    TxUserEntity queryByMobile(@Param(value = "mobile") String mobile);

    TxUserEntity queryByUnionId(@Param(value = "unionId") String unionId);

    List<TxUserEntity> getFriendList(@Param(value = "userId") Long userId,
                                     @Param(value = "userName") String userName);

    List<TxUserEntity> queryByIdNo(@Param(value = "idNo") String idNo);

    List<TxUserEntity> queryNonEmptyNameList(Map<String, Object> map);

    List<TxUserEntity> getUserListByMerchantNo(Map<String, Object> map);

    void updateNamePinyin(TxUserEntity userEntity);

}

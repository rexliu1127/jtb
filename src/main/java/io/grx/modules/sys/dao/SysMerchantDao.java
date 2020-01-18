package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 商家
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-06-09 23:41:29
 */
@Mapper
public interface SysMerchantDao extends BaseDao<SysMerchantEntity> {

    public List<SysMerchantEntity> queryValidList(Map<String, Object> map);

    public List<SysMerchantEntity> queryValidListByFunType(Map<String, Object> map);

}

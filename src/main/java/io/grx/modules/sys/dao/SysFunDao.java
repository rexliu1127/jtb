package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 *  账户总览
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:35
 */
@Mapper
public interface SysFunDao extends BaseDao<SysFunEntity> {

    SysFunEntity queryinfo(String merchant_no);

    //修改商户为风险商户
    int updateRistBatch(Object[] id);
	
}

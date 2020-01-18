package io.grx.modules.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.auth.entity.AuthMissingRecordEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 丢失记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-13 01:25:30
 */
@Mapper
public interface AuthMissingRecordDao extends BaseDao<AuthMissingRecordEntity> {

    List<AuthMissingRecordEntity> queryMissingRecords();
}

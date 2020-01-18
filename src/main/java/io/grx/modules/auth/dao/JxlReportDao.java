package io.grx.modules.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.auth.entity.JxlReportEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 用户认证报告(聚信立)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-31 21:54:19
 */
@Mapper
public interface JxlReportDao extends BaseDao<JxlReportEntity> {

    JxlReportEntity queryByToken(String token);
}

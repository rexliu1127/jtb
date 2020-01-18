package io.grx.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.sys.entity.SysUserChannelEntity;

/**
 * 渠道用户与渠道对应关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-16 23:57:03
 */
@Mapper
public interface SysUserChannelDao extends BaseDao<SysUserChannelEntity> {

    List<Long> queryUserChannelIdList(Long userId);
}

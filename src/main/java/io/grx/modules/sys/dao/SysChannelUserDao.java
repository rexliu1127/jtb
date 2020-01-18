package io.grx.modules.sys.dao;

import io.grx.modules.sys.entity.SysChannelUserEntity;
import io.grx.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 渠道与审核人员对应关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 17:58:41
 */
@Mapper
public interface SysChannelUserDao extends BaseDao<SysChannelUserEntity> {
    List<Long> queryChannelUserIdList(Long channelId);
    List<SysChannelUserEntity> queryChannelUserNameIdList(Long channelId);
}

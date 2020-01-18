package io.grx.modules.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grx.modules.sys.dao.SysUserRoleDao;
import io.grx.modules.sys.service.*;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.annotation.DataFilter;
import io.grx.common.utils.Constant;
import io.grx.modules.sys.dao.SysUserDao;
import io.grx.modules.sys.entity.SysUserEntity;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserChannelService sysUserChannelService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return sysUserDao.queryByUserName(username);
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        return sysUserDao.queryObject(userId);
    }

    @Override
    @DataFilter(tableAlias = "u", user = false)
    public List<SysUserEntity> queryList(Map<String, Object> map){
        return sysUserDao.queryList(map);
    }

    @Override
    @DataFilter(tableAlias = "u", user = false)
    public int queryTotal(Map<String, Object> map) {
        return sysUserDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        sysUserDao.save(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

        if (user.isChannelUser()) {
            // 保存渠道关系
            sysUserChannelService.saveOrUpdate(user.getUserId(), user.getChannelIdList());
        }
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }else{
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        sysUserDao.update(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

        if (user.isChannelUser()) {
            // 保存渠道关系
            sysUserChannelService.saveOrUpdate(user.getUserId(), user.getChannelIdList());
        }
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        sysUserRoleDao.deleteBatch(userId);;
        sysUserDao.deleteBatch(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return sysUserDao.updatePassword(map);
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user){
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if(user.getCreateUserId() == Constant.SUPER_ADMIN){
            return ;
        }

        //查询用户创建的角色列表
//		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());
//
//		//判断是否越权
//		if(!roleIdList.containsAll(user.getRoleIdList())){
//			throw new RRException("新增用户所选角色，不是本人创建");
//		}
    }


    /**
     * 根据用户名，查询系统用户
     *
     * @param email
     */
    @Override
    public SysUserEntity queryByEmail(final String email) {
        return sysUserDao.queryByEmail(email);
    }

    @Override
    public List<SysUserEntity> queryFreeAuditorList(Long deptId) {
        return sysUserDao.queryFreeAuditorList();
    }

    @Override
    public boolean isDeptHasUser(Long deptId) {
        return sysUserDao.isDeptHasUser(deptId);
    }

    @Override
    public List<SysUserEntity> querySpecifiedFreeAuditorList(Long channelId) {
        return sysUserDao.querySpecifiedFreeAuditorList(channelId);
    }
}

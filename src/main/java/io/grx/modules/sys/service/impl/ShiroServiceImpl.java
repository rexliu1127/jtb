package io.grx.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.grx.common.service.SecurityService;
import io.grx.common.utils.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.common.utils.Constant;
import io.grx.modules.sys.dao.SysMenuDao;
import io.grx.modules.sys.dao.SysUserDao;
import io.grx.modules.sys.dao.SysUserTokenDao;
import io.grx.modules.sys.entity.SysMenuEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.entity.SysUserTokenEntity;
import io.grx.modules.sys.service.ShiroService;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private SecurityService securityService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            SysUserEntity userEntity = sysUserDao.queryObject(userId);
            if (userEntity.isChannelUser()) {
                permsList = Constant.CHANNEL_USER_PERMS;
            } else {
                permsList = sysUserDao.queryAllPerms(userId);
            }
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        if (StringUtils.equalsIgnoreCase(ShiroUtils.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)
                && securityService.toMaskKeyInfo()) {
            permsSet.removeAll(Constant.EXPORT_PERMS);
        }
        return permsSet;
    }

    @Override
    public SysUserTokenEntity queryByToken(String token) {
        return sysUserTokenDao.queryByToken(token);
    }

    @Override
    public SysUserEntity queryUser(Long userId) {
        return sysUserDao.queryObject(userId);
    }
}

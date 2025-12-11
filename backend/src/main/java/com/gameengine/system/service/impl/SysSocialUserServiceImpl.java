package com.gameengine.system.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gameengine.system.domain.SysSocialUser;
import com.gameengine.system.mapper.SysSocialUserMapper;
import com.gameengine.system.service.ISysSocialUserService;

/**
 * 第三方登录绑定服务实现
 */
@Service
public class SysSocialUserServiceImpl implements ISysSocialUserService {

    @Autowired
    private SysSocialUserMapper socialUserMapper;

    @Override
    public SysSocialUser findBySourceAndUuid(String source, String uuid) {
        if (StringUtils.isAnyBlank(source, uuid)) {
            return null;
        }
        return socialUserMapper.selectOne(
            new LambdaQueryWrapper<SysSocialUser>()
                .eq(SysSocialUser::getSource, source)
                .eq(SysSocialUser::getUuid, uuid)
        );
    }

    @Override
    public SysSocialUser findBySourceAndUnionId(String source, String unionId) {
        if (StringUtils.isAnyBlank(source, unionId)) {
            return null;
        }
        return socialUserMapper.selectOne(
            new LambdaQueryWrapper<SysSocialUser>()
                .eq(SysSocialUser::getSource, source)
                .eq(SysSocialUser::getUnionId, unionId)
        );
    }

    @Override
    public SysSocialUser saveOrUpdate(SysSocialUser socialUser) {
        Date now = new Date();
        socialUser.setUpdateTime(now);
        if (socialUser.getId() == null) {
            socialUser.setCreateTime(now);
            socialUserMapper.insert(socialUser);
        } else {
            socialUserMapper.updateById(socialUser);
        }
        return socialUser;
    }
}



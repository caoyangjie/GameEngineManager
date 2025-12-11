package com.gameengine.system.service;

import com.gameengine.system.domain.SysSocialUser;

/**
 * 第三方登录绑定服务
 */
public interface ISysSocialUserService {

    SysSocialUser findBySourceAndUuid(String source, String uuid);

    SysSocialUser findBySourceAndUnionId(String source, String unionId);

    SysSocialUser saveOrUpdate(SysSocialUser socialUser);
}



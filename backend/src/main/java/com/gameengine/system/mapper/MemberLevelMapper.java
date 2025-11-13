package com.gameengine.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.MemberLevel;

/**
 * 用户等级 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface MemberLevelMapper extends BaseMapper<MemberLevel> {
    
    /**
     * 查询所有启用的等级列表，按排序升序
     * 
     * @return 等级列表
     */
    List<MemberLevel> selectEnabledLevels();
    
    /**
     * 根据等级代码查询等级
     * 
     * @param levelCode 等级代码
     * @return 等级对象
     */
    MemberLevel selectByLevelCode(String levelCode);
}


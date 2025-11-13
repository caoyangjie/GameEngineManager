package com.gameengine.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameengine.system.domain.CurrencyType;

/**
 * 虚拟货币类型 数据层
 * 
 * @author GameEngine
 */
@Mapper
public interface CurrencyTypeMapper extends BaseMapper<CurrencyType> {
    
    /**
     * 查询所有启用的可交易货币
     * 
     * @return 货币列表
     */
    List<CurrencyType> selectTradableCurrencies();
    
    /**
     * 根据货币代码查询货币
     * 
     * @param currencyCode 货币代码
     * @return 货币对象
     */
    CurrencyType selectByCurrencyCode(String currencyCode);
}


package com.gameengine.system.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.system.domain.BusinessModelCanvas;

/**
 * 商业模式画布 业务层
 * 
 * @author GameEngine
 */
public interface IBusinessModelCanvasService {
    
    /**
     * 查询商业模式画布列表（分页）
     * 
     * @param page 分页对象
     * @param canvas 查询条件
     * @return 画布列表
     */
    IPage<BusinessModelCanvas> selectCanvasList(Page<BusinessModelCanvas> page, BusinessModelCanvas canvas);
    
    /**
     * 查询商业模式画布列表（不分页）
     * 
     * @param canvas 查询条件
     * @return 画布列表
     */
    List<BusinessModelCanvas> selectCanvasList(BusinessModelCanvas canvas);
    
    /**
     * 根据ID查询商业模式画布
     * 
     * @param canvasId 画布ID
     * @return 画布对象
     */
    BusinessModelCanvas selectCanvasById(Long canvasId);
    
    /**
     * 新增商业模式画布
     * 
     * @param canvas 画布对象
     * @return 结果
     */
    int insertCanvas(BusinessModelCanvas canvas);
    
    /**
     * 修改商业模式画布
     * 
     * @param canvas 画布对象
     * @return 结果
     */
    int updateCanvas(BusinessModelCanvas canvas);
    
    /**
     * 批量删除商业模式画布
     * 
     * @param canvasIds 需要删除的画布ID数组
     * @return 结果
     */
    int deleteCanvasByIds(Long[] canvasIds);
    
    /**
     * 删除商业模式画布信息
     * 
     * @param canvasId 画布ID
     * @return 结果
     */
    int deleteCanvasById(Long canvasId);
}


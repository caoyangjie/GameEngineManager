package com.gameengine.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameengine.common.utils.StringUtils;
import com.gameengine.system.domain.BusinessModelCanvas;
import com.gameengine.system.mapper.BusinessModelCanvasMapper;
import com.gameengine.system.service.IBusinessModelCanvasService;

/**
 * 商业模式画布 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class BusinessModelCanvasServiceImpl implements IBusinessModelCanvasService {
    
    @Autowired
    private BusinessModelCanvasMapper canvasMapper;
    
    @Override
    public IPage<BusinessModelCanvas> selectCanvasList(Page<BusinessModelCanvas> page, BusinessModelCanvas canvas) {
        LambdaQueryWrapper<BusinessModelCanvas> wrapper = buildQueryWrapper(canvas);
        return canvasMapper.selectPage(page, wrapper);
    }
    
    @Override
    public List<BusinessModelCanvas> selectCanvasList(BusinessModelCanvas canvas) {
        LambdaQueryWrapper<BusinessModelCanvas> wrapper = buildQueryWrapper(canvas);
        return canvasMapper.selectList(wrapper);
    }
    
    @Override
    public BusinessModelCanvas selectCanvasById(Long canvasId) {
        return canvasMapper.selectById(canvasId);
    }
    
    @Override
    public int insertCanvas(BusinessModelCanvas canvas) {
        Date now = new Date();
        if (canvas.getCreateTime() == null) {
            canvas.setCreateTime(now);
        }
        if (canvas.getUpdateTime() == null) {
            canvas.setUpdateTime(now);
        }
        return canvasMapper.insert(canvas);
    }
    
    @Override
    public int updateCanvas(BusinessModelCanvas canvas) {
        canvas.setUpdateTime(new Date());
        return canvasMapper.updateById(canvas);
    }
    
    @Override
    public int deleteCanvasByIds(Long[] canvasIds) {
        return canvasMapper.deleteBatchIds(java.util.Arrays.asList(canvasIds));
    }
    
    @Override
    public int deleteCanvasById(Long canvasId) {
        return canvasMapper.deleteById(canvasId);
    }
    
    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<BusinessModelCanvas> buildQueryWrapper(BusinessModelCanvas canvas) {
        LambdaQueryWrapper<BusinessModelCanvas> wrapper = new LambdaQueryWrapper<>();
        
        if (canvas != null) {
            // 标题模糊查询
            if (StringUtils.isNotEmpty(canvas.getTitle())) {
                wrapper.like(BusinessModelCanvas::getTitle, canvas.getTitle());
            }
            
            // 状态查询
            if (StringUtils.isNotEmpty(canvas.getStatus())) {
                wrapper.eq(BusinessModelCanvas::getStatus, canvas.getStatus());
            }
            
            // 搜索值（用于通用搜索）
            if (StringUtils.isNotEmpty(canvas.getSearchValue())) {
                wrapper.and(w -> w.like(BusinessModelCanvas::getTitle, canvas.getSearchValue())
                    .or().like(BusinessModelCanvas::getKeyPartners, canvas.getSearchValue())
                    .or().like(BusinessModelCanvas::getValuePropositions, canvas.getSearchValue()));
            }
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(BusinessModelCanvas::getCreateTime);
        
        return wrapper;
    }
}


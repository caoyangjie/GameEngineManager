package com.gameengine.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 商业模式画布对象 business_model_canvas
 * 
 * @author GameEngine
 */
@TableName("business_model_canvas")
public class BusinessModelCanvas extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 画布ID */
    @TableId(type = IdType.AUTO)
    private Long canvasId;
    
    /** 画布标题 */
    private String title;
    
    /** 版本 */
    private String version;
    
    /** 关键合作 */
    private String keyPartners;
    
    /** 关键活动 */
    private String keyActivities;
    
    /** 关键资源 */
    private String keyResources;
    
    /** 价值主张 */
    private String valuePropositions;
    
    /** 客户关系 */
    private String customerRelationships;
    
    /** 渠道通路 */
    private String channels;
    
    /** 客户细分 */
    private String customerSegments;
    
    /** 成本构成 */
    private String costStructure;
    
    /** 售卖途径 */
    private String revenueStreams;
    
    /** 状态（0正常 1停用） */
    private String status;
    
    public Long getCanvasId() {
        return canvasId;
    }
    
    public void setCanvasId(Long canvasId) {
        this.canvasId = canvasId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getKeyPartners() {
        return keyPartners;
    }
    
    public void setKeyPartners(String keyPartners) {
        this.keyPartners = keyPartners;
    }
    
    public String getKeyActivities() {
        return keyActivities;
    }
    
    public void setKeyActivities(String keyActivities) {
        this.keyActivities = keyActivities;
    }
    
    public String getKeyResources() {
        return keyResources;
    }
    
    public void setKeyResources(String keyResources) {
        this.keyResources = keyResources;
    }
    
    public String getValuePropositions() {
        return valuePropositions;
    }
    
    public void setValuePropositions(String valuePropositions) {
        this.valuePropositions = valuePropositions;
    }
    
    public String getCustomerRelationships() {
        return customerRelationships;
    }
    
    public void setCustomerRelationships(String customerRelationships) {
        this.customerRelationships = customerRelationships;
    }
    
    public String getChannels() {
        return channels;
    }
    
    public void setChannels(String channels) {
        this.channels = channels;
    }
    
    public String getCustomerSegments() {
        return customerSegments;
    }
    
    public void setCustomerSegments(String customerSegments) {
        this.customerSegments = customerSegments;
    }
    
    public String getCostStructure() {
        return costStructure;
    }
    
    public void setCostStructure(String costStructure) {
        this.costStructure = costStructure;
    }
    
    public String getRevenueStreams() {
        return revenueStreams;
    }
    
    public void setRevenueStreams(String revenueStreams) {
        this.revenueStreams = revenueStreams;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}


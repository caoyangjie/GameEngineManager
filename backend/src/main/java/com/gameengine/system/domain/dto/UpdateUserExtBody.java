package com.gameengine.system.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新用户扩展信息对象
 * 
 * @author GameEngine
 */
@Schema(description = "更新用户扩展信息请求")
public class UpdateUserExtBody {
    
    /**
     * 招聘链接
     */
    @Schema(description = "招聘链接", example = "https://voyagewest.game/register?refid=11898525444")
    private String recruitmentLink;
    
    /**
     * 当前旅程等级
     */
    @Schema(description = "当前旅程等级", example = "银")
    private String currentLevel;
    
    /**
     * 玩家ID
     */
    @Schema(description = "玩家ID", example = "11898525444")
    private String playerId;
    
    /**
     * BEP20地址
     */
    @Schema(description = "BEP20地址", example = "0x1234567890abcdef")
    private String bep20Address;
    
    public String getRecruitmentLink() {
        return recruitmentLink;
    }
    
    public void setRecruitmentLink(String recruitmentLink) {
        this.recruitmentLink = recruitmentLink;
    }
    
    public String getCurrentLevel() {
        return currentLevel;
    }
    
    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    public String getBep20Address() {
        return bep20Address;
    }
    
    public void setBep20Address(String bep20Address) {
        this.bep20Address = bep20Address;
    }
}


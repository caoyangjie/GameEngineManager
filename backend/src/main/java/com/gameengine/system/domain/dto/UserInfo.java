package com.gameengine.system.domain.dto;

import java.util.List;
import java.util.Set;

import com.gameengine.system.domain.SysUser;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户信息对象
 * 
 * @author GameEngine
 */
@Schema(description = "用户信息")
public class UserInfo {
    
    /**
     * 用户信息
     */
    @Schema(description = "用户基本信息")
    private SysUser user;
    
    /**
     * 角色集合
     */
    @Schema(description = "用户角色列表", example = "[\"admin\"]")
    private List<String> roles;
    
    /**
     * 权限集合
     */
    @Schema(description = "用户权限列表", example = "[\"system:user:list\", \"system:user:add\"]")
    private Set<String> permissions;
    
    public SysUser getUser() {
        return user;
    }
    
    public void setUser(SysUser user) {
        this.user = user;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}


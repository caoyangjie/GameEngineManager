package com.gameengine.system.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameengine.common.core.domain.BaseEntity;

/**
 * 用户对象 sys_user
 * 
 * @author GameEngine
 */
@TableName("sys_user")
public class SysUser extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    @TableId(type = IdType.AUTO)
    private Long userId;
    
    /** 用户账号 */
    private String userName;
    
    /** 用户昵称 */
    private String nickName;
    
    /** 用户分类（player:玩家 recruiter:招聘者 admin:管理员） */
    private String userCategory;
    
    /** 用户邮箱 */
    private String email;
    
    /** 手机号码 */
    private String phonenumber;
    
    /** 用户性别（0男 1女 2未知） */
    private String sex;
    
    /** 头像地址 */
    private String avatar;
    
    /** 密码 */
    private String password;
    
    /** 帐号状态（0正常 1停用） */
    private String status;
    
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    
    /** 最后登录IP */
    private String loginIp;
    
    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getUserCategory() {
        return userCategory;
    }
    
    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhonenumber() {
        return phonenumber;
    }
    
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    public String getLoginIp() {
        return loginIp;
    }
    
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    
    public Date getLoginDate() {
        return loginDate;
    }
    
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}


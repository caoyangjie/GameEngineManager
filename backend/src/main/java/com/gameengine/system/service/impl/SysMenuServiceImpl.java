package com.gameengine.system.service.impl;

import com.gameengine.system.domain.SysMenu;
import com.gameengine.system.mapper.SysMenuMapper;
import com.gameengine.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单 业务层处理
 * 
 * @author GameEngine
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    
    @Autowired
    private SysMenuMapper menuMapper;
    
    /**
     * 根据用户ID查询菜单
     * 
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = menuMapper.selectMenuTreeByUserId(userId);
        return getChildPerms(menus, 0L);
    }
    
    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, Long parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();) {
            SysMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() != null && t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }
    
    /**
     * 递归列表
     * 
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }
    
    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = it.next();
            if (n.getParentId() != null && n.getParentId().equals(t.getMenuId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }
    
    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
    
    /**
     * 构建前端路由所需要的菜单
     * 
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<SysMenu> buildMenus(List<SysMenu> menus) {
        List<SysMenu> menuList = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                menuList.add(menu);
            }
        }
        return menuList;
    }
}


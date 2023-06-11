package com.example.commonutils.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.commonutils.entity.Role;
import com.example.commonutils.entity.RoleMenu;
import com.example.commonutils.mapper.RoleMapper;
import com.example.commonutils.mapper.RoleMenuMapper;
import com.example.commonutils.service.RoleMenuService;
import com.example.commonutils.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gqq
 * @since 2023-04-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMenuMapper roleMenuMapper;
    @Resource
    RoleMapper roleMapper;

    @Override
    @Transactional
    public Boolean addRole(Role role) {
        //写入角色表
        this.baseMapper.insert(role);
        //写入角色菜单关系表
        if(null != role.getMenuIdList()){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null , role.getRoleId() , menuId ));

            }
            return true;
        }
        return false;
    }

    //@Override
    //public Role getRoleById(Integer roleId) {
    //    Role role = this.baseMapper.selectByRoleId(roleId);
    //    List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(roleId);
    //    role.setMenuIdList(menuIdList);
    //    return role;
    //}
    @Override
    public Role getRoleById(String roleId) {
        Role role = this.baseMapper.selectByRoleId(roleId);
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(roleId);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public boolean updateRoleById(Role role) {
        //修改角色表
        this.baseMapper.updateById(role);
        //删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId , role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增权限
        if(role.getMenuIdList() != null){
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null , role.getRoleId() , menuId));
            }
            return  true;
        }
        return true;
    }

    @Override
    public List<Role> getRoleList(Role role) {
        return roleMapper.getRoleList(role);
    }

    @Override
    @Transactional
    public Boolean deleteRoleById(String roleId) {
        this.baseMapper.deleteById(roleId);
        //删除角色菜单表的权限管理
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId , roleId);
        roleMenuMapper.delete(wrapper);
        return true;
    }

    @Override
    public Boolean deleteRoleByIds(Long[] roleIds) {
        roleMapper.deleteRoleByIds(roleIds);
        roleMenuMapper.deleteRoleMenuByIds(roleIds);
        return true;
    }

    @Override
    public List<Role> getAllRoleList() {
        List<Role> roleList = roleMapper.getAllRoleList();
        return roleList;
    }

    @Override
    public Role getUserAndRoleById(String roleId) {
        return roleMapper.getUserAndRoleById(roleId);
    }


}

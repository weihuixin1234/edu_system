package com.example.commonutils.service;

import com.example.commonutils.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-04-03
 */
public interface RoleService extends IService<Role> {

    Boolean addRole(Role role);

    //Role getRoleById(Integer roleId);
    Role getRoleById(String roleId);

    boolean updateRoleById(Role role);


    List<Role> getRoleList(Role role);

    Boolean deleteRoleById(String roleId);

    Boolean deleteRoleByIds(Long[] roleIds);

    List<Role> getAllRoleList();

    Role getUserAndRoleById(String roleId);
}

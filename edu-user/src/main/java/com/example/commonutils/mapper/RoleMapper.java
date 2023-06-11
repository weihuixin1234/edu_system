package com.example.commonutils.mapper;

import com.example.commonutils.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-04-03
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRoleList(Role role);

    Role selectByRoleId(@Param("roleId") String roleId);

    Boolean deleteRoleByIds(Long[] roleIds);

    List<Role> getAllRoleList();

    Role getUserAndRoleById(String roleId);
}

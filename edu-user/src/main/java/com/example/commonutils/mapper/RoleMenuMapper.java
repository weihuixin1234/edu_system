package com.example.commonutils.mapper;

import com.example.commonutils.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-04-04
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<Integer> getMenuIdListByRoleId(String roleId);

    Boolean deleteRoleMenuByIds(Long[] roleIds);
}

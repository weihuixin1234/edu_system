package com.example.commonutils.mapper;

import com.example.commonutils.entity.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {
   List<Menu> getMenuListByUserId(@Param("userId") Integer userId , @Param("pid") Integer pid );
}

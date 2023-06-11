package com.example.commonutils.service;

import com.example.commonutils.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-04-04
 */
public interface MenuService extends IService<Menu> {

    List<Menu> getAllMenu();

    List<Menu> getAllMenuByUserId(Integer userId);
}

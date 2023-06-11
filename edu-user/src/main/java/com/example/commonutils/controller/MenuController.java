package com.example.commonutils.controller;


import com.example.commonutils.entity.Menu;
import com.example.commonutils.entity.R;
import com.example.commonutils.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gqq
 * @since 2023-04-04
 */
@RestController
@CrossOrigin
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @GetMapping("/list")
    public R getAllMenu(){
        List<Menu> menuList = menuService.getAllMenu();
        return R.ok().data("list" , menuList);
    }
}


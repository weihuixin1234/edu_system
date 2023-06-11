package com.example.commonutils.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonutils.common.annotation.Log;
import com.example.commonutils.entity.R;
import com.example.commonutils.entity.Role;
import com.example.commonutils.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gqq
 * @since 2023-04-03
 */
@RestController
@CrossOrigin
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    RoleService roleService;

    //测试日志文件输出
    @Log(modul = "角色表模块", type = "测试类型" ,desc = "测试说明")
    @GetMapping("/testLog")
    public R testLog(){
        log.info("测试日志功能");
        return R.ok();
    }

    //@Log(modul = "角色模块"  , type = "SELECT" , desc = "查询所有角色列表")
    @PostMapping("/list/{pageNum}/{pageSize}")
    public R getRoleList(@PathVariable Integer pageNum , @PathVariable Integer pageSize , @RequestBody(required = false) Role role){
        PageHelper.startPage(pageNum , pageSize);
        List<Role> roleList = roleService.getRoleList(role);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleList);
        return R.ok().data("result" , rolePageInfo);
    }


    @GetMapping("/get")
    public R getById(@RequestParam String roleId){
        //Role role = roleService.getRoleById(Integer.parseInt(roleId));
        Role role = roleService.getRoleById(roleId);
        return R.ok().data("role" , role);
    }

    @Log(modul = "角色模块"  , type = "ADD" , desc = "添加角色")
    @PostMapping("/add")
    public R addRole(@RequestBody Role role){
        Boolean save = roleService.addRole(role);
        if(save){
            return R.ok().data("message" , "新增成功");
        }
        return R.error();
    }

    @Log(modul = "角色模块"  , type = "UPDATE" , desc = "修改角色")
    @PutMapping("/update")
    public R updateRole(@RequestBody Role role){
        boolean b = roleService.updateRoleById(role);
        if(b){
            return R.ok().data("message" , "更新成功");
        }
        return R.error();
    }

    @DeleteMapping("/delete")
    public R deleteRoleById(@RequestParam(value = "roleId") String roleId){
        roleService.deleteRoleById(roleId);
        return R.ok().data("message" , "删除成功");
    }

    @Log(modul = "角色模块"  , type = "DELETE" , desc = "删除角色")
    @DeleteMapping("/delete/{roleIds}")
    public R remove(@PathVariable Long[] roleIds){
        roleService.deleteRoleByIds(roleIds);
        return R.ok().data("message" , "批量删除成功");
    }

}


package com.example.commonutils.controller;


import com.example.commonutils.entity.LogInfo;
import com.example.commonutils.entity.R;
import com.example.commonutils.service.LogInfoService;
import com.example.commonutils.utils.core.BaseController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gqq
 * @since 2023-04-08
 */
@RestController
@RequestMapping("/log")
public class LogInfoController extends BaseController {
    @Autowired
    LogInfoService logInfoService;

    @PostMapping("/list/{pageNum}/{pageSize}")
    public R getLogList(@PathVariable Integer pageNum , @PathVariable Integer pageSize ,  @RequestBody(required = false) LogInfo logInfo){
        PageHelper.startPage(pageNum , pageSize);
        List<LogInfo> list = logInfoService.selectLogList(logInfo);
        PageInfo<LogInfo> pageInfo = new PageInfo<>(list);
        return R.ok().data("result" , pageInfo);

    }
}


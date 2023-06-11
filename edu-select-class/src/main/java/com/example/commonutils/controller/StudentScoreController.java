package com.example.commonutils.controller;


import com.example.commonutils.entity.Course;
import com.example.commonutils.entity.R;
import com.example.commonutils.entity.ScoreTable;
import com.example.commonutils.entity.StudentScore;
import com.example.commonutils.service.StudentScoreService;
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
 * @since 2023-05-23
 */
@RestController
@CrossOrigin
@RequestMapping("/student-score")
public class StudentScoreController {

    @Autowired
    StudentScoreService studentScoreService;

    @PostMapping("/list/{pageNum}/{pageSize}")
    public R getStuScoreList(@PathVariable Integer pageNum , @PathVariable Integer pageSize ,
                             @RequestBody(required = false) StudentScore studentScore){
        PageHelper.startPage(pageNum , pageSize);
        List<ScoreTable> stuScoreList = studentScoreService.getStuScoreList(studentScore);
        PageInfo<ScoreTable> stuScoreInfo = new PageInfo<>(stuScoreList);
        return R.ok().data("result" ,stuScoreInfo);
    }
}


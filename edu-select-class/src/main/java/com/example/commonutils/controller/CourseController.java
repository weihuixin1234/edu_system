package com.example.commonutils.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.example.commonutils.common.annotation.Log;
import com.example.commonutils.entity.*;

import com.example.commonutils.mapper.ClassTableMapper;
import com.example.commonutils.mapper.CourseStudentMapper;
import com.example.commonutils.service.ClassTableService;
import com.example.commonutils.service.CourseService;
import com.example.commonutils.utils.CodeUtil;
import com.example.commonutils.utils.JwtTokenUtil;
import com.example.commonutils.utils.RedisUtils;
import com.example.commonutils.utils.StringUtils;
import com.example.commonutils.utils.core.BaseController;
import com.example.commonutils.utils.poi.ExcelUtil;
import com.example.commonutils.utils.sign.Base64;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gqq
 * @since 2023-03-18
 */
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseController extends BaseController {

    @Autowired
    CourseService courseService;
    @Resource
    CourseStudentMapper courseStudentMapper;

    @Autowired
    ClassTableService classTableService;

    @PostMapping("/list/{pageNum}/{pageSize}/{userId}")
    public R getList(@PathVariable Integer pageNum , @PathVariable Integer pageSize ,@PathVariable(value = "userId" , required = false) String userId, @RequestBody(required = false) Course course){
        PageHelper.startPage(pageNum , pageSize);
        List<Course> list = courseService.getCourseList(course);
        PageInfo<Course> userPageInfo = new PageInfo<>(list);
        if(StringUtils.isNotNull(userId)){
            List<String> beforeCourseId = courseStudentMapper.selectClass(userId);
            return R.ok().data("result" , userPageInfo).data("beforeCourseId" , beforeCourseId);
        }
        System.out.println();
        return R.ok().data("result" ,userPageInfo);
    }


    @GetMapping(value = {"/class" , "/class/{courseId}"})
    public R getCourseAndRole(@PathVariable(value = "courseId" , required = false) Long courseId){
        Course course = new Course();
        if(StringUtils.isNotNull(courseId)){
            course = courseService.getById(courseId);
        }
        return R.ok().data("course" , course);
    }

    //@Log(modul = "用户模块" , type = "ADD" , desc = "添加用户")
    @PostMapping("/add")
    public R addCourse(@Validated @RequestBody Course course){
        String courseId = course.getCourseId();
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id" , courseId);
        List<Course> list = courseService.list(wrapper);
        if(list.size() != 0){
            return R.error().data("message" , courseId+ "登录账号已存在");
        }
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //String password = course.getPwd();
        //user.setPwd(passwordEncoder.encode(password));
        courseService.save(course);
        return R.ok().data("message" , "新增成功");
    }

    //@Log(modul = "用户模块" , type = "UPDATE" , desc = "修改用户")
    @PutMapping("/update")
    public R updateCourse(@Validated @RequestBody Course course){
        //String username = user.getName();
        //QueryWrapper<User> wrapper = new QueryWrapper<>();
        //wrapper.eq("name" , username);
        //List<User> list = userService.list(wrapper);
        if(!courseService.checkCourseNameUnique(course)){
            return R.error().data("message" , course.getCourseId() + "修改账号已存在");
        }
        courseService.updateById(course);
        return R.ok().data("message", "修改成功");

    }
    //@Log(modul = "用户模块" , type = "DELETE" , desc = "删除用户")
    @DeleteMapping("/delete/{courseId}")
    public Boolean deleteCourse(@PathVariable(value = "courseId") Long courseId){

        //return R.ok().data("message" , "删除成功");
        return courseService.deleteCourseById(courseId);
    }


    @GetMapping("/selectClass/{userId}/{courseId}")
    public R selectClass(@PathVariable(value = "userId") String userId , @PathVariable(value = "courseId") String courseId){
        Boolean isExist = courseService.selectClass(userId, courseId);
        if(isExist){
            return R.ok().data("message" , "选课成功");
        }else{
            return R.error().data("message" , "课程已选");
        }
    }

    @PostMapping("/getAlreadyClass/{userId}")
    public R getAlreadyClass(@PathVariable(value = "userId") String userId , @RequestBody(required = false) Course course){
        List<Course> alreadyClass = courseService.getAlreadyClass(userId, course);
        return R.ok().data("list" , alreadyClass);
    }


    @DeleteMapping("/dropClass/{userId}/{courseId}")
    public R dropClass(@PathVariable(value = "userId") String userId , @PathVariable(value = "courseId") String courseId){
        courseService.dropClass(userId , courseId);
        return R.ok().data("message" , "退课成功");
    }

    @GetMapping("/getTable/{userId}")
    public R getTable(@PathVariable(value = "userId") String userId , @RequestBody(required = false) Course course){
        List<ClassTable> list = classTableService.list();
        return R.ok().data("list" , list);
    }
}


package com.example.commonutils.service;

import com.example.commonutils.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-04-25
 */
public interface CourseService extends IService<Course> {

    List<Course> getCourseList(Course course);

    boolean checkCourseNameUnique(Course course);

    Boolean deleteCourseById(Long courseId);

    Boolean selectClass(String userId, String courseId);

    List<Course> getAlreadyClass(String userId, Course course);

    Boolean dropClass(String userId, String courseId);
}

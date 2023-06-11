package com.example.commonutils.mapper;

import com.example.commonutils.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-04-25
 */
public interface CourseMapper extends BaseMapper<Course> {

    Course checkCourseNameUnique(String courseId);

    Boolean updateCourseById(Long courseId);

    List<Course> getCourseList(Course course);

    List<Course> getAlreadyClass(String userId, String coursePeriod);

    void updateSelectPeople(@Param(value = "courseId") String courseId);

    void dropSelectPeople(@Param(value = "courseId")String courseId);
}

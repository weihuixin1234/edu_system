package com.example.commonutils.mapper;

import com.example.commonutils.entity.CourseDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-05-17
 */
public interface CourseDayMapper extends BaseMapper<CourseDay> {

    List<Integer> getCourseNo(@Param(value = "courseId") String courseId);
}

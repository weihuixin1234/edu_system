package com.example.commonutils.mapper;

import com.example.commonutils.entity.CourseStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-05-07
 */
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {

    List<String> selectClass(String userId);

    void insertClass(String userId, String courseId);

    Boolean dropClass(String userId, String courseId);
}

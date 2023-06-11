package com.example.commonutils.service.impl;

import com.example.commonutils.entity.Course;
import com.example.commonutils.entity.CourseDay;
import com.example.commonutils.entity.CourseStudent;
import com.example.commonutils.mapper.CourseDayMapper;
import com.example.commonutils.mapper.CourseMapper;
import com.example.commonutils.mapper.CourseStudentMapper;
import com.example.commonutils.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.commonutils.utils.StringUtils;
import com.example.commonutils.utils.constant.UserConstants;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gqq
 * @since 2023-04-25
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    CourseMapper courseMapper;

    @Resource
    CourseStudentMapper courseStudentMapper;

    @Resource
    CourseDayMapper courseDayMapper;

    @Override
    public List<Course> getCourseList(Course course) {
        return courseMapper.getCourseList(course);
    }

    @Override
    public boolean checkCourseNameUnique(Course course) {
        String courseId = StringUtils.isNull(course.getCourseId()) ? null : course.getCourseId();
        Course courseInfo = courseMapper.checkCourseNameUnique(course.getCourseName());
        if(StringUtils.isNotNull(courseInfo) && courseInfo.getCourseId() != courseId){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public Boolean deleteCourseById(Long courseId) {
        courseMapper.updateCourseById(courseId);
        return true;
    }

    @Override
    @Transactional
    public Boolean selectClass(String userId, String courseId) {
        List<String> beforeCourseId = courseStudentMapper.selectClass(userId);
        for (String id : beforeCourseId) {
            if(courseId.equals(id)){
                return false;
            }
        }
        courseStudentMapper.insert(new CourseStudent(null , courseId , userId));
        courseMapper.updateSelectPeople(courseId);
        return true;
    }

    @Override
    public List<Course> getAlreadyClass(String userId, Course course) {
        List<Course> alreadyClass = courseMapper.getAlreadyClass(userId, course.getCoursePeriod());
        for (Course aClass : alreadyClass) {
            List<Integer> courseNo = courseDayMapper.getCourseNo(aClass.getCourseId());
            aClass.setCourseNo(courseNo);
        }
        return alreadyClass;
    }

    @Override
    @Transactional
    public Boolean dropClass(String userId, String courseId) {
        Boolean isDelete = courseStudentMapper.dropClass(userId, courseId);
        courseMapper.dropSelectPeople(courseId);
        return isDelete;
    }
}

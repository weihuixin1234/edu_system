package com.example.commonutils.service.impl;

import com.example.commonutils.entity.Student;
import com.example.commonutils.mapper.StudentMapper;
import com.example.commonutils.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gqq
 * @since 2023-04-25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}

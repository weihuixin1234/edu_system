package com.example.commonutils.service.impl;

import com.example.commonutils.entity.ScoreTable;
import com.example.commonutils.entity.StudentScore;
import com.example.commonutils.mapper.StudentScoreMapper;
import com.example.commonutils.service.StudentScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gqq
 * @since 2023-05-23
 */
@Service
public class StudentScoreServiceImpl extends ServiceImpl<StudentScoreMapper, StudentScore> implements StudentScoreService {

    @Resource
    StudentScoreMapper studentScoreMapper;

    @Override
    public List<ScoreTable> getStuScoreList(StudentScore studentScore) {
        List<ScoreTable> stuScoreList = studentScoreMapper.getStuScoreList(studentScore);
        return stuScoreList;
    }
}

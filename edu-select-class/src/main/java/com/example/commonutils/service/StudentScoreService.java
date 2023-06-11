package com.example.commonutils.service;

import com.example.commonutils.entity.ScoreTable;
import com.example.commonutils.entity.StudentScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-05-23
 */
public interface StudentScoreService extends IService<StudentScore> {

    List<ScoreTable> getStuScoreList(StudentScore studentScore);
}

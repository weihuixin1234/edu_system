package com.example.commonutils.mapper;

import com.example.commonutils.entity.ScoreTable;
import com.example.commonutils.entity.StudentScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-05-23
 */
public interface StudentScoreMapper extends BaseMapper<StudentScore> {

    List<ScoreTable> getStuScoreList(StudentScore studentScore);
}

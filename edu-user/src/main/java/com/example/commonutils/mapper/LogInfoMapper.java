package com.example.commonutils.mapper;

import com.example.commonutils.entity.LogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gqq
 * @since 2023-04-08
 */
public interface LogInfoMapper extends BaseMapper<LogInfo> {

    List<LogInfo> selectLogList(LogInfo logInfo);
}

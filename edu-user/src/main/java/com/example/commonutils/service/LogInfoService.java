package com.example.commonutils.service;

import com.example.commonutils.entity.LogInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gqq
 * @since 2023-04-08
 */
public interface LogInfoService extends IService<LogInfo> {

    List<LogInfo> selectLogList(LogInfo logInfo);
}

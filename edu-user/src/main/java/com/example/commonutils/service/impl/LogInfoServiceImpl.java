package com.example.commonutils.service.impl;

import com.example.commonutils.entity.LogInfo;
import com.example.commonutils.mapper.LogInfoMapper;
import com.example.commonutils.service.LogInfoService;
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
 * @since 2023-04-08
 */
@Service
public class LogInfoServiceImpl extends ServiceImpl<LogInfoMapper, LogInfo> implements LogInfoService {

    @Resource
    LogInfoMapper logInfoMapper;
    @Override
    public List<LogInfo> selectLogList(LogInfo logInfo) {
        return logInfoMapper.selectLogList(logInfo);
    }
}

package com.example.commonutils.config.MybatisConfig;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 实现元对象处理器接口
 *
 * @author lizhihang
 * @create 2022-06-28 14:42
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //受理表逻辑删除状态
        this.setFieldValByName("state", 0, metaObject);
        //人员信息表,附件详情表表，附件目录表，逻辑删除状态
        this.setFieldValByName("sfyx", "0", metaObject);
        //视频信息表
        this.setFieldValByName("ysbs", "0", metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);

    }
}

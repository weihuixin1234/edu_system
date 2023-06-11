package com.example.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StudentTable对象", description="")
public class ScoreTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stuno;

    private String stuname;

    private String stusex;

    private String javajc;

    private String dsjswfx;

    private String xszc;

    private String linuxjc;

    private String sfsjyfx;

    private String yjhx;

    private String average;

    private String total;

}

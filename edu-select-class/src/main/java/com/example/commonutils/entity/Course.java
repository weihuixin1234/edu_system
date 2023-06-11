package com.example.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gqq
 * @since 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Course对象", description="")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "course_id", type = IdType.AUTO)
    private String courseId;

    private String courseName;

    private Integer courseCredit;

    private String courseTime;

    private String coursePeriod;

    private String sort;

    private String proId;

    private String courseAddress;

    private Integer selectNum;

    @TableField(exist = false)
    private String teacherName;

    private String college;

    private String specialty;

    private Integer beginTime;

    private Integer endTime;

    private Integer weekTime;

    private Integer xq;
    @TableField(exist = false)
    private List<Integer> courseNo;
}

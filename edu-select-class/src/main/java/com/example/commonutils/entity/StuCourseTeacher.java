package com.example.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
@ApiModel(value="StuCourseTeacher对象", description="")
public class StuCourseTeacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sct_id", type = IdType.AUTO)
    private String sctId;

    private String stuId;

    private String courseId;

    private String teacherId;

    private Integer grade;

    private String term;


}

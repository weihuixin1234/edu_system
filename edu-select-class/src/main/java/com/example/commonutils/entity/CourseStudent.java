package com.example.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gqq
 * @since 2023-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value="CourseStudent对象", description="")
public class CourseStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cs_id", type = IdType.ID_WORKER_STR)
    private String csId;

    private String courseId;

    private String stuId;


}

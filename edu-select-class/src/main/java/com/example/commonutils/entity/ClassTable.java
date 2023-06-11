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
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ClassTable对象", description="")
public class ClassTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "jcid", type = IdType.ID_WORKER_STR)
    private Integer jcid;

    private String sjbz;

    private String mc;

    private String z1;

    private String z2;

    private String z3;

    private String z4;

    private String z5;

    private String z6;

    private String z7;


}

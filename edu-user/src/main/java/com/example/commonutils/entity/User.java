package com.example.commonutils.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.example.commonutils.annotation.Excel;
import com.example.commonutils.utils.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.commonutils.annotation.Excel;
import com.example.commonutils.annotation.Excel.ColumnType;
import com.example.commonutils.annotation.Excel.Type;
import com.example.commonutils.annotation.Excels;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author gqq
 * @since 2023-03-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="")
@TableName(value = "user")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Excel(name = "用户序号" , cellType = ColumnType.NUMERIC , prompt = "用户编号")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Excel(name = "登录名称")
    private String name;

    @Excel(name = "用户性别")
    private String sex;

    private Integer uTimes;

    private String pwd;
    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String uuid;

    @Excel(name = "账号状态" , readConverterExp = "1=正常,2=停用")
    private String status;

    private String role;

    @TableField(fill = FieldFill.INSERT)
    @Excel(name = "创建时间" , dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Excel(name = "更新时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Version
    private Integer version;

    @TableLogic
    @TableField(select = false)
    private Integer isDeleted;

    private String openid;

    private String nickname;

    private String avatar;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.pwd;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    public String getNickname(){
        return this.nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}

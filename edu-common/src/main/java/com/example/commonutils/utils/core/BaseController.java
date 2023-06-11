package com.example.commonutils.utils.core;

import com.example.commonutils.entity.User;
import com.example.commonutils.utils.PageUtils;
import com.example.commonutils.utils.SecurityUtils;
import com.example.commonutils.utils.StringUtils;
import com.example.commonutils.utils.core.page.PageDomain;
import com.example.commonutils.utils.core.page.TableSupport;
import com.example.commonutils.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 获取用户缓存信息
     */
    public User getLoginUser()
    {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public String getUserId()
    {
        return getLoginUser().getId();
    }


    /**
     * 获取登录用户名
     */
    public String getUsername()
    {
        return getLoginUser().getUsername();
    }

}
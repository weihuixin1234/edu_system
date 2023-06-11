package com.example.commonutils.config.MybatisConfig;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
 *
 * mybatis-plus配置
 * @author Mark sunlightcs@gmail.com
 */

@Configuration
public class MybatisPlusConfig {

    /*
     *
     * 分页插件

     */

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /*
     *
     * 逻辑删除插件  该mybatis-plus版本无需手动配置插件
     *
     * @return

     */

    /*@Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }*/

    @Bean
    public CustomizedSqlInjector customizedSqlInjector() {
        return new CustomizedSqlInjector();
    }


}

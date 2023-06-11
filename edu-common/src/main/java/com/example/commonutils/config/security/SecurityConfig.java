package com.example.commonutils.config.security;


import com.example.commonutils.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * AuthenticationEntryPoin:用来解决匿名用户访问无权资源时的异常，也就时未登录
 * AccessDeineHandler用来解决认证过的用户访问无权资源时的异常
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    /**
     * AuthenticationManagerBuilder:用来配置全局的认证相关的信息，其实就是AuthenticationProvider和UserDetailsService，
     *              前者是认证服务提供商，后者是用户详情查询服务
     * WebSecurity:全局请求忽略规则配置
     * HttpSecurity:具体的权限控制规则配置，一个这个配置相当于xml配置中的一个标签
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/scy/login/**",
                "/scy/code",
                //"/log/**",
                "/api/ucenter/wx/**"

        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用JWT，而非cookie信息，避免csrf
        http.csrf().disable()
                //基于token,不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //所有请求都要认证
                .anyRequest()
                .authenticated()
                .and()
                //禁用缓存
                .headers()
                .cacheControl();
        http.cors();
        //添加jwt，登录授权拦截器
        http.addFilterBefore(jwtAuthencationTokenFilter() , UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);

    }
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return name -> {
            User admin = new User();
            admin.setName(name);
            if(admin != null){
                return admin;
            }
            return null;
        };
    }

    /**
     * PasswordEncoder是一个密码解析器
     * PasswordEncoder的一个常见实现类BCryptPasswordEncoder
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new JwtAuthencationTokenFilter();
    }
}

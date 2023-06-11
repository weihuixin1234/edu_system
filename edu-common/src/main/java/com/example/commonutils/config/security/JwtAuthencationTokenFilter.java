package com.example.commonutils.config.security;

import com.example.commonutils.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthencationTokenFilter extends OncePerRequestFilter {
    //@Value("${jwt.tokenHeader}")
    private String tokenHeader = "Authorization";
    //@Value("${jwt.tokenHead})
    private String tokenHead = "Bearer";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 实现了UserDetailServer接口，重写loadUserByUsername方法，从数据库获取用户名，密码等
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);
        //判断是否有token
        if(authHeader != null){
            //判断token的开头是不是定义的tokenHead
            if(authHeader.startsWith(tokenHead)){
                //把请求头中的tokenHead截取出来得到token
                String authToken = authHeader.substring(tokenHead.length());
                String username = jwtTokenUtil.getUserNameFormToken(authToken);
                //用户名存在但是未登录，也就是SecurityContextHolder.getContext().getAuthentication()里面记录的登录状态
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //首先先用用户名登录，进行验证
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    //验证token是否有效 ，重新设置用户对象
                    //当authToken解密后的用户名和通过上面的userDetails.getUsername相同且token没有过期时，才重新设置用户对象
                    if(jwtTokenUtil.validateToken(authToken , userDetails)){
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails , null , userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }
        filterChain.doFilter(request , response);
    }
}

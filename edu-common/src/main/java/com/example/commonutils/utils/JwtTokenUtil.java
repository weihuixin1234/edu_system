package com.example.commonutils.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    //用户名称
    private static final String CLATM_KEY_USERNAME="sub";
    //创建时间
    private static final String CLATM_KEY_CREATED="created";
    //加密方式 @Value("${jwt.secret}")
    private String secret = "yeb-secret";
    //失效时间
    //@Value("${jwt.expiration}")
    private Long expiration = 1800L ;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static  final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    //根据用户信息生成token
    public String generateToken(UserDetails userDetails){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLATM_KEY_USERNAME , userDetails.getUsername());
        claims.put(CLATM_KEY_CREATED , new Date());
        return generateToken(claims);
    }

    //根据claims载荷生成JWT.Token
    private String generateToken(Map<String ,Object> claims){
        return Jwts.builder()
                //注入参数
                .setClaims(claims)
                //设置过期时间
                .setExpiration(generateExpirationDate())
                //设置算法及密钥
                .signWith(SignatureAlgorithm.HS512 , secret)
                .compact();
    }

    //计算token过期时间
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration *  MILLIS_SECOND);
    }

    //从token中获取登录用户名
    public String getUserNameFormToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    //从token中获取载荷
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    //验证token是否有效
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFormToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    //判断token是否失效
    private boolean isTokenExpired(String token){
        Date expireDate = getExpiredDateFromToken(token);
        /**
         * Date1.after(Date2),当Date1大于Date2时，返回True,当小于等于时，返回false；
         * Date2.before(Date2),当Date1小于Date2时，返回True,当大于等于时，返回false；
         */
        return expireDate.before(new Date());
    }

    //从token中获取时间
    private Date getExpiredDateFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
}

package com.example.commonutils;

import com.example.commonutils.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class TestApplication {
    @Autowired
    RedisUtils redisUtils;
    @Test
    void contextLoads(){
        redisUtils.set("test" , "1111");
        System.out.println(redisUtils.get("test"));
    }

    @Test
    public void md5Test(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("123");
        String encode2 = passwordEncoder.encode("123");
        boolean matches = passwordEncoder.matches("123", "$2a$10$.WtqKC5xmVdj.56eE3./YetraILWTWCsfKRJqzuZCUdZ0iL1GrxUi");
        System.out.println(encode1);
        System.out.println(encode2);
        System.out.println(matches);
    }
}

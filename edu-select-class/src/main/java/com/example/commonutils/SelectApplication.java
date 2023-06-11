package com.example.commonutils;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@MapperScan(basePackages = "com.example.commonutils.mapper")
public class SelectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SelectApplication.class,args);
    }
}

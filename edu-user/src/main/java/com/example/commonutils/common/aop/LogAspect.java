package com.example.commonutils.common.aop;

import com.alibaba.fastjson.JSON;
import com.example.commonutils.common.annotation.Log;
import com.example.commonutils.entity.LogInfo;
import com.example.commonutils.service.LogInfoService;
import com.example.commonutils.utils.IpUtils;
import com.example.commonutils.utils.JwtTokenUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * AOP思想：AOP是一种面向切面的变成思想，面向切面编程是将程序抽象成各个切面，即解剖对象的内部，将
 * 那些影响了多个类的公共行为抽取到一个可重用模块里，减少系统的重复代码，降低模块间的耦合度，增强代码的可操作性
 * 和可维护性，AOP把软件系统分为两个部分：核心关注点和横切关注点，业务处理的主要流程是核心关注点，与之关系
 * 不大的部分是横切关注点，横切关注点的一个特点是，他们经常发生在核心关注点的多处，而各处都基本相似，
 * 比如权限认证，日志，事务处理，增强处理
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LogInfoService logInfoService;

    /**
     * 统计请求的处理时间，这里要用ThreadLocal一个线程一个时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 设置操作日志切入点，记录操作日志，在注解的位置切入代码
     */
    @Pointcut("@annotation(com.example.commonutils.common.annotation.Log)")
    public void logPointCut(){

    }

    /**
     * 设置操作异常切入点记录异常日志，扫描所有controller包下操作，controller包下出现异常会记录
     */
    @Pointcut("execution(* com.example.commonutils.controller..*(..))")
    public void exceptionLogPointCut(){

    }

    @Before("logPointCut()")
    public void doBefore(){
        //接收到请求，记录请求开始时间
        startTime.set(System.currentTimeMillis());
    }

    /**
     *  正常返回通知，拦截用户操作日志，连接点正常执行完成后执行，如果连接点抛出异常，则不会执行
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "logPointCut()" , returning = "keys")
    public void doAfterReturning(JoinPoint joinPoint , Object keys){
        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        LogInfo logInfo = LogInfo.builder().build();

        try{
            //从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            //获取切入点所在的方法
            Method method = signature.getMethod();

            //获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();

            //获取操作，就是标注在@Log注解上的属性
            Log log = method.getAnnotation(Log.class);
            if(Objects.nonNull(log)){
                logInfo.setModule(log.modul());
                logInfo.setType(log.type());
                logInfo.setMessage(log.desc());
            }
            //logInfo.setid(UUID.randomUUID().toString());    //logId ----> 随机生成的UUID

            logInfo.setMethod(className + "." + method.getName());  //请求的方法名

            String reqParam = JSON.toJSONString(converMap(request.getParameterMap()));
            if(reqParam.length() < 2000){
                logInfo.setReqParam(reqParam);   //获取请求参数
            } else{
                logInfo.setReqParam(reqParam.substring(0,2000));
            }

            if(JSON.toJSONString(keys).length() < 2000){
                logInfo.setResParam(JSON.toJSONString(keys));
            }else{
                logInfo.setResParam(JSON.toJSONString(keys).substring(0 , 2000)); //返回结果，json格式
            }

            //从token值中获取username
            String usernameByToken = getUsernameByToken(request);
            //if(usernameByToken.equals("admin")){
            //    logInfo.setUserName(usernameByToken);
            //}else{
            //    logInfo.setUserName()
            //}
            logInfo.setUserName(usernameByToken); //请求用户名称

            logInfo.setIp(IpUtils.getIpAddr(request));  //获取请求Ip
            logInfo.setUri(request.getRequestURI());  // 获取请求url

            //创建时间设置了MyBatis-plus的自动添加
            logInfo.setTakeUpTime(System.currentTimeMillis() - startTime.get());  //耗时

            logInfoService.save(logInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 转换request为Map请求参数
     * @param paramMap
     * @return
     */
    public Map<String , String> converMap(Map<String ,String[]> paramMap){
        HashMap<String, String> rtnMap = new HashMap<>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key , paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    public String getUsernameByToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || authHeader.equals("")){
            return "admin";
        }
        if(!authHeader.contains("Bearer")){
            return "admin";
        }
        String authToken = authHeader.substring("Bearer".length());
        String username = jwtTokenUtil.getUserNameFormToken(authToken);
        return username;
    }
}


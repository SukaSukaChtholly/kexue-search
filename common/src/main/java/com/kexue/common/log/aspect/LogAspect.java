package com.kexue.common.log.aspect;

import com.alibaba.fastjson2.JSON;
import com.kexue.common.log.annotation.Log;
import com.kexue.common.util.IpUtils;
import com.kexue.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    

    @Around("@annotation(controllerLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, Log controllerLog) throws Throwable {

        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        String methodInfo = controllerLog.info();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String args = JSON.toJSONString(joinPoint.getArgs());

        long startTime = System.currentTimeMillis();

        log.info("[{} start], 方法名->[{}#{}], IP->[{}], 请求参数->[{}],",
                methodInfo, className, methodName, ip, args);

        Object resp = joinPoint.proceed();

        long endTime = System.currentTimeMillis();

        log.info("[{} end], 耗时->[{}ms] 返回值->[{}], ",
                methodInfo, endTime - startTime, JSON.toJSONString(resp));
        
        return resp;
    }

}

package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
@Aspect
public class AopConfiguration {

    //private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("@annotation(com.example.demo.TrackTime)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed(joinPoint.getArgs());

        long timeTaken = System.currentTimeMillis() - startTime;
        //logger.info("Time Taken by {} is {}", joinPoint, timeTaken);
        System.out.println("Time taken for " + method.getName() + "() : " + timeTaken);

        return result;
    }

    @Around("@annotation(com.example.demo.SlowDown)")
    public Object slowndown(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SlowDown myAnnotation = method.getAnnotation(SlowDown.class);
        Thread.sleep(myAnnotation.value());

        Object result = joinPoint.proceed(joinPoint.getArgs());

        return result;
    }
     
//
}
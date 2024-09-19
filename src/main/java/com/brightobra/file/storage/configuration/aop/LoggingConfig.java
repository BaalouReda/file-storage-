package com.brightobra.file.storage.configuration.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class LoggingConfig {

    private static final Logger logger = LogManager.getLogger(LoggingConfig.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")//@see https://stackoverflow.com/questions/70011772/why-is-aop-logging-not-working-in-my-project
    private void publicMethodsFromLoggingPackage() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    private void publicMethodsFromServicePackage() {
    }

    @Around("publicMethodsFromServicePackage()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("call {} with args  {} in class  {} ", methodName, Arrays.toString(args),className);
        Object result = joinPoint.proceed();
        logger.debug("result of  {}  equals  {} in class  {} ", methodName, result,className);
        return result;
    }

    @Before(value = "publicMethodsFromLoggingPackage()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("call {} with args  {} in class  {} ", methodName, Arrays.toString(args),className);
    }

    @AfterReturning(value = "publicMethodsFromLoggingPackage()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.debug("result of  {}  equals  {} in class  {} ", methodName, result,className);
    }

    @AfterThrowing(pointcut = "publicMethodsFromLoggingPackage()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.error("throwing exception in   {} with message  {} in class  {}", methodName, exception.getMessage(),className);
    }

}



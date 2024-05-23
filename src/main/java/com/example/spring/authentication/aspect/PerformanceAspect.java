package com.example.spring.authentication.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@Slf4j
public class PerformanceAspect {

    @Pointcut("within(com.example.spring.authentication.rest.*)")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        log.info("[PerformanceAspect] Starting execution of " + getMethodName(joinPoint));
    }

    @After("controllerMethods()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        log.info("[PerformanceAspect] Finished execution of " + getMethodName(joinPoint));
    }

    @Around("controllerMethods()")
    public void measureMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTimer = System.nanoTime();
        Object result = proceedingJoinPoint.proceed();
        long endTimer = System.nanoTime();
        String methodName = getMethodName(proceedingJoinPoint);

        log.info("[PerformanceAspect] measureMethodExecution: Execution of " + methodName + " took "
                + TimeUnit.NANOSECONDS.toMillis(endTimer - startTimer) + "ms");
    }

    private String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}

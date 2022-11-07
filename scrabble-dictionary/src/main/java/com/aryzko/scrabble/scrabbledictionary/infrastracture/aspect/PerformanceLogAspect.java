package com.aryzko.scrabble.scrabbledictionary.infrastracture.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceLogAspect {

    private final String messageFormat = "Executed {} in {} ms. Data: names({}), values({})";

    @Around("@annotation(performanceLog)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint, final PerformanceLog performanceLog) throws Throwable {
        final String methodSignature = joinPoint.getSignature().toShortString();
        final String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        final Object[] argValues = joinPoint.getArgs();

        final long start = System.currentTimeMillis();
        final Object proceed = joinPoint.proceed();
        final long executionTime = System.currentTimeMillis() - start;

        log.info(messageFormat, methodSignature, executionTime, argNames, argValues);

        return proceed;
    }
}

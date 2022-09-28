package com.ysx.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * public * com.ysx.controller.*.*(..)由以下几部分组成
     * public                       方法修饰符
     * *                            返回值类型,*匹配所有类型
     * com.ysx.controller.*         包路径
     * *                            类名,*匹配该包下所有类
     * *(..)                        方法,*匹配类中所有方法,(..)匹配所有方法参数
     */
    private final static String POINT_CUT = "execution(public * com.ysx.controller.*.*(..))";

    @Pointcut(POINT_CUT)
    private void logAspect(){}

    @Before("logAspect()")
    public void before(JoinPoint joinPoint) throws Exception {
        log.info("================================================before start==========================================");
        Object[] args = joinPoint.getArgs();
        log.info("joinPoint.getArgs()={}", Arrays.toString(args));
        log.info("joinPoint.getThis()={}", joinPoint.getThis());
        log.info("joinPoint.getTarget()={}", joinPoint.getTarget());
        log.info("joinPoint.getSignature().getDeclaringType()={}", joinPoint.getSignature().getDeclaringType());
        log.info("joinPoint.getSignature().getModifiers()={}", Modifier.toString(joinPoint.getSignature().getModifiers()));
        log.info("joinPoint.getSignature().getName()={}", joinPoint.getSignature().getName());

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> parameterMap = new HashMap<>();
        for(Enumeration<String> e = parameterNames; e.hasMoreElements();){
            String parameterName = e.nextElement();
            String parameterValue = request.getParameter(parameterName);
            parameterMap.put(parameterName, parameterValue);
        }
        log.info("Parameters={}", new ObjectMapper().writeValueAsString(parameterMap));
        log.info("================================================before end==========================================");
    }


    @Around("logAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("================================================around start==========================================");
        Object proceed = joinPoint.proceed();
        log.info("proceed={}", proceed);
        log.info("================================================around end==========================================");
        return proceed;
    }

    @After("logAspect()")
    public void after(JoinPoint joinPoint) throws Throwable {
        log.info("================================================after start==========================================");
        log.info("================================================after end==========================================");
    }

    @AfterReturning("logAspect()")
    public void afterReturning(JoinPoint joinPoint) throws Throwable {
        log.info("================================================afterReturning start==========================================");
        log.info("================================================afterReturning end==========================================");
    }

    @AfterThrowing("logAspect()")
    public void afterThrowing(JoinPoint joinPoint) throws Throwable {
        log.info("================================================afterThrowing start==========================================");
        log.info("================================================afterThrowing end==========================================");
    }
}

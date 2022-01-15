package com.hongye.acpect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HongyeAspect {

    @Before("execution(public void com.hongye.service.UserService.test())")
    public void hongyeBefore(JoinPoint joinPoint){
        joinPoint.getTarget();//拿到的是普通对象 UesrService
         System.out.println("hongye before");
    }


}

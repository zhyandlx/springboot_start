package com.hongye.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserServiceBase {
    //因为有事务所以会生成事务的代理对象
    //以非事务的方式运行存在一个事务就抛一个异常
    @Transactional(propagation = Propagation.NEVER)
    public void expire(){

    }
}

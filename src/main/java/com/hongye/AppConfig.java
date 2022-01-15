package com.hongye;

import com.hongye.service.OrderService;
import com.sun.javafx.logging.PulseLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;

@ComponentScan("com.hongye")
//@EnableAspectJAutoProxy
//为什么加了Configuration事务就有用 为什么没加事务就没用
//加了@Configuration保证了dataSource是同一个 事务才会生效
//与spring的代理模式有关系
//AppConfig代理对象 代理逻辑
//首先会去看spring容器中有没有dataSource对象 如果有就直接返回了
//没有的话就创建 保证了是同一个对象
@Configuration
@EnableTransactionManagement
public class AppConfig {
    //这样spring容器里就有三个OrderService的bean
    @Bean
    public OrderService orderService1(){
        return new OrderService();
    }
    @Bean
    public OrderService orderService2(){
        return new OrderService();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        //因为放的是方法所以是不同的两个dateSource对象
        return new JdbcTemplate(this.dateSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        DataSourceTransactionManager transactionManager=new DataSourceTransactionManager();
        //因为放的是方法所以是不同的两个dateSource对象
        transactionManager.setDataSource(dateSource());
        return transactionManager;
    }

    @Bean
    public DataSource dateSource(){
        DriverManagerDataSource dataSource=new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/springstart");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

}

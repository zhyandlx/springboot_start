package com.hongye.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
//属于单例bean 非懒加载的单例模式

//懒加载 用的时候才去创建
//@Lazy
//原型bean
//如果是原型bean的话 每次getBean时创建新的对象 多例
//@Scope("66")
public class UserService implements InitializingBean {

//    public UserService() {
//        System.out.println("1111");
//    }

    //如果上面注掉的情况下 spring会调用下面的构造方法
    //如果只有下面一个构造方法orderService会不有值（有值）
    //那值从哪里来的呐（如果有参数 spring得找到这个参数值传进来）
    //spring从哪里去找（从spring容器中去找，所以被传的参数必须是一个spring管理的bean）
    //如果参数不是一个spring管理的bean就会报一下错误
    //Unsatisfied dependency expressed through constructor parameter 0; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.hongye.service.OrderService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
    //找的过程是怎么找的呐（当前能用的信息有什么？能知道的信息就是入参的类型和入参的名字）
    //拿beanName去spring容器里去找（会有问题）
    //拿类型去找会找到多个假如说找到三个（找到一个直接就用）
    //再通过名字的去找(没匹配上的时候)
    //expected single matching bean but found 3: orderService,orderService1,orderService2
//    public UserService(OrderService orderService123) {//spring容器可以想象成map Map<beanName,bean对象>
//        this.orderService = orderService123;
//        System.out.println("2222");
//    }
    //如果有两个有参的构造方法的时候加 @Autowired注解
//    @Autowired
//    public UserService(OrderService orderService) {//spring容器可以想象成map Map<beanName,bean对象>
//        this.orderService = orderService;
//        System.out.println("2222");
//    }


    //如果这两个相邻的方法没有注掉 spring会报错
    //原因是有两个有参的构造方法 spring不知道用哪个
    //对于java而言无参的构造方法是默认的 不需要声明的 有默认的意思在里面
    //如果有两个或两个以上的有参的构造方法 spring就默认就去找默认的构造方法
    //没有无参的构造方法就报错No default constructor found;
//    public UserService(OrderService orderService,OrderService orderService1) {
//        this.orderService = orderService;
//        System.out.println("2222");
//    }

    @Autowired
    private OrderService orderService;//付的是什么值？

//    @Autowired
//    private UserServiceBase userServiceBase;

    @Autowired
    private UserService userService;

    //管理原员的用户信息是不是都存在数据库
    //mysql--->user对象--->admin
    private User admin;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //@PostConstruct需要告诉spring我要在创建对象之前调用mySqlDate方法
    //初始化前
    @PostConstruct
    public void mySqlDate(){
        //mysql--->user对象--->admin
        System.out.println("this is mySqlDate");
    }

   @Transactional//因为开启了事务所以spring会创建一个代理对象
    public void test(){
       System.out.println("this is test");
        //得先拿到数据库连接执行sql 用自己的dataSource对象建立的连接自动提交
        jdbcTemplate.execute("INSERT t1 VALUES(1,1,1,1,1)");
//        throw new NullPointerException();
       //是以普通对象在调下面的方法所以不会抛异常导致事务失效
       //自己注入自己使事务生效
       //因为方法中有@Transactional注解 所以自己注入自己的时候在一个代理对象之中 生成两个事务 两个事务所以有效
//       userService.expire();
    }
    //以非事务的方式运行存在一个事务就抛一个异常
    @Transactional(propagation = Propagation.NEVER)
//    @Transactional
    public void expire(){

    }

    //初始化
    //spring怎么判断我们是否实现了InitializingBean呐？
    //对象 instance of InitializingBean 判断
    //如果实现了我就强转成InitializingBean对象
    //一旦你把它转化成了InitializingBean接口之后我是不是就能执行接口里的方法afterPropertiesSet
    //------------------------------------------------------------------------------
    //源码里spring会判断
    //有没有打开安全管理器System.getSecurityManager()
    //如果有打开通过下面的方式执行代码
    // try {
    //                    AccessController.doPrivileged(() -> {
    //                        ((InitializingBean)bean).afterPropertiesSet();
    //                        return null;
    //                    }, this.getAccessControlContext());
    //                } catch (PrivilegedActionException var6) {
    //                    throw var6.getException();
    //                }
    //如果没有打开通过下面的方式执行代码
    // ((InitializingBean)bean).afterPropertiesSet();
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("this is InitializingBean");
        "d".hashCode();
    }
}

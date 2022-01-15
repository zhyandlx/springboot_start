package com.hongye;

import com.hongye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Field;

public class Test {
    public static void main(String[] args) {
        //Spring Framework
        //以下是创建spring容器的两种方式
        //Spring 5.3.10
        //------------------------------------------------------
        //第二节课 手写spring
        //非懒加载的单例bean是什么时候创建的呐（spring启动的过程中会把非懒加载的单例bean创建出来）
        //如果是懒加载的会在getBean时创建
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        //Spring3.0之前xml的方式
        //ClassPathXmlApplicationContext context1 = new ClassPathXmlApplicationContext("spring.xml");

        //Spring帮我们创建的对象
        //Spring帮我们创建的对象与我们自己创建的普通对象（new 对象）的区别？
        //加了@Component表示对像交给spring容器管理  加了@Autowired 表示依赖注入
        //这时候如果从userService去拿orderService会不会有值？（有值）
        //那如果我们new UserService 呐 orderService会不会有值？（没值）
        //从以上现象可以看出spring创建的bean与我们自己new出来的一个小小区别
        //--------------------------------------------------------------------
        //spring 构造对象的过程
        //Bean 创建的生命周期（回调等） 还有bean销毁的生命周期
        //UserService.class ---->无参的构造方法（orderService属性无值）（推断构造方法）
        //                                                           --->对象（orderService属性无值）
        //                                                           --->依赖注入（加了@Autowired注解属性赋值）
        //                                                           --->初始化前
        //                                                           --->初始化
        //                                                           --->初始化后(AOP实现)判断bean进行aop
        //                                                           --->代理对象（如果有AOP代理对象将会成为bean）CGLIB
        //                                                           --->bean（orderService属性有值）
        //CGLIB的底层原理
        //首先生成一个被代理类的子类 比如说class UserServceProxy extends UserService{
        //    //会增加一个属性
        //    UserService target;
        //    //重写test方法
        //    public void test(){
        //        //进到这里
        //        //这里的逻辑是什么
        //        //先去执行切面的逻辑@Befor
        //        //再调用父类的test方法
                  //target.test()
        //
        //    }
        // }
        //UserServceProxy ---->代理对象.target=普通对象（orderService属性有值 进行过依赖注入）---->bean
        //代理对象.test（）
        UserService userService=(UserService)context.getBean("userService");
        //用了AOP之后userService 就是代理对象 UserService$$EnhancerBySpringCGLIB
        //这个时候的orderservice是没有值的
        //为什么呐？
        //因为aop是在初始化执行 得到代理对象的 所以这个对象并没有前面的那些依赖注入的操作 所以orderservice 没值
        //orderService=null
        //进到真正的test方法后orderService又有值了
        //为什么不给代理对象的orderService赋值哪 没有必要 代理对象的orderService没有地方用 赋值没有意义
        //doCreateBean
        //spring 为事务创建的代理对象 spring事务的代理逻辑是怎样的呐
        //----------------------------------------------------
        //1.写没写 @Transactional注解
        //2.写了的话 创建数据库连接（由事务管理器创建的）用自己的dateSource对象建立的
        //3.把conn.autoommit=false
        //默认是true(比如有两个sql及以上的sql执行完一个sql就提交了)
        //改为手动提交
        //如果抛异常了就rollback
        userService.test();

        //com.hongye.service.OrderService@3eeb318f
        //com.hongye.service.OrderService@20a14b55
        //com.hongye.service.OrderService@39ad977d
//        System.out.println(context.getBean("orderService"));
//        System.out.println(context.getBean("orderService1"));
//        System.out.println(context.getBean("orderService2"));
        //从数据库里拿到admin的属性之后就想让他有值
        //在成为bean之前调了
        // public void mySqlDate(){
               //mysql--->user对象--->admin
        //    }
        //准确的是在初始化前调用mySqlDate方法
        //找对象里哪些方法加了@PostConstruct然后执行
        //其实有两种方式可以实现另一种是 implements InitializingBean
        //userService.admin

        //依赖注入大概的工作原理
        //问题：值到底该怎么来？
        //for (Field field : userService.getClass().getFields()) {
        //    if (field.isAnnotationPresent(Autowired.class)){
        //           field.set(userService,??);
        //    }
        //}
        //@PostConstruct原理
        //for (Field field : userService.getClass().getFields()) {
        //    if (field.isAnnotationPresent(PostConstruct.class)){
        //           method.invoke(userService,null);
        //    }
        //}
        //SecurityManager
        PasswordSecurityManager manager = new PasswordSecurityManager("123456");
        if(manager.accessOK("2545")) System.out.println("OK"); else System.out.println("NO");

        //切面
        //切面也是一种bean只不过是一种特殊的bean 我们可以定义很多的切面Bean
        //找出所有切面bean
        //遍历切面bean 看有没有@Befor @After的注解
        //如果有我就看当前切点所定义的表达式 和我正在创建的userservice是不是匹配
        //如果匹配了 方法===userService的方法 就表示需要进行aop  会把方法===userService的方法 缓存起来（缓存map启动时缓存）备用map<class,List<找到的方法>>
    }
}


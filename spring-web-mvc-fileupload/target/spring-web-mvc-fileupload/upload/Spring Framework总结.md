# SpringIOC

## IOC容器概念

IOC其实就是一个对象的容器。全称Inversion Of Control 控制反转，核心的作用就是将原来由开发人员来控制的对象管理操作交由Spring来管理。

SpringIOC不仅帮我们管理了对象的创建，还包括给对象增加了生命周期行为、作用域(单例、非单例)、懒加载。 配合Spring的DI， 更能方便的解决对象属性值注入、对象之间的依赖注入问题。



## IOC容器初始化方式

### ClassPathXmlApplicationContext

该类负责加载类路径下的xml配置文件的方式，去初始化IOC容器上下文，具体使用步骤如下：

1. 编写配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
       
       <!-- 使用bean标签将对象配置到ioc容器中 -->
       <bean id="man" class="com.lanou3g.bean.Man" />
       <bean id="apple" class="com.lanou3g.bean.Food" />
   </beans>
   ```

   

2. 加载配置文件，初始化IOC容器
  ```java
   // 加载单个xml配置，初始化上下文
   ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext1.xml");
  
   // 加载多个xml配置，初始化上下文
   ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"applicationContext1.xml", "applicationContext2.xml"});
  
   // 加载当前运行类所在的类路径下所有以application开头的配置文件
   ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("appliction*.xml");
  
   // 加载工程中所有类路径下所有以application开头的配置文件
   ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:appliction*.xml");
  
   // 加载工程中所有类路径下所有以application或spring开头的配置文件
   ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{"classpath*:appliction*.xml", "classpath*:spring*.xml"});
  ```

  > 配置文件路径中可以包含通配符(\*)和前缀(classpath*:，代表所有类路径，包括源码类路径和单元测试类路径)

3. 从容器中获取对象

   ```java
   // 通过xml中配置的bean的id或name属性的值获取bean, 返回的类型是Object，需要强转成对象真正的类型
   Man man = (Man) cxt.getBean("man"); 
   // 通过bean的名称获取bean的同时，指定类型 
   Man man1 = cxt.getBean("man",Man.class); 
   // 通过类型从容器中获取bean，如果容器中该类型的bean有多于一个，则会报错
   Man man2 = cxt.getBean(Man.class); 
   
   // 调用对象的方法
   man.eat();
   man.play();
   ```

### AnnotationConfigApplicationContext

加载通过Java注解方式配置的Bean上下文。具体使用步骤如下：

1. 定义配置类

    ```java
    @Configuration	//代表该类是一个bean的配置类，类似于xml配置中的<beans>标签
    public class MyConfiguration {
    
    }
    ```

    1. 开启注解支持，配置扫描包路径

        如果是通过ClassPathXmlApplicationContext初始化的上下文，则在xml中添加如下配置：

        ```xml
        <!-- 添加context命名空间 -->
        <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:context="http://www.springframework.org/schema/context"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance	xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
               http://www.springframework.org/schema/context
               http://www.springframework.org/schema/context/spring-context.xsd">
        
        <!-- 下面两个配置用于xml和注解配置混合使用时，开启注解扫描支持，并指定扫描的包路径 -->
        <!-- 如果指定了扫描包路径，该配置可省略 -->
        <context:annotation-config /> 
        <context:component-scan base-package="com.lanou3g.spring" />
        ```

        如果是通过AnnotationConfigApplicationContext初始化的上下文，则需要在t通过`@Configuration`注解标注的配置类上添加 @ComponentScans注解：

        ```java
        @Configuration
        @ComponentScan(basePackages = "com.lanou.spring") //指定扫描包路径
        public class MyConfiguration {
        
        }
        ```

        

    2. 配置Bean

        通过注解方式有两种方式可以将类的对象交由IOC容器中管理

        - 通过@Component的方式定义

          ```java
          @Component
          public class King {
          }
          ```

          > 适用于我们自己定义的类，或者我们可以修改源代码的类

        - 通过@Bean注解定义

          ```java
          public class King {
          }
          
          
          @Configuration
          public class MyApplicationContextConf {
              @Bean
              public King king() {
                  return new King();
              }
          }
          ```

          > 适用于需要将第三方jar包中的类交由ioc管理，因为我们不能修改jar包中的代码，无法给其添加@Component注解

2. 加载配置类，初始化IOC容器

    ```java
    // 加载单个注解配置，初始化上下文
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfiguration.class);
    
    // 加载多个注解配置，初始化上下文
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(new Class[]{MyConfiguration1.class, MyConfiguration2.class});
    ```

3. 从容器中获取bean

   ```java
   // 通过xml中配置的bean的id或name属性的值获取bean, 返回的类型是Object，需要强转成对象真正的类型
   Man man = (Man) cxt.getBean("man"); 
   // 通过bean的名称获取bean的同时，指定类型 
   Man man1 = cxt.getBean("man",Man.class); 
   // 通过类型从容器中获取bean，如果容器中该类型的bean有多于一个，则会报错
   Man man2 = cxt.getBean(Man.class); 
   
   // 调用对象的方法
   man.eat();
   man.play();
   ```

   

## Bean初始化方式

### 构造方法方式（最常用）

```xml
<bean id="xx" class="com.test.StudentDao" />
```

### 静态工厂方法

```xml
<!-- 
	class：指定的是静态工厂类，而不是将要创建的对象类型
	factory-method: 指定的是工厂中的静态方法 
-->
<bean id="xx" class="com.test.StudentDaoFactory" factory-method="createDao" />
```

### 实例工厂方法

```xml
<!-- 
	class：指定的是实例工厂类 
-->
<bean id="xxFactory" class="com.test.StudentDaoFactory" />
<!-- 
	factory-bean：指定的是实例工厂对象
	factory-method: 指定的是工厂中的实例方法 
-->
<bean id="xx" factory-bean="xxFactory" factory-method="createDao" />
```

## Bean的命名

在XML中配置中可以通过<bean>标签上的id、name属性值给一个bean命名，以便在其他地方引用。

id属性： bean的唯一名称，只允许出现一个值。且同一个IOC容器中不允许出现两个id值一样的bean。

name属性： 和id类似也是给bean命名。但是name属性的值可以有多个，多个值之间使用英文逗号(,)或者英文分号(;)或者空格符隔开

## Bean的作用域

- prototype

  在SpringIOC中prototype scope的意思指的是非单例，就是每次使用该bean的时候都会重新创建一个对象。

  ![原型作用域示意图](https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/images/prototype.png)

- singleton(默认)

  singleton作用域是IOC中默认的作用域，代表单例。每次使用bean的时候，不会重新创建对象，在整个IOC容器中该类型的对象只有一个。

  ![单例作用域示意图](https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/images/singleton.png)

## Bean的生命周期

- Singleton Bean的生命周期

  - 初始化时机： 在IOC容器初始化时，就会把配置的所有单例bean实例化。
  - 销毁时机：在IOC容器销毁时，所有bean的销毁方法会被调用。
- Prototype Bean的生命周期
  - 初始化时机： 在实际使用该bean的时候，比如：getBean、获取依赖此bean的其他bean需要使用
  - 销毁时机： 在IOC容器销毁时。（但是通过destroy-method指定的声明周期方法不会被调用，也就是说Spring不提供prototypebean完整的生命周期管理）
- 如何指定生命周期的回调方法
  - xml中的init-method、destroy-method
  - 注解方式@PostConstrutor、@PreDestroy
- 指定默认的声明周期回调方法
  - 在xml中，通过在beans标签上添加default-init-method、default-destory-method来指定
  - 在注解配置中，没有对应的方法可以设置所有bean默认的生命周期回调



## Bean懒加载

lazy-init属性

默认是false

> 懒加载配置主要是针对单例的bean，因为它默认是在容器初始化时就被实例化了。


## 如何优雅的停止非Web Spring应用

添加一个shutdown hook。所有派生自ConfigurableApplicationContext接口的实现类都支持此方法

```java
ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("xxx.xml");

ctx.registerShutdownHook(); //注册停止回调
```

# Spring DI

## 概述

DI的全称是Dependency Injection（依赖注入）。IOC是将我们工程中的所有对象交由Spring来管理，DI是此基础，将对象中的属性、依赖的其他对象也管理起来，自动注入到由Spring帮我们管理的对象中。

> 将要注入的对象和目标对象都必须是由SpringIOC管理的bean.

## DI的细节实现

### 构造参数注入

将一个bean创建过程中构造方法需要的参数，通过Spring DI的方式，自动注入到构造方法中。

### Setter注入

先通过一个无参的构造方法创建对象，然后通过属性的setter方法，将属性值注入到对象上。

### 支持注入的类型

- 普通字面量

  - String
  - Integer(int)
  - Long(long)
  - Byte(byte)
  - ...

- 集和类型

  - List

    ```xml
    <bean id="xxx" class="xx.xxx.xxx.AA"></bean>
    <bean>
        <property name="hobbies">
        	<list>
            	<value>简单类型值</value>
                <bean>内部bean</bean>
                <ref bean="xxx" />
            </list>
        </property>
    </bean>
    ```

  - Map

    ```xml
    <bean id="xxx" class="xx.xxx.xxx.AA"></bean>
    <bean>
        <property name="gameTitle">
        	<map>
            	<entry key="王者荣耀" value="荣耀王者" />
                <entry key="王者荣耀" value-ref="xxx" />
            </list>
        </property>
    </bean>
    ```

  - Set

    ```xml
    <bean id="xxx" class="xx.xxx.xxx.AA"></bean>
    <bean>
        <property name="hobbies">
        	<!-- set用法和List类似， 里面可以注入普通字面量值、也可以是一个bean引用，或者内部bean、或者是一个set、list、Properties  -->
            <set>
            	<value>简单类型值</value>
                <bean>内部bean</bean>
                <ref bean="xxx" />
            </set>
        </property>
    </bean>
    ```

  - java.util.Properties

    ```xml
    <!-- props标签是用来注入java.util.Properties类型的属性，用法和map类似，但是属性值是在标签中间写 -->
    <property name="gameNick">
        <props>
            <prop key="王者荣耀">最擅长1V5</prop>
            <prop key="吃鸡">一枪爆头</prop>
        </props>
    </property>
    ```

  - 注入空置、空字符串

    ```xml
    <property name="gameNick">
        <null />
    </property>
    
    
    <property name="gameNick" value="" />
    ```

### 自动装配

##### 自动装配支持的策略

- byType  

  按照类型去IOC容器中找需要的bean，如果找到一个，则自动装配；如果没找到，不注入此属性；如果找到了多个匹配类型的bean，就会报错。

- byName

  按照名称去IOC容器中找需要的bean，如果找到就自动注入；如果没找到，不注入此属性。

- constructor

  工作原理和byType类似，也是按照类型去IOC容器中找对应的bean。不同的是注入的地方不是setter，而是构造方法的参数。

- no  （默认值）

  如果没有打开自动注入，默认Spring不会自动装配需要的属性。

## XML方式的DI

### 构造参数注入

- 常规写法

  ```xml
  <bean id="xx" class="" />
  <bean class="com.test.XXX">
  	<constructor-arg name="age" value="45" />
      <constructor-arg name="x" ref="xx" />  <!-- 注入引用的bean -->
      <constructor-arg name="">	<!-- 注入内部bean -->
      	<bean></bean>
      </constructor-arg>
      <constructor-arg name="">	<!-- 注入list类型构造参数 -->
      	<list>
          	
          </list>
      </constructor-arg>
      ....
  </bean>
  ```

- c命名空间简写

  ```xml
  <bean id="xx" class="" />
  <bean class="com.test.XXX" c:age="45" c:x-ref="xx" />
  ```

### setter方式注入

setter注入能够注入的类型以及写法基本和构造参数注入时的写法一致，只不过将<construtor-arg>标签换成了<property>

- 常规用法

  ```xml
  <bean id="xx" class="" />
  <bean class="com.test.XXX">
  	<property name="age" value="45" />
      <property name="x" ref="xx" />  <!-- 注入引用的bean -->
      <property name="">	<!-- 注入内部bean -->
      	<bean></bean>
      </property>
      <property name="">	<!-- 注入list类型构造参数 -->
      	<list>
          	
          </list>
      </property>
      ....
  </bean>
  ```

- p命名空间简写

  ```xml
  <bean id="xx" class="" />
  <bean class="com.test.XXX" p:age="45" p:x-ref="xx" />
  ```

### 开启自动装配

```xml
<!-- 
通过给当前的bean添加autowire属性开启自动注入
可选的值：参见自动装配章节
 -->
<bean id="xx" class="" autowire="" />
```



### 提高自动装配时的权重

```xml
<!-- 当其他的bean中需要注入一个Test类型的属性，而满足条件的bean有多个时，会优先注入primary="true"的bean -->
<bean id="xx" class="com.Test" primary="true" />
```

### 按类型自动装配时，不参与候选

```xml
<!-- 当其他的bean中需要注入一个Test类型的属性，而满足条件的bean有多个时，autowire-candidate="false"的bean会自动退出候选序列 -->
<bean id="xx" class="com.Test" autowire-candidate="false" />
```


## 注解方式的DI

- 构造参数注入

  在构造方法上添加 `@Autowired`注解，构造方法的参数就会自动注入进来

- setter方法注入

  方法有两种：

  1. 在属性的setter方法上添加`@Autowired`注解
  2. 在属性上添加`@Autowired`注解

### 提高自动装配时的权重

```java
@Primary
@Component
public class Test {


}

public class Main {
    
    @Primary
    @Bean
    public void test() {
        return new Test();
    }
    
}
```

## 注入外部Property文件中的属性值

### 在XML中的配置方式

```xml
<!-- 添加context命名空间 -->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">


<!-- 导入外部properties文件到ioc上下文中 -->
<context:property-placeholder location="jdbc.properties" />

<!-- 使用占位符的方式获取properties文件中的属性值 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.user}" />
    <property name="password" value="${jdbc.password}" />
    <property name="driverClassName" value="${jdbc.driver}" />
</bean>
```

### 在注解中的配置方式

通过`@PropertySource`注解可以将外部properties文件导入到上下文中，通过`@Value`注解可以获取properties文件中的属性值，并注入到当前类的属性中

```java
@Configuration
@ComponentScan(basePackages = "com.lanou.spring")
@PropertySource("classpath:/jdbc.properties")
public class MyConfiguration {

	@Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.driver}")
    private String driverClassName;
}
```




# SpringAOP

## 概述

AOP的全称是Aspect Oriented  Programming(面向切面编程)

OOP语言提供了类与类之间纵向的关系（继承、接口），而AOP补充了横向的关系（比如在不改变目标类中源代码的情况下给com.john.demo.dao包下所有类中以insert和update开头的方法添加事务管理）



### SpringAOP和AspectJ的区别

AspectJ是一个专门主打面向切面编程的框架。 它是使用一种特殊的语言(扩展自Java语言)来编写切面代码，后缀是.aj格式，并且需要使用专门的编译器将其编译成jvm可以运行的class文件。

SpringAOP底层也是使用了AspectJ的方案，但是在上层做了很多封装层面的工作，可以让开发人员直接使用Java代码来编写切面。并且由于使用的是标准的Java语言，所以并不需要在额外安装一个专门的编译器。但是由于开发人员直接接触的是Spring AOP，那么凡是Spring中没有实现的那些AOP功能，我们就用不了了，这种情况下只能跟产品经理撕逼或者去学习原生的AspectJ。

## AOP的术语

- 切面（Aspect）

  简单来说，切面就是我们要往目标代码中插入进去的代码。

- 连接点（Join Pointer）

  理论上所有可能会被切入的地方都可以称之为连接点

- 切入点(Pointcut)

  选择某个连接点切入，将切面代码织入进去。这个连接点就叫做切入点。

- 织入(Weaving)

  把切面代码糅合到目标代码中的过程就是织入。

- 通知(Advice)

  通知决定了切面代码织入到目标代码中后，运行的时机(比如是在目标方法执行前，还是执行后)。

## 在Spring中使用AOP

### 基于XML方式使用

1. 把aop的schema引入

   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/aop
          http://www.springframework.org/schema/aop/spring-aop.xsd">
   ```

2. 创建一个切面类，并且以bean的方式配置到IOC容器中

   ```java
   package com.lanou3g.spring;
   public class MyAspect {
   
       public void wakeup() {
           System.out.println("[前置通知]我刚学习SpringAOP睡着了，刚才谁打了我一下？");
       }
   
   
       public void goToBed() {
           System.out.println("[后置通知]SpringAOP太难了，一不小心又睡着了");
       }
   
   
       public void afterRetuing(Object message) {
           System.out.println("[后置返回通知]方法执行已经return了，方法返回值是：" + message);
       }
   
       public void afterThrowing(Throwable ex) {
           System.out.println("[后置异常通知]方法执行出现异常，异常原因：" + ex.getMessage());
       }
   
       /**
        * 环绕通知
        * 可以接受一个ProceedingJoinPoint参数
        *      通过此参数可以获取到被切入方法的所有信息
        *      还可以通过此参数来决定是否调用目标方法
        */
       public void aroundAdvice(ProceedingJoinPoint joinPoint) {
   
           // 连接点参数可以获取到被切入方法的所有信息
           // 这里演示了如何获取被切入方法的名称
           String targetMethodName = joinPoint.getSignature().getName();
           System.out.println("[环绕通知]被切入的方法名：" + targetMethodName);
   
           //
           System.out.println("[环绕通知]即将开始新的一天， 早起的鸟儿有虫吃！");
           try {
               joinPoint.proceed();
           } catch (Throwable throwable) {
               throwable.printStackTrace();
           }
           System.out.println("[环绕通知]这真是充实的一天， 早睡早起，方能养生！");
       }
   }
   ```

   ```xml
   <bean id="myAspect" class="com.lanou3g.spring.MyAspect" />
   ```

3. 使用<aop:config>标签配置aop（将切面、切入点、通知结合到一起）

   1. 定义切入点表达式
   2. <aop:aspect>
      1. 引用外部定义的切面bean
      2. 配置通知，引用切入点表达式

   ```xml
   <aop:config>
       <!-- 切入点表示匹配com.lanou3g.spring包下的所有类中所有以oneDay开头的方法，方法的参数、返回值不限 -->
   	<aop:pointcut id="myPointcut" expression="execution(* com.lanou3g.spring..*.oneDay*(..))" />
       <aop:aspect ref="myAspect">
       	<!-- 无论是否出现异常，只要被切入的方法开始运行，都会触发此通知 -->
               <aop:before method="wakeup" pointcut-ref="beforeOneDay" />
               <!-- 无论是否出现异常，只要被切入的方法运行结束，都会触发此通知 -->
               <aop:after method="goToBed" pointcut-ref="beforeOneDay" />
               <!--
               可以最大限度的对被切入方法附加功能，在方法执行前、后都可以通知（无论是否出现异常）
               ，还可以获取到被切入方法的所有信息，包括是否调用被切入的方法
               -->
               <aop:around method="aroundAdvice" pointcut-ref="beforeOneDay" />
               <!-- 被切入的方法正常返回值以后，会触发此通知 -->
               <aop:after-returning method="afterRetuing" pointcut-ref="beforeOneDay" returning="message" />
               <!-- 被切入的方法抛出异常以后，会触发此通知，并且不会触发after-returning -->
               <aop:after-throwing method="afterThrowing" pointcut-ref="beforeOneDay" throwing="ex" />
       </aop:aspect>
   </aop:config>
   ```

   

### 基于注解方式使用

 1. 开启AOP注解支持

    方式一：注解的方式

    ```xml
    @Configuration
    @EnableAspectJAutoProxy
    public class AppConfig {
    
    }
    ```

    方式二：xml中开启

    ```xml
    <aop:aspectj-autoproxy/>
    ```

	2. 定义切面类

    ```java
    /**
     * 该切面用来插入起床的逻辑
     */
    @Aspect
    @Component  //@Aspect注解没有将bean交给ioc容器管理的功能
    public class MyAspect {
    
        @Before("com.lanou3g.spring.aop.MyPointcut.allOneDayMehtod()")
        public void wakeup() {
            System.out.println("[前置通知]我刚学习SpringAOP睡着了，刚才谁打了我一下？");
        }
    
        @After("com.lanou3g.spring.aop.MyPointcut.allOneDayMehtod()")
        public void goToBed() {
            System.out.println("[后置通知]SpringAOP太难了，一不小心又睡着了");
        }
    
        @AfterReturning(value = "com.lanou3g.spring.aop.MyPointcut.allOneDayMehtod()", returning = "message")
        public void afterRetuing(Object message) {
            System.out.println("[后置返回通知]方法执行已经return了，方法返回值是：" + message);
        }
    
        @AfterThrowing(value = "com.lanou3g.spring.aop.MyPointcut.allOneDayMehtod()", throwing = "ex")
        public void afterThrowing(Throwable ex) {
            System.out.println("[后置异常通知]方法执行出现异常，异常原因：" + ex.getMessage());
        }
    
        /**
         * 环绕通知
         * 可以接受一个ProceedingJoinPoint参数
         *      通过此参数可以获取到被切入方法的所有信息
         *      还可以通过此参数来决定是否调用目标方法
         */
    //    @Around("com.lanou3g.spring.aop.MyPointcut.allOneDayMehtod()")
        public Object aroundAdvice(ProceedingJoinPoint joinPoint) {
    
            // 连接点参数可以获取到被切入方法的所有信息
            // 这里演示了如何获取被切入方法的名称
            String targetMethodName = joinPoint.getSignature().getName();
            System.out.println("[环绕通知]被切入的方法名：" + targetMethodName);
    
            //
            System.out.println("[环绕通知]即将开始新的一天， 早起的鸟儿有虫吃！");
            Object ret = null;
            try {
                ret = joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            System.out.println("[环绕通知]这真是充实的一天， 早睡早起，方能养生！");
            return ret;
        }
    }
    ```

    > 注意：@Aspect注解没有将bean交给ioc容器管理的功能，我们需要额外添加一个@Component注解

	3. 定义切入点

    官方建议我们将所有的切入点统一定义到一个地方管理，在配置通知时通过引入的方式来使用。方便后期维护（一处修改，处处生效）

    ```java
    @Component
    public class MyPointcut {
        // 通过@Pointcut注解定义一个切入点
        @Pointcut("execution(* oneDay(..))")
        public void allOneDayMehtod() {}
    }
    ```

    

	4. 在切面类中添加要切入的代码

    >  参见定义切面部分

	5. 在切入的代码方法上添加通知的注解

    > 参见定义切面部分

## Spring AOP代理机制实现原理

Spring AOP底层支持两种动态实现： 

- JDK原生的动态代理
- Cglib动态代理

> Spring在创建代理对象时，会自动选择要使用哪种代理方案。如果被代理的类实现了接口，那么就用JDK动态代理； 反之就使用Cglib动态代理

### JDK原生动态代理

区别静态代理每代理一个类就需要创建一个专门的代理类，动态代理只需要一个通用的代理类，即可代理所有实现了接口的类。

关键的API:

- InvocationHandler: 回调接口

  ```java
  
  public class MyProxy implements InvacationHandler {
      /**
       *  此方法在通过代理对象去调用目标方法时，会自动进入此方法(实际上调用的就是此方法)，目标方法时在此方法中调用的(当然，也可以不调用)。
  
       *  第一个参数proxy: 代理对象(注意不是目标对象)
       *  第二个参数method: 被代理的方法对象(方法本身)
       *  第三个参数args: 代理对象调用时穿进来的参数，用于在代理方法中调用原方法时传入
       *
       */
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
  
              String methodName = method.getName();
  
              // 模拟在方法运行前执行的操作
              System.out.println(methodName+ " 开始执行了");
  
              Object retVal =  method.invoke(target, args);
  
              // 模拟在方法运行后执行的操作
              System.out.println(methodName+ " 执行结束了，返回值： " + retVal);
  
              return retVal;
      }
  }
  ```

  

- Proxy： 创建代理类的工厂类，用于动态创建代理对象

  - 如何创建代理对象

    ```java
    // 1. 创建代理对象
    // 参数说明：
    //		第一个参数是类加载器
    //		第二个参数是被代理类实现的接口，可以写多个(写几个接口就代表你需要代理从几个接口中实现的方法)
    //        第三个参数是一个实现了InvacationHandler接口的对象，用于回调
    //        当我们通过代理对象去调用目标方法时，会自动执行第三个参数传进来的回调方法
    		
    Object obj = Proxy.newProxyInstance(classLoader, interfaces..., callback);
    
    // 2. 将类型强转成需要代理类的接口类型
    Man man = (Man)obj;
    
    // 3. 通过代理对象去调用原本想调用的方法
    man.oneDay();
    ```

### Cglib动态代理

关键的API

- Enhancer: 该类负责动态创建代理

  - 如何创建代理类

  ```java
  Enhancer enhancer = new Enhancer();	//类似于一个创建代理对象的工厂类
  // 下面三行类似于给工厂对象设置参数
  enhancer.setSuperclass(clazz);	// 让动态创建出来的代理类继承指定的类
  enhancer.setCallback(this);	 // 指定调用代理对象的方法时，进入的回调方法
  return enhancer.create();	// 创建代理对象
  ```

- MehtodInctercepor: 执行代理对象方法时的回调，作用类似于JDK动态代理中的InvacationHandler

  ```java
  public class MyCglibProxy implements MethodInterceptor {
  
      // 创建动态代理类的工厂对象
      private Enhancer enhancer = new Enhancer();
      public Object getProxy(Class clazz) {
          enhancer.setSuperclass(clazz);
          enhancer.setCallback(this);
          enhancer.setUseCache(false);
          return enhancer.create();
      }
  
      /**
       *  此方法在通过代理对象去调用目标方法时，会自动进入此方法(实际上调用的就是此方法)，目标方法时在此方法中调用的(当然，也可以不调用)。
  
       *  第一个参数proxy: 代理对象(注意不是目标对象)
       *  第二个参数method: 被代理的方法对象(方法本身)
       *  第三个参数args: 代理对象调用时穿进来的参数，用于在代理方法中调用原方法时传入
       *  第四个参数methodProxy: 是Cglib提供的一个方法代理对象，代理了第二个参数method,它可以实现直接调用传进来对象的父类上的方法
       */ 
      @Override
      public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
          String methodName = method.getName();
          System.out.println(methodName + "开始执行了");
          //代理类对象实例调用父类方法（其实就是调用被代理类上的方法实现）
          Object retVal = methodProxy.invokeSuper(proxy, args);
          System.out.println(methodName + "执行结束了");
          return retVal;
      }
  }
  ```



### 两种动态代理对比

- JDK动态代理要求被代理的类必须是至少实现一个接口才能代理
- Cglib动态代理没有上述限制，也就是说他可以代理实现了接口的类，也可以代理没实现接口的类
- JDK动态代理创建代理对象的原理是让创建的代理对象实现和被代理类一样的接口，从而代理接口中的方法
- Cglib动态代理创建代理对象的原理是让创建的代理对象继承被代理的目标类，从而代理从父类(被代理的类)中继承过来的方法

### 强制使用Cglib方式创建代理

如果上下文入口是XML配置文件

```xml
<!-- 方式一. 局部 -->
<aop:config proxy-target-class="true">
    <!-- other beans defined here... -->
</aop:config>

<!-- 方式二. 全局，在开启注解支持的地方添加属性(通过注解配置的AOP) -->
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

如果上下文入口是注解类

```java
@EnableAspectJAutoProxy(proxyTargetClass = true) // 开启AOP注解支持,并强制使用cglib代理
public class MyConfiguration {
}
```

## Spring声明式事务

### 在Xml中使用声明式事务的步骤

1. 添加tx schema

   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
          xmlns:tx="http://www.springframework.org/schema/tx"
          xmlns:context="http://www.springframework.org/schema/context"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns:aop="http://www.springframework.org/schema/aop"
          xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/tx
          http://www.springframework.org/schema/tx/spring-tx.xsd
          http://www.springframework.org/schema/aop
          https://www.springframework.org/schema/aop/spring-aop.xsd
           http://www.springframework.org/schema/context
          https://www.springframework.org/schema/context/spring-context.xsd">
   ```

   

2. 配置数据源

   ```xml
   <!-- 导入外部properties文件 -->
   <context:property-placeholder location="jdbc.properties" />
   <!-- 配置数据库连接池 -->
   <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
       <property name="url" value="${jdbc.url}" />
       <property name="driverClassName" value="${jdbc.driver}" />
       <property name="username" value="${jdbc.user}" />
       <property name="password" value="${jdbc.password}" />
   </bean>
   ```

   

3. 配置事务管理器

   ```xml
   <!-- 第一步： 配置事务管理器 -->
   <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <!-- 事务管理器必须依赖数据源 -->
       <property name="dataSource" ref="dataSource" />
   </bean>
   ```

4. 配置事务通知，同时还能指定一些事务相关的具体属性

   ```xml
   <!-- 第二步： 配置事务通知（不同于我们自己之前配置的前置、后置通知，这个是Spring帮我们封装好的，专门用来做事务管理的通知） -->
   <!-- tx:advice封装了切面和通知相关的逻辑，不需要我们自己再去编写切面和通知的逻辑 -->
   <tx:advice id="txAdvice" transaction-manager="transactionManager">
       <tx:attributes>
           <!-- 只有触发了特定异常才回滚事务 -->
           <tx:method name="*" rollback-for="Exception" />
           <!-- 触发以下特定异常，不会回滚事务 -->
           <tx:method name="*" no-rollback-for="NullPointerException" />
           <!-- 配置只读事务，只能查询，不能修改 -->
           <tx:method name="find*" read-only="true" />
           <!-- 配置事务超时时间，超时后事务自动回滚，单位：秒，
                   仅当传播行为propagation设置成REQUIRED或者REQUIRES_NEW的时候有效 -->
           <tx:method name="find*" timeout="500" />
       </tx:attributes>
   </tx:advice>
   ```

5. 配置事务的AOP

   其实就是将Spring给我们封装好的事务切面、通知和切入点整合到一起，通过AOP的方式来工作。

   ```xml
   <!-- 第三步： 配置AOP -->
   <aop:config>
       <aop:pointcut id="allServiceMethod" expression="execution(* com.lanou3g.spring.aoptx..*.*(..))"/>
       <!-- 这个advisor类似于我们手工配置的aop:aspect，它将切面、通知和切入点做了一个整合 -->
       <aop:advisor advice-ref="txAdvice" pointcut-ref="allServiceMethod" />
   </aop:config>
   ```

   

### 在注解中使用声明式事务的步骤

1. 开启注解事务支持

   开启注解事务支持有两种方式

   方式一： 在xml配置文件中开启

   ```xml
   <!-- 开启事务注解扫描 -->
   <!-- 如果定义的事务管理器名称就叫transactionManager，则此属性可以省略 -->
   <tx:annotation-driven transaction-manager="txManager" />
   ```

   方式二：在注解配置类上开启，添加`@EnableTransactionManagement`注解

   ```java
   @Configuration
   @EnableTransactionManagement
   @ComponentScan(basePackages = "com.lanou3g.spring")
   @PropertySource("classpath:jdbc.properties")
   public class MyConfiguration {
       
   }
   ```

   

2. 配置数据源

   ```java
   /**
   * 配置数据源
   * @return
   */
   @Bean
   public DataSource dataSource() {
       DriverManagerDataSource dataSource = new DriverManagerDataSource();
       dataSource.setDriverClassName(driver);
       dataSource.setUrl(url);
       dataSource.setUsername(user);
       dataSource.setPassword(password);
       return dataSource;
   }
   ```

   

3. 配置事务管理器

   ```java
   /**
    * 配置事务管理器
    * @param dataSource
    * @return
    */
   @Bean
   public PlatformTransactionManager transactionManager(DataSource dataSource) {
       return new DataSourceTransactionManager(dataSource);
   }
   ```

   

4. 在需要事务管理的方法上添加`@Transactional`注解

   ```java
   // 凡是xml中支持的事务属性，在注解中都有对应的属性来实现，具体属性含义参见xml配置
   @Transactional(
       rollbackFor = Exception.class   // 指定哪些异常可以触发事务回滚
       //noRollbackFor =   // 指定事务不回滚哪些异常
       //            isolation =   // 指定事务隔离级别
       //            timeout =     // 指定事务超时时间
       //            propagation = // 指定事务传播行为
       //            readOnly = // 指定只读事务
   )
   public void login(User user) {
   
       // Service中只写业务操作代码，不需要关注事务管理
       // 1 更新用户表用户最后登录时间
       user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
       userDao.updateUser(user);
   
       int ret = 9 / 0;  // 模拟操作异常
   
       // 2 插入登录日志
       SystemLog log = new SystemLog();
       log.setAction("login");
       log.setOperator(user.getUserName());
       log.setCreateTime(new Date());
       systemLogDao.insertLog(log);
   }
   ```

   > @Transactional注解除了可以在方法上使用外，还可以在类上。表示类中所有的公开方法都添加此事务管理

### XML方式的事务和注解方式的事务该选哪个？

- XML方式的事务
  - 优点是对代码没有任何侵入性，修改事务相关逻辑时，只需要修改配置文件，无需重新编译代码。另外XML方式可以通过切入点表达式灵活的对大量的类添加事务管理。
  - 缺点是配置相较于注解方式麻烦一些
- 注解方式的事务
  - 优点是配置简单，使用方便
  - 缺点是无法统一对大量的方法添加事务管理，需要在添加事务的类或方法上一个个添加事务注解，当工程中需要事务管理的代码很多时，工作量就比XML方式还要大。



### Spring事务的传播行为和隔离级别

#### 事务的传播行为

事务传播描述的事务与事务之间的传播关系， 常见的场景是在一个嵌套调用的方法中，外部方法和内部每个方法都添加了事务管理， 不同的传播行为配置，决定了最终是这些方法是使用同一个事务，还是在不同的事务中运行。

- PROPAGATION_REQUIRED 

  支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。

- PROPAGATION_SUPPORTS

  支持当前事务，如果当前没有事务，就以非事务方式执行。

- PROPAGATION_MANDATORY 

  支持当前事务，如果当前没有事务，就抛出异常。

- PROPAGATION_REQUIRES_NEW 

  新建事务，如果当前存在事务，把当前事务挂起。

- PROPAGATION_NOT_SUPPORTED 

  以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。

- PROPAGATION_NEVER 

  以非事务方式执行，如果当前存在事务，则抛出异常。

- PROPAGATION_NESTED 

  如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。



#### 事务的隔离级别

事务的隔离级别描述的是多个事务之间的可见性问题。比如一个事务还未提交时，其他事务是否能看到被未提交事务修改的数据。

| 隔离级别                   | 说明                                                         |
| -------------------------- | ------------------------------------------------------------ |
| ISOLATION_DEFAULT          | 这是一个PlatfromTransactionManager默认的隔离级别，使用数据库默认的事务隔离级别. |
| ISOLATION_READ_UNCOMMITTED | 这是事务最低的隔离级别，它允许另外一个事务可以看到这个事务未提交的数据。 这种隔离级别会产生脏读，不可重复读和幻像读。 |
| ISOLATION_READ_COMMITTED   | 保证一个事务修改的数据提交后才能被另外一个事务读取。另外一个事务不能读取该事务未提交的数据。 这种事务隔离级别可以避免脏读出现，但是可能会出现不可重复读和幻像读。 |
| ISOLATION_REPEATABLE_READ  | 这种事务隔离级别可以防止脏读、不可重复读。但是可能出现幻像读。 它保证了一个事务不能修改已经由另一个事务读取但还未提交的数据 |
| ISOLATION_SERIALIZABLE     | 这是花费最高代价但是最可靠的事务隔离级别。事务被处理为顺序执行。 除了防止脏读，不可重复读外，还避免了幻像读。 |

##### 关键词：
- 幻读（虚读）

>  事务1读取记录时事务2增加了记录并提交，事务1再次读取时可以看到事务2新增的记录； 
> 通俗的说，幻读就是指在一个事务内读取了别的事务插入的数据，导致前后读取不一致(insert)

- 不可重复读取

>  事务1读取记录时，事务2更新了记录并提交，事务1再次读取时可以看到事务2修改后的记录；
> 在一个事务内读取表中的某一行数据,多次读取结果不同.一个事务读取到了另一个事务提交后的数据.

- 脏读

>  事务1更新了记录，但没有提交，事务2读取了更新后的行，然后事务T1回滚，现在T2读取无效。
> 通俗的说，脏读就是指一个事务读取了一个未提交事务的数据








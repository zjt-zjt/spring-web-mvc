# Mybatis框架学习笔记

## Mybatis介绍

## Mybatis简单使用

1. 初始化SqlSessionFactory对象

   SqlSessionFactory对象需要从一个核心配置文件中构建，因此我们创建SqlSessionFactory对象之前需要先配置一个Mybatis核心配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
       <!-- 引入外部properties文件,路径从类路径的根目录开始 -->
       <properties resource="jdbc.properties" />
   
       <settings>
           <!-- 开启将数据库中下划线连接的字段自动映射为Java的小驼峰命名 -->
           <setting name="mapUnderscoreToCamelCase" value="true"/>
       </settings>
   
       <!-- 定义类型别名，在xxxMapper.xml文件中就可以用别名代替很长的类名 -->
       <typeAliases>
           
               <!-- 单个类配置别名 -->
   <!--        <typeAlias type="com.lanou3g.mybatis.bean.User" alias="User" />-->
           
               <!-- 统一配置某个包下所有类的别名, 会使用 Bean 的首字母小写的类名来作为它的别名。 -->
           <package name="com.lanou3g.mybatis.bean" />
       </typeAliases>
   
   
       <!-- 配置不同环境的参数 -->
       <environments default="development">
           <!-- 开发环境数据库、事务配置 -->
           <environment id="development">
               <!-- 事务管理使用JDBC的事务 -->
               <transactionManager type="JDBC"/>
               <!-- 配置开发环境数据源 -->
               <dataSource type="POOLED">
                   <property name="driver" value="${jdbc.driver}"/>
                   <property name="url" value="${jdbc.url}"/>
                   <property name="username" value="${jdbc.user}"/>
                   <property name="password" value="${jdbc.password}"/>
                   <!-- 将所有driver.开头的参数，附加到url属性的值后面上 -->
                   <property name="driver.characterEncoding" value="utf8"/>
               </dataSource>
           </environment>
       </environments>
   
       <!-- 将mapper SQL映射文件包含进来 -->
       <mappers>
           <!-- 将通过XML方式配置的mapper引入进来 -->
           <mapper resource="mapper/userMapper.xml"/>
           <!-- 将通过注解方式配置的mapper引入进来 -->
   <!--        <mapper class="com.lanou3g.mybatis.mapper.UserMapper" />-->
   
           <!-- 将com.lanou3g.mybatis.mapper包下所有通过注解方式配置的mapper引入进来 -->
   <!--        <package name="com.lanou3g.mybatis.mapper"/>-->
       </mappers>
   </configuration>
   ```

   构建对象

   ```java
   // 1. 初始化mybatis配置
   String confPath = "mybatis_conf.xml";
   InputStream in = Resources.getResourceAsStream(confPath);
   
   // 2. 构建SqlSessionFactory对象
   SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
   ```

   

2. 创建SqlSession对象

   通过上一步的SqlSessionFactory对象可以获取到负责执行SQL语句的SqlSession对象

   ```java
   // 3. 获取SqlSession对象，默认事务不自动提交
   // SqlSession sqlSession = sqlSessionFactory.openSession();
   // 获取一个自动提交事务的sqlSession
   SqlSession sqlSession = sqlSessionFactory.openSession(true);
   ```

   

3. 用SqlSession对象从Mybatis中获取Mapper接口的实现类

   ```java
   // 4. 获取Mapper
   UserMapper mapper = sqlSession.getMapper(UserMapper.class);
   ```

   

4. 编写Mapper对象的xml配置文件

   XML格式的Mapper配置文件类似于接口的实现类，它指定了具体要执行的SQL语句，以及结果集如何映射。

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
           PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="com.lanou3g.mybatis.mapper.UserMapper">
       <select id="queryAllUser" resultType="user">
           select * from user
        </select>
   
       <insert id="insertUser">
         insert into user (username,nick_name,email)
         values (#{username},#{nickName},#{email})
       </insert>
   </mapper>
   ```

   

## 深入了解Mybatis

### 主要组件

#### 核心配置文件

核心配置文件是Mybatis的入口，它里面可以配置mybatis的具体参数、数据源、类型别名、关联映射文件等。。

具体的参数配置说明参见：

[Mybatis3核心配置文件官方说明](https://mybatis.org/mybatis-3/zh/configuration.html)

#### SqlSessionFactory

一个SqlSessionFactory只能连接一个数据库实例，如果需要连接多个数据库，需要构建多个SqlSessionFactory对象。

在构建SqlSesssionFactory时可以指定environment的id，表示使用指定的数据源来构建factory对象

```java
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in, "dev");
```

#### SqlSession

一个SqlSession对象代表一次到数据的会话，该对象有一下功能：

- 获取Mapper实现类
- 管理事务操作



> 注意： SqlSession对象是非线程安全的，在多线程环境下，建议不要作为类的实例属性来用。



#### Mapper

- Mapper接口类

  定义了增删盖查的方法。注意，必须是接口类型，而且方法只需要定义就可以了。

- Mapper配置文件

  Mapper配置文件中就是负责实现接口中的方法，它定义了具体要执行什么SQL语句，如何映射结果集。

  - 配置文件中select、delete、update、insert标签的id必须是对应接口中的方法名。
  - mapper文件的namespace属性需要对应Mapper接口的完全类型限定名。



### 深入Mybatis核心配置文件

具体的参数配置说明参见：

[Mybatis3核心配置文件官方说明](https://mybatis.org/mybatis-3/zh/configuration.html)

### 深入Mybatis映射配置文件

#### CRUD语句定义

##### 查询语句

接口中

```java
List<User> queryAllUser();

User queryUserById(Integer id);
```

xml配置文件中

```xml
<select id="queryAllUser" resultType="user">
    select * from user
</select>

<select id="queryUserById" resultType="user">
    select * from user where id = #{id}
</select>
```




##### 插入语句

###### 普通插入语句

接口中

```java
int insertUser(User user);
```

xml配置文件中

```xml
<insert id="insertUser">
    insert into user (username,nick_name,email)
    values (#{username},#{nickName},#{email})
</insert>
```



###### 如何返回数据库自增的ID

```xml
<!-- 给insert语句添加useGeneratedKeys、keyProperty后，mybatis会将自增的id值直接赋值到传进来的user对象的id属性上
        useGeneratedKeys: 指定需要获取数据库自增的id
        keyProperty: 指定自增地段的名称
     -->
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
    insert into user (username,nick_name,email)
    values (#{username},#{nickName},#{email})
</insert>
```

##### 删除语句

接口中

```java
void deleteUserById(Integer id);
```

XML配置中

```xml
<delete id="deleteUserById">
    delete from user where id = #{id}
</delete>
```



##### 更新语句

接口中

```java
void updateUser(User user);
```

XML配置中

```xml
<update id="updateUser">
    update user set password = #{password} where id = #{id}
</update>
```



#### 接口中的参数如果传递到SQL中

##### 简单类型参数

接口中：

```java
void deleteUserById(Integer id);
```

xml配置文件中：

```xml
<delete id="deleteUserById">
    delete from user where id = #{id}
</delete>
```

##### 引用类型参数

接口中：

```java
int insertUser(User user);
```

xml配置文件中：

```xml
<!-- 给insert语句添加useGeneratedKeys、keyProperty后，mybatis会将自增的id值直接赋值到传进来的user对象的id属性上
        useGeneratedKeys: 指定需要获取数据库自增的id
        keyProperty: 指定自增地段的名称
     -->
<insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
    insert into user (username,nick_name,email)
    values (#{username},#{nickName},#{email})
</insert>
```



##### 当接口中参数和XML配置取值时名称不一样时

在接口中的参数前加注解

```java
User queryUserById(@Param("id") Integer xxxxxxxId);
```

在XML中取值时用注解指定的名称

```xml
<select id="queryUserById" resultType="user">
    select * from user where id = #{id}
</select>
```



##### #{}与${}的区别

- 它俩都可以获取接口调用中传递过来的参数
- #{}会将参数作为占位符，使用预编译语句(PreparedStatement)执行
- ${}会直接用实际参数替换${}， 参数可以作为SQL的一部分。

比如，要实现下面的效果：

Java代码

```java
List<User> queryAllUserOrderBy(String order);

// 调用：
mapper.queryAllUserOrderBy("last_login_time");
```

XML配置：

```xml
<select id="queryAllUserOrderBy" resultMap="userMap">
    select * from user order by ${order} desc;
</select>
```

如果上面使用#{order}取获取参数，最终执行的SQL会是这样：

```
==>  Preparing: select * from user order by ? desc; 
==> Parameters: last_login_time(String)
```

无法实现排序效果

如果使用${order}来取参数，最终执行SQL：

```
==>  Preparing: select * from user order by last_login_time desc; 
==> Parameters: 
```

可以实现排序效果



#### 结果集如何映射

##### ResultType方式

ResultType方式适用于数据库结果集可以直接映射成一个Java类的情况

Java实体类：

```java
@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String username;
    private String nickName;
    private String password;
    private String email;
    private Timestamp lastLoginTime;
}
```



使用方法：

```xml
<select id="queryAllUser" resultType="com.lanou3g.bean.User">
    select * from user
</select>
```



##### ResultMap方式

ResultMap方式适用于复杂的结果集映射，比如数据库返回的结果集中的列名和JavaBean无法一一对应，或者对象间存在一对一、一对多关联映射时。

###### 解决数据库列名与Java类中属性名不一致的映射问题

```xml
<mapper>
    ...
    <resultMap id="userMap" type="user">
        <id property="id" column="id" />
        <result property="username" column="username" />
        <result property="lastLoginttime" column="last_login_time" />
        <result property="email" column="email" />
        <result property="nickName" column="nick_name" />
    </resultMap>
    ...
</mapper>
```

在查询语句中将resultType换成resultMap

```xml
<select id="queryAllUser" resultMap="userMap">
    select * from user
</select>
```

> 其实，如果遇到单纯字段名和属性名不对应的情况，使用别名的方式更简单

###### 解决一对一映射查询问题



###### 解决一对多映射查询问题



#### 动态SQL



##### 条件分支SQL

- if
- choose&when&otherwise

##### 循环SQL

- forEach

##### 其他特殊SQL

- where
- set
- trim

#### 批量插入

##### 通过forEach动态SQL方式

###### 实现原理

forEach批量插入的原理是直接通过forEach动态标签，根据传过来的参数数量动态生成一个很长的SQL语句。一个语句就是一次批量插入。

语句形如：

```sql
insert into user (username, age) values
('张三', 10),
('李四', 10),
('王五', 10),
('赵六', 10),
('盖聂', 9000)

# 上面的语句一次会插入5条数据
```



我们需要做的就是生成此语句就可以了。

###### 具体做法

Mapper接口

```java
int batchInsertUser(List<User> userList);
```

Mapper映射文件

```xml
<insert id="batchInsertUser">
	insert into user (username, age) values
    <forEach collection="list" item="user" separator=",">
    	(#{user.username}, #{user.age})
    </forEach>
</insert>
```



##### 通过Executor.BATCH的方式

###### 实现原理

这种批量插入在底层的Mapper接口和Mapper映射文件中，都只是一个普通插入单条数据的写法。它通过在上层获取SqlSession时，指定执行类型是批量ExcecutorType.BATCH的方式，实现每次执行完单条插入以后并没有真正写入数据库，只有当调用sqlSession.flushStatement()时，才会将这一批数据一次性写入数据库，从而实现批量操作。

###### 使用步骤

1. 获取SqlSession时指定执行类型为批量

   ```java
   // 获取一个批量执行的sqlSession对象
   SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
   ```

   

2. Mapper接口和Mapper映射文件中只需按照单条插入去写方法和SQL语句即可

   

   Mapper接口

   ```java
   int insertUser(User user);
   ```

   

   XML映射文件

   ```xml
   <insert id="insertUser">
       insert into user (nick_name, status, createtime)
       values
       (#{nickName},#{status},#{createtime})
   </insert>
   ```

   

3. 空置批量的大小，在够一批数据时，调用`sqlSession.flushStatement()`去整体往数据库写一次

   ```java
   int batchSize = 100;
   int count = 0;
   List<BatchResult> resultList = new ArrayList<>();
   for(User user : userList) {
       // ExecutorType.Batch方式这里返回的不是影响的条数，具体获取方法参见下面代码
       mapper.insertUser(user);
       count++;
       if(count % batchSize == 0) {
           resultList.addAll(sqlSession.flushStatements());
       }
   }
   if(count % batchSize != 0) {
       resultList.addAll(sqlSession.flushStatements());
   }
   ```

4. 获取影响的条数

   这种方式获取影响条数没有那么直接，需要去API返回的BatchResult对象中读取updateCounts方法才能拿到。

   ```java
   int rows = 0;
   for(BatchResult batchResult : resultList) {
       int[] updateCounts = batchResult.getUpdateCounts();
       for(int updateCount : updateCounts) {
           rows += updateCount;
       }
   }
   System.out.println("批量插入成功，响应的行数：" + rows);
   ```

   

#### 缓存

##### 一级缓存（本地缓存）

Mybatis的一级缓存是指Session缓存。一级缓存的作用域默认是一个SqlSession。Mybatis默认开启一级缓存。 
也就是在同一个SqlSession中，执行相同的查询SQL，第一次会去数据库进行查询，并写到缓存中； 
第二次以后是直接去缓存中取。 
当执行SQL查询中间发生了增删改的操作，MyBatis会把SqlSession的缓存清空。

> 问题： 如果增删改的表和缓存的表是毫不相干的两个表，是否会清空缓存？

###### 测试方法

1. 我们在一个 sqlSession 中，对 User 表根据id进行两次查询，查看他们发出sql语句的情况

```java
@Test
public void testSelectOrderAndUserByOrderId(){
    //根据 sqlSessionFactory 产生 session
    SqlSession sqlSession = sessionFactory.openSession();
    String statement = "one.to.one.mapper.OrdersMapper.selectOrderAndUserByOrderID";
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    //第一次查询，发出sql语句，并将查询的结果放入缓存中
    User u1 = userMapper.selectUserByUserId(1);
    System.out.println(u1);
     
    //第二次查询，由于是同一个sqlSession,会在缓存中查找查询结果
    //如果有，则直接从缓存中取出来，不和数据库进行交互
    User u2 = userMapper.selectUserByUserId(1);
    System.out.println(u2);
     
    sqlSession.close();
}
```

2.  同样是对user表进行两次查询，只不过两次查询之间进行了一次update操作。

```java
@Test
public void testSelectOrderAndUserByOrderId(){
    //根据 sqlSessionFactory 产生 session
    SqlSession sqlSession = sessionFactory.openSession();
    String statement = "one.to.one.mapper.OrdersMapper.selectOrderAndUserByOrderID";
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    //第一次查询，发出sql语句，并将查询的结果放入缓存中
    User u1 = userMapper.selectUserByUserId(1);
    System.out.println(u1);
     
    //第二步进行了一次更新操作，sqlSession.commit()
    u1.setSex("女");
    userMapper.updateUserByUserId(u1);
    sqlSession.commit();
     
    //第二次查询，由于是同一个sqlSession.commit(),会清空缓存信息
    //则此次查询也会发出 sql 语句
    User u2 = userMapper.selectUserByUserId(1);
    System.out.println(u2);
     
    sqlSession.close();
}
```

##### 如何禁用一级缓存

- 在映射文件中给对应的select标签上添加`flushCache="true"`属性
- 在核心配置文件中将localCacheScope设置成STATEMENT（默认值是SESSION）



##### 二级缓存（全局缓存）

###### 什么是二级缓存？

Mybatis中二级缓存相比一级（本地）缓存来说是一个作用域更大的缓存方案。 二级缓存的作用域可以跨多个SqlSession，只要是同一个namespace下的mapper映射文件都可以共享缓存。但是不能跨SqlSessionFactory。

Mybatis二级缓存需要手动开启。

###### 开启二级缓存的方法

1.在 SqlMapConfig.xml 文件开启二级缓存 

```xml
<settings> 
    <!-- 开启全局二级缓存的支持 -->  
    <setting name="cacheEnabled" value="true"/> 
</settings> 
```

2.配置相关的 Mapper 映射文件 (因为二级缓存就是针对特定的mapper namespace的)

```xml
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE mapper     
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"     
"http://mybatis.org/dtd/mybatis-3-mapper.dtd"> 
<mapper namespace="com.itheima.dao.IUserDao">  
    <!-- 开启二级缓存的支持 -->  
    <cache />
</mapper>
```

> <cache>标签表示当前这个 mapper 映射将使用二级缓存，能否命中二级缓存就看多次查询是否属于同一个namespace。

3. 设置某个特定的查询语句不用二级缓存

```xml
<!-- 根据 id 查询 --> 
<select id="findById" resultType="user" parameterType="int" useCache="false">  
    select * from user where id = #{uid} 
</select> 
```

> 注意： 如果mapper文件中没有开启 <cache /> 仅在语句上添加useCache="true"是无法开启二级缓存的。这个属性本意是用来关闭特定查询的二级缓存。
>

###### 二级缓存注意事项

> 当我们在使用二级缓存时，所缓存的类一定要实现 java.io.Serializable 接口，这种就可以使用序列化 方式来保存对象。 

```java
public class User implements Serializable { }
```




#### Mybatis逆向工程



## Spring与Mybatis整合

1. 加入依赖

   ```xml
   <!-- mybatis-spring整合依赖，这个是最主要的一个依赖 -->
   <dependency>
       <groupId>org.mybatis</groupId>
       <artifactId>mybatis-spring</artifactId>
       <version>2.0.1</version>
   </dependency>
   
   <!-- Spring和数据源相关依赖 -->
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-context</artifactId>
       <version>5.2.0.RELEASE</version>
   </dependency>
   <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-jdbc</artifactId>
       <version>5.2.0.RELEASE</version>
   </dependency>
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
       <version>1.1.20</version>
   </dependency>
   ```

2. 在Spring bean配置文件中配置Mybatis、Spring整合bean  SqlSessionFactoryBean

   ```xml
   <!-- 配置整合bean -->
   <bean id="sessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">
       <!-- 数据源是必要参数 -->
       <property name="dataSource" ref="dataSource" />
       <!-- Mybatis核心配置文件其实大多数情况下都可以省略，通过指定属性可以间接设置核心配置文件中的参数 -->
       <!--<property name="configLocation" value="mybatis_conf.xml" />-->
   
       <!-- 省略mybatis核心配置文件后，可以通过类似下面这些特定属性，设置mybatis参数 -->
       <property name="typeAliasesPackage" value="com.lanou3g.mybatis.spring.bean" />
       <property name="mapperLocations" value="classpath:mapper/*.xml" />
   </bean>
   ```

3. 在Spring bean配置文件中开启Mybatis Mapper扫描

   1. 需要使用mybatis schema

      配置方法，在bean配置文件的头部添加

      ```xml
      <beans xmlns="http://www.springframework.org/schema/beans"
             xmlns:context="http://www.springframework.org/schema/context"
             xmlns:mybatis="http://mybatis.org/schema/mybatis-spring"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.springframework.org/schema/beans
             http://www.springframework.org/schema/beans/spring-beans.xsd
              http://www.springframework.org/schema/context
             http://www.springframework.org/schema/context/spring-context.xsd
              http://mybatis.org/schema/mybatis-spring
              http://mybatis.org/schema/mybatis-spring.xsd">
      ```

   2. 开启Mybatis Mapper扫描

      开启Mybatis Mapper扫描的作用是：告诉Mybatis要创建哪个包下接口的实现类，并以bean的方式加入到SpringIOC容器中

      ```xml
      <!-- 开启Mapper扫描，Mybatis会创建将此包下的接口的实现类，并以bean的方式加入到SpringIOC容器中 -->
      <mybatis:scan base-package="com.lanou3g.mybatis.spring.mapper" />
      ```

4. 创建实体类、mapper映射文件、Mapper接口（可以通过Mybatis逆向工程直接生成）

   Mapper接口

   ```java
   @Repository	//此注解不是必须的，因为MessageMapper类的实现类是由Mybatis创建并放到ioc容器中的，不是由Spring来创建的。
   public interface MessageMapper {
       int insert(Message record);
       List<Message> selectAll();
   }
   ```

   实体类、映射文件 略

5. 将Mapper接口用Spring自动注入的方式注入到需要的地方使用

   MessageService.java

   ```java
   @Service
   public class MessageService {
       @Autowired
       MessageMapper messageMapper;
       public List<Message> queryAllMessage() {
           return messageMapper.selectAll();
       }
   }
   ```

   
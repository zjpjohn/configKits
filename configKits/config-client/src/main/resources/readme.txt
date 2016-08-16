热配使用配置：
 1. 引入 spring namespace:
     xmlns:config="http://www.springframework.org/schema/config"

 2. 添加schema location:
     http://www.springframework.org/schema/config
     http://www.springframework.org/schema/config/spring-config-0.0.1.xsd

 3. spring xml配置
    <!-- 开启config注解配置-->
    <config:annotation-driven/>
    <!-- 配置zookeeper配置中心-->
    <config:config zkUrl="" appName="" env="" charset=""/>
    zkUrl ：zookeeper地址
    appName:应用名称
    env:应用环境 dev、beta、prod环境
    charset:字符编码 默认utf-8

 4. 程序应用-只支持String、Map类型
    4.1 String 类型
    @Config("xxx.switch")
    private String switch;

    4.2 Map<String,String>类型
    @Config("config.properties")
    private Map<String,String> hotConfig;

热配总结：
   主要解决分布式应用系统中分散的配置，利用zookeeper进行集中管理
   应用场景：1.动态配置
           2.线上开关切换

原理：
   通过监听zookeeper节点数据变化，来同步数据内容
   结合spring，完全基于注解的方式，使用简单
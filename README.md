tkhoon Framework



tkhoon是我自己在github上发布的一个自己做的轻量级WEB框架。
项目地址是：https://github.com/thinkhoon/tkhoon。
tkhoon框架具有如下功能： <br />
1 一个轻量级的IOC框架； <br />
2 一个轻量级的AOP框架； <br />
3 一个轻量级的ORM框架； <br />
4 提供了一个轻量级的基于Servlet3.0的可扩展的MVC框架。 <br />
技术要点： <br />
1 IOC框架通过扫描目录下带有@Bean注解的类从而形成一个beanMap，再对beanMap中需要进行依赖注入的对象进行依赖注入； <br />
2 AOP框架通过扫描目录下带有@Aspect注解的类并且查看注解被赋予的包名和类名类确定哪些类需要被生成代理类，然后通过cglib生成相应的代理类，最后替换掉beanMap中原有的类；
 在实现AOP特性的同时还实现了事务的特性，通过threadLocal技术和代理技术实现了事务特性 <br />
3 ORM框架通过扫描目录下带有@Entity注解的类，形成一个entityMap,结合apache.commons包下的dbutils就能使用户传入sql语句及想要的结果类型就能得到转换号的对象，形成orm功能。 <br />
4 MVC框架通过扫描目录下带有@Action注解的类得到所有的controller类，根据注解中的属性确定对应的要匹配的url路径。当有请求到来时,会有专门负责分发请求的Servlet类根据正则匹配对请求进行分发。

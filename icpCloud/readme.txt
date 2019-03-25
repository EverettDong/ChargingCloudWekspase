下面是icp的端口配置信息：

====================port list
25000 : preGateWay  前置网关
26000 : register-1 (peer1)
26001 : register-2 (used for HA mode) (peer2)
26010 : gateWay
26020 : configServer
26030 : platform-ui
26040 : hall-ui
26050 : system
26060 : resource
26070 : crm
26080 : billing
26090 : collect
26100 : singleton
26110 : dashboard

====================manage port list
25100 : preGateWay  前置网关
27000 : register-1
27001 : register-2 (used for HA mode)
27010 : gateWay
27020 : configServer
27030 : platform-ui
27040 : hall-ui
27050 : system
27060 : resource
27070 : crm
27080 : billing
27090 : collect
27100 : singleton
27110 : dashboard


【备注】
除gateWay,platform-ui,hall-ui的端口能被外部访问外，其他端口都不能

====================添加peer1,peer2地址映射
windows系统如何修改host方法
参见：https://zhidao.baidu.com/question/551591555.html
修改 c:/windows/system32/drivers/etc/hosts文件
【备注】linux, 在/etc/hosts里进行修改
127.0.0.1 peer1
127.0.0.1 peer2



====================spring cache的使用方法
https://segmentfault.com/a/1190000012490895
@Cacheable可加在需要缓存的业务方法上，比如get,find,query等
for example:
//给出了通过 SpEL表达式生成key 的实例
 @Cacheable(cacheNames="books", key="#isbn")
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

 @Cacheable(cacheNames="books", key="#isbn.rawNumber")
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)

 @Cacheable(cacheNames="books", key="T(someType).hash(#isbn)")
 public Book findBook(ISBN isbn, boolean checkWarehouse, boolean includeUsed)


 ===================修改文件夹文件的lock属性
 sudo chflags -R nouchg [文件夹名]


 ===================mac 检查service状态
 通过brew services 命令，eg.,
  brew services list | grep rabbitmq

brew info rabbitmq查看信息

====================maven 对多个项目进行打包编译
mvn -DskipTests clean package -pl [子目录1],[子目录2]
#用-pl参数

=====================如何使用gitblit建立版本服务器
参见：https://wenku.baidu.com/view/09e9c08f4128915f804d2b160b4e767f5bcf8070.html
服务器地址： http://10.3.10.169:16666 or https://103.10.169:16667


=====================如何节约机器资源
1）开发时不必自己启注册服务register和配置服务configServer,这两个微服务都放到一个地方，让大家
公用。 比如10.3.10.166或10.3.10.169. 只要将本机hosts文件的peer1和peer2指过去即可
2）接着修改微服务里的bootstrap.yml的spring.cloud.config.name属性名，比如改为cc-zjg,然后
将cc-config.properties复制一份为cc-zjg-config.properties放到configServer所在机器即可
#目前cc-config.properties放在我们自己的git里，大家各自的也放到git里
*register和configServer配在了169上,工作目录在/root/icp下
3）因为公用注册服务，所以各自修改各自的微服务的名称以便区别，即spring.application.name。比如
icp-platform-ui就改为icp-platform-ui-zjg.这个属性在application.yml里。大家改完这个文件，不用commit. 同时
将刚才复制的cc-zjg-config.properties里的icp.platform-ui.url的值也改为icp-platform-ui-zjg。
【备注】测试人员就关注bootstrap.yml和cc-config.properties这两个文件即可
通过上面操作，就能区分出各自的微服务，本机不用启动很多服务，一般就启动ui和相关微服务即可。


======================服务间的消息驱动开发
参考如下两个链接：
http://blog.didispace.com/spring-cloud-starter-dalston-7-3/

https://zhuanlan.zhihu.com/p/33484178

=======================开发注意事项
* 关键页面前后端校验，防止渗透性攻击
* 如何使用spring-cache, 参考platformUi的CacheUtil
* 何时使用facade

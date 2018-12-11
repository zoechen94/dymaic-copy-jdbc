# `springboot 动态切换数据源 +  跨服务器复制表`

首先在application.properties里面有如下的配置\
**`系统主要访问的数据库，从别的库复制到该数据库`**

spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

spring.datasource.url=jdbc:mysql://localhost:3306/db1?useUnicode=true&characterEncoding=utf-8

spring.datasource.username=root

spring.datasource.password=root2018

spring.datasource.driver-class-name=com.mysql.jdbc.Driver


`**这个是有多少个数据源，用逗号拼起来**` 
custom.datasource.names=ds1,ds2,ds3

`**根据上面拼接的数据库名字，复制 custom.datasource.xxx.type...等5个参数**`

custom.datasource.ds1.type=com.alibaba.druid.pool.DruidDataSource

custom.datasource.ds1.driver-class-name=com.mysql.jdbc.Driver

custom.datasource.ds1.url=jdbc:mysql://192.168.1.87:3306/db2?useUnicode=true&characterEncoding=utf-8

custom.datasource.ds1.username=root

custom.datasource.ds1.password=root@2017


`**oracle的时候阿里巴巴的数据连接池不支持，直接去掉**`

custom.datasource.ds3.driver-class-name=oracle.jdbc.driver.OracleDriver

custom.datasource.ds3.url=jdbc:oracle:thin:@192.168.1.112:1521:ORCL

custom.datasource.ds3.username=werry

custom.datasource.ds3.password=123456

### `注意上面的mysql数据库在配置的时候一定要有 ?useUnicode=true&characterEncoding=utf-8 `
 `1 解决中文 `?` 问题 `  
 `2 程序里会从字符串里取库名` 
 
# `How to Use —— 动态切换数据源`

可以在方法上使用@TargetDataSource(name = "ds3")

这个 name 对应的参数是配置的数据库别名

如果是ds3那现在就访问的oracle的192.168.1.112 那个数据库

### `那如果方法里面需要动态的访问超过1个库怎么办呢？`
 在代码上方先切换数据源： 
 DynamicDataSourceContextHolder.setDataSourceType("ds3");
 
 里面的参数和注解类TargetDataSource一致
 
## ` 如果以上两个都没有，默认主数据源。`


# `How to Use —— 利用接口复制表`

**正常的不需要复制操作的接口忽略此步骤**

以下是一个接口示例，必要传参 _database_ 和 _table_ ；

database的值依然是数据库的别名 ds3  

table是待复制表的名，即 ds3库下有表*T_JZ_SWASJRYPCBXX* ，执行接口，主数据库就会生成表*T_JZ_SWASJRYPCBXX*

并且数据全量拷贝过来。

  ` @ApiOperation("得到Oracle里的数据")
    @GetMapping("/oracle")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "database",defaultValue = "ds3",value = "数据库",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "table",defaultValue = "T_JZ_SWASJRYPCBXX",value = "表",dataType = "String",paramType = "query")
    })
    public ResponseBean getOracle(String database,String table){
        return
                ResponseBean.success(testService.getDs1AndDs3());
    }`



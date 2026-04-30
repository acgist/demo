# 配置中心说明
* 客户端通过bootstrap.properties配置配置中心
* 客户端通过spring.cloud.config.name+spring.profiles.active进行获取配置

#### 通用配置
|配置|说明|依赖|必选|
|:-|:-|:-|:-|
|${spring.application.name}|系统配置|-|√|
|system|切面配置|-|√|
|web|web配置|-|√|
|eureka|eureka配置|-|√|
|logger|日志配置|-|√|
|freemarker|freemarker配置|-|√|
|data|数据源配置|-|×|
|c3p0|c3p0连接池配置|data|×|
|redis|redis配置|-|×|
|feign|feign配置|-|×|
|stream|消息配置|-|×|
|bus|bus配置|stream|×|
|zipkin|zipkin配置|stream|×|
|hystrix|hystrix配置|stream|×|

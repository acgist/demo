# 网关模块

#### 项目结构
|目录|描述|
|:-|:-|
|api-gateway-www|前台网站网关|
|api-gateway-admin|后台网站网关|
|api-gateway-service|接口服务网关|

#### 安全说明
* 网关关闭csrf防御。
* 网关验证端点访问的IP地址。
* 网关屏蔽端点、服务、服务熔断逻辑的路由规则。
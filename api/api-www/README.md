# 网站模块
* 使用interceptor防止csrf攻击，而不在zuul实现。（zuul session信息无法传送到后台服务。）
* 使用spring session + redis实现session共享。
* 静态资源服务器，生产环境可以使用独立的服务器，而不是用微服务。资源均可以跨域访问（CORS）。
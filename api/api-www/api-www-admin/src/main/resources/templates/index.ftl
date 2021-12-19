<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>API-ADMIN</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body class="layui-layout-body">
		<div class="layui-layout layui-layout-admin">
			<div class="layui-header">
				<div class="layui-logo">API-ADMIN</div>
				<ul class="layui-nav layui-layout-right">
					<li class="layui-nav-item">
						<a href="/admin/info" target="api-content">
							<img src="${staticBase}/resources/images/logo.png" class="layui-nav-img" />
							${adminDetails.name}
						</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="/logout">退出</a>
							</dd>
						</dl>
					</li>
				</ul>
			</div>
			<div class="layui-side layui-bg-black">
				<div class="layui-side-scroll">
					<ul class="layui-nav layui-nav-tree" lay-filter="test">
						<@autho name="系统管理">
						<li class="layui-nav-item">
							<a>系统管理</a>
							<dl class="layui-nav-child">
								<@autho pattern="/admin/list">
								<dd>
									<a href="/admin/list" target="api-content">系统用户</a>
								</dd>
								</@autho>
								<@autho pattern="/role/list">
								<dd>
									<a href="/role/list" target="api-content">系统角色</a>
								</dd>
								</@autho>
								<@autho pattern="/permission/tree">
								<dd>
									<a href="/permission/tree" target="api-content">系统权限</a>
								</dd>
								</@autho>
								<@autho pattern="/system">
								<dd>
									<a href="/system" target="api-content">系统管理</a>
								</dd>
								</@autho>
							</dl>
						</li>
						</@autho>
						<@autho name="服务管理">
						<li class="layui-nav-item">
							<a>服务管理</a>
							<dl class="layui-nav-child">
								<@autho pattern="/discovery">
								<dd>
									<a href="/discovery" target="api-content">服务列表</a>
								</dd>
								</@autho>
								<@autho pattern="/hystrix/">
								<dd>
									<a href="/hystrix/" target="api-content">服务监控</a>
								</dd>
								</@autho>
								<@autho pattern="/zipkin/">
								<dd>
									<a href="/zipkin/" target="api-content">链路监控</a>
								</dd>
								</@autho>
							</dl>
						</li>
						</@autho>
						<@autho name="用户管理">
						<li class="layui-nav-item">
							<a href="/user/list" target="api-content">用户管理</a>
						</li>
						</@autho>
						<@autho name="订单管理">
						<li class="layui-nav-item">
							<a href="/order/list" target="api-content">订单管理</a>
						</li>
						</@autho>
					</ul>
				</div>
			</div>
			<div class="layui-body">
				<iframe name="api-content" width="100%" height="100%" src="/admin/info"></iframe>
			</div>
			<div class="layui-footer">
				<p>Copyright <span>&copy;</span> 2013-${.now?string("yyyy")} ACGIST.COM. All Rights Reserved.</p>
			</div>
		</div>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var element = layui.element;
		});
		</script>
	</body>
</html>
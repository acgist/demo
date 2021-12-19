<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>系统面板</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-background iframe-content">
			<div class="layui-row layui-col-space15">
				<div class="layui-col-md6">
					<div class="layui-card">
						<div class="layui-card-header">当前用户</div>
						<div class="layui-card-body">
							${adminDetails.name}
						</div>
					</div>
				</div>
				<div class="layui-col-md6">
					<div class="layui-card">
						<div class="layui-card-header">系统状态</div>
						<div class="layui-card-body">
							running
						</div>
					</div>
				</div>
				<div class="layui-col-md12">
					<div class="layui-card">
						<div class="layui-card-header">最近操作</div>
						<div class="layui-card-body">
							${adminDetails.name}
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
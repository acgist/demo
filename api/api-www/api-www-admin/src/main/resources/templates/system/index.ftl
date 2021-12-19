<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>系统管理</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content cache">
			<fieldset class="layui-elem-field">
				<legend>功能按钮</legend>
				<div>
					<@autho pattern="/mail/test">
					<button class="layui-btn" id="mail">邮件测试</button>
					</@autho>
				</div>
			</fieldset>
			<fieldset class="layui-elem-field">
				<legend>刷新缓存</legend>
				<div>
					<@autho pattern="/cache/permission/roles">
					<button class="layui-btn" id="permissionRoles">权限角色映射</button>
					</@autho>
				</div>
			</fieldset>
		</div>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
			$('#mail').on('click', function() {
				layer.prompt({
					formType : 0, // 0-默认/1-密码/2-多行文本
					title : "请输入邮箱"
				}, function(value, index, elem){
					layui.jquery.get("/mail/test?to=" + value, function() {
						layer.alert('测试邮件发送成功', {icon : 1});
					});
					layer.close(index);
				});
			});
			$('#permissionRoles').on('click', function() {
				layui.jquery.get("/cache/permission/roles", function() {
					layer.alert('缓存已刷新', {icon : 1});
				});
			});
		});
		</script>
	</body>
</html>
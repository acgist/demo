<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>用户角色</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<blockquote class="layui-elem-quote layui-text">
			用户名称 - ${entity.name}
		</blockquote>
		<div class="iframe-content">
			<form class="layui-form" lay-filter="role" id="data-form">
				<input type="hidden" name="aid" value="${entity.id}" />
				<div class="admin-roles" id="roles">
					<div class="layui-form-item">
						<label class="layui-form-label">选择角色</label>
						<div class="layui-input-block">
							<#list roles as role>
							<input type="checkbox" name="rids" ${entity.hasRole(role)?string('checked', '')} value="${role.id}" title="${role.name}" />
							</#list>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-input-block">
						<button class="layui-btn" lay-submit="" lay-filter="submit">保存</button>
					</div>
				</div>
			</form>
		</div>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
			var form = layui.form;
			form.render();
			form.on('submit(submit)', function(data) {
				layui.jquery.post("/admin/role", $('#data-form').serialize(), function(data) {
					parent.layer.alert("更新成功", {icon : 1});
				});
				return false;
			});
		});
		</script>
	</body>
</html>
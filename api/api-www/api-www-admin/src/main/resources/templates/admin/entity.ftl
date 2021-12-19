<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>系统用户</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<form class="layui-form" lay-filter="permission">
				<input type="hidden" name="id" value="${entity.id}" />
				<div class="layui-form-item">
					<label class="layui-form-label">名称</label>
					<div class="layui-input-block">
						<input type="text" name="name" value="${entity.name}" ${entity???string('readonly', '')} lay-verify="name" autocomplete="off" placeholder="用户名称" class="layui-input" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">密码</label>
					<div class="layui-input-block">
						<input type="text" name="password" value="${entity.password}" lay-verify="password" autocomplete="off" placeholder="用户密码" class="layui-input" />
					</div>
				</div>
				<div class="layui-form-item layui-form-text">
					<label class="layui-form-label">描述</label>
					<div class="layui-input-block">
						<textarea name="memo" placeholder="用户描述" class="layui-textarea">${entity.memo}</textarea>
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
			var form = layui.form;
			form.on('submit(submit)', function(data) {
				layui.jquery.post("${entity???string("/admin/update", "/admin/submit")}", data.field, function(data) {
					if(data.code == "0000") {
						parent.layui.table.reload('data-table-model');
						parent.layer.closeAll();
						parent.layer.alert("${entity???string("修改", "添加")}成功", {icon : 1});
					} else {
						parent.layer.alert(data.message);
					}
				});
				return false;
			});
			form.verify({
				name : [/^[a-zA-Z]{4,}$/, '用户账号不少于四个英文字符'],
				password : [/^[a-zA-Z0-9]{6,}$/, '用户密码不少于六位数字或英文字符']
			});
		});
		</script>
	</body>
</html>
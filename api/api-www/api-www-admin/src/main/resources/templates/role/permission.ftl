<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>角色权限</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<blockquote class="layui-elem-quote layui-text">
			角色名称 - ${entity.name}
		</blockquote>
		<div class="iframe-content">
			<form class="layui-form" lay-filter="permission" id="data-form">
				<input type="hidden" name="rid" value="${entity.id}" />
				<div class="layui-form-item">
					<label class="layui-form-label">选择权限</label>
					<div class="role-permissions" id="permissions"></div>
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
			var root = $("#permissions");
			var permissions = ${permissions};
			function tree(root, element) {
				var id = element.entity ? element.entity.id : '';
				var checked = element.checked ? 'checked' : '';
				var disabled = element.entity ? '' : 'disabled';
				var children = $('\
					<div class="layui-input-block">\
						<input type="checkbox" name="pids" value="' + id + '" ' + checked + disabled + ' title="' + element.name + '" />\
					</div>\
				');
				if(element.children) {
					root.append(children);
					for(var index in element.children) {
						tree(children, element.children[index]);
					}
				} else {
					root.append(children);
				}
			}
			tree(root, permissions);
			form.render();
			form.on('submit(submit)', function(data) {
				layui.jquery.post("/role/permission", $('#data-form').serialize(), function(data) {
					parent.layer.alert("更新成功", {icon : 1});
				});
				return false;
			});
		});
		</script>
	</body>
</html>
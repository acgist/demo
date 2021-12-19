<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>系统权限</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<blockquote class="layui-elem-quote layui-text">
			添加：添加新节点；更新：更新当前节点；删除：删除当前节点
		</blockquote>
		<div class="iframe-background iframe-content">
			<div class="layui-row layui-col-space12">
				<div class="layui-col-md2">
					<div class="layui-card">
						<div class="layui-card-header">权限管理</div>
						<div class="layui-card-body">
							<ul id="permissions"></ul>
						</div>
					</div>
				</div>
				<div class="layui-col-md10">
					<div class="layui-card">
						<div class="layui-card-header">权限信息</div>
						<div class="layui-card-body">
							<form class="layui-form" lay-filter="permission">
								<input type="hidden" name="id" />
								<input type="hidden" name="parent" />
								<div class="layui-form-item">
									<label class="layui-form-label">名称</label>
									<div class="layui-input-block">
										<input type="text" name="name" lay-verify="name" autocomplete="off" placeholder="权限名称" class="layui-input" />
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">地址</label>
									<div class="layui-input-block">
										<input type="text" name="pattern" lay-verify="pattern" placeholder="权限地址" autocomplete="off" class="layui-input" />
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">排序</label>
									<div class="layui-input-block">
										<input type="number" name="sort" lay-verify="sort" placeholder="权限排序" autocomplete="off" class="layui-input" />
									</div>
								</div>
								<div class="layui-form-item layui-form-text">
									<label class="layui-form-label">描述</label>
									<div class="layui-input-block">
										<textarea name="memo" placeholder="权限描述" class="layui-textarea"></textarea>
									</div>
								</div>
								<div class="layui-form-item">
									<div class="layui-input-block">
										<@autho pattern="/permission/submit">
										<button class="layui-btn" lay-submit="" lay-filter="submit">添加</button>
										</@autho>
										<@autho pattern="/permission/update">
										<button class="layui-btn layui-btn-normal" lay-submit="" lay-filter="update">更新</button>
										</@autho>
										<@autho pattern="/permission/delete">
										<button class="layui-btn layui-btn-danger" lay-submit="" lay-filter="delete">删除</button>
										</@autho>
										<@autho pattern="/permission/submit">
										<button type="reset" class="layui-btn layui-btn-primary">重置</button>
										</@autho>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		layui.use(['tree', 'form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
			var form = layui.form;
			function load() {
				$("#permissions").html(""); // 清空菜单
				layui.jquery.post("/permission/tree", function(options) {
					layui.tree({
						"elem" : "#permissions",
						"nodes" : options,
						"spread" : true,
						click : function(node) {
							form.val('permission', node.entity);
						}
					});
				});
			}
			load();
			form.on('submit(submit)', function(data) {
				layui.jquery.post("/permission/submit", data.field, function(data) {
					if(data.code == "0000") {
						load();
						layer.alert("添加成功", {icon : 1});
					} else {
						parent.layer.alert(data.message);
					}
				});
				return false;
			});
			form.on('submit(update)', function(data) {
				layui.jquery.post("/permission/update", data.field, function(data) {
					if(data.code == "0000") {
						load();
						layer.alert("更新成功", {icon : 1});
					} else {
						parent.layer.alert(data.message);
					}
				});
				return false;
			});
			form.on('submit(delete)', function(data) {
				if(data.field.id == "") {
					layer.alert("请选择需要删除的权限", {icon : 2});
					return false;
				}
				layui.jquery.post("/permission/delete", {id : data.field.id}, function(data) {
					if(data.code == "0000") {
						load();
						layer.alert("删除成功", {icon : 1});
					} else {
						parent.layer.alert(data.message);
					}
				});
				return false;
			});
			form.verify({
				name : function(value) {
					if(value.length < 2) {
						return '权限名称不能少于两个字符';
					}
				},
				sort : [/^\d{4}$/, '四位排序数字']
			});
		});
		</script>
	</body>
</html>
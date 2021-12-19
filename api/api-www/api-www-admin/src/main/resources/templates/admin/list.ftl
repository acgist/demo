<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>系统用户</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<div class="data-filter">
				<div class="layui-inline">
					<input class="layui-input" name="name" id="nameInput" placeholder="账号" autocomplete="off" />
				</div>
				<button class="layui-btn layui-btn-normal" id="data-search">搜索</button>
				<@autho pattern="/admin/submit">
				<button class="layui-btn" id="create">新建</button>
				</@autho>
			</div>
			<table class="layui-table" id="data-table" lay-filter="data-table"></table>
		</div>
		<script type="text/html" id="handle">
			<@autho pattern="/admin/update">
			<a class="layui-btn layui-btn-sm" lay-event="update">修改</a>
			</@autho>
			<@autho pattern="/admin/role">
			<a class="layui-btn layui-btn-sm layui-btn-normal" lay-event="role">角色</a>
			</@autho>
			<@autho pattern="/admin/delete">
			<a class="layui-btn layui-btn-sm layui-btn-danger" lay-event="delete">删除</a>
			</@autho>
		</script>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
			var table = layui.table;
			table.render({
				method : 'POST',
				elem : '#data-table',
				url : '/admin/list',
				cols : [[
					{checkbox : true, fixed : true},
					{title : '用户账号', field : 'name', width : 200, sort : true},
					{title : '用户描述', field : 'memo', width : 200},
					{title : '操作', toolbar : '#handle'}
				]],
				id : 'data-table-model',
				page : true
			});
			$('#data-search').on('click', function() {
				var nameInput = $('#nameInput');
				table.reload('data-table-model', {
					page : {curr : 1},
					where : {
						name : nameInput.val()
					}
				});
			});
			$("#create").on('click', function() {
				layer.open({
					type : 2,
					title : '添加用户',
					area : ['800px', '400px'],
					content : '/admin/submit'
				});
			});
			table.on('tool(data-table)', function(obj) {
				var data = obj.data;
				if(obj.event == 'delete') {
					layer.confirm('确定删除系统用户（' + data.name + '）吗？', {icon : 3, title : '提示'}, function(index) {
						layer.closeAll();
						layer.open({type : 3});
						layui.jquery.post("/admin/delete", {
							id : data.id
						}, function(message) {
							layer.closeAll();
							table.reload('data-table-model');
							layer.alert('系统用户删除成功', {icon : 1});
						});
					});
				} else if(obj.event == 'update') {
					layer.open({
						type : 2,
						title : '修改用户',
						area : ['800px', '400px'],
						content : '/admin/update?id=' + data.id
					});
				} else if(obj.event == 'role') {
					layer.open({
						type : 2,
						title : '设置角色',
						area : ['800px', '400px'],
						content : '/admin/role?id=' + data.id
					});
				}
			});
		});
		</script>
	</body>
</html>
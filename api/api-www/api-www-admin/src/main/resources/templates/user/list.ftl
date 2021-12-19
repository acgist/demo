<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>用户管理</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<div class="data-filter">
				<div class="layui-inline">
					<input class="layui-input" name="name" id="nameInput" placeholder="账号" autocomplete="off" />
				</div>
				<button class="layui-btn layui-btn-normal" id="data-search">搜索</button>
				<@autho pattern="/user/submit">
				<button class="layui-btn" id="create">新建</button>
				</@autho>
			</div>
			<table class="layui-table" id="data-table" lay-filter="data-table"></table>
		</div>
		<script type="text/html" id="handle">
			<@autho pattern="/user/update">
			<a class="layui-btn layui-btn-sm" lay-event="update">修改</a>
			</@autho>
			<@autho pattern="/user/cert/download">
			<a class="layui-btn layui-btn-sm" lay-event="download">下载证书</a>
			</@autho>
			<@autho pattern="/user/delete">
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
				url : '/user/list',
				cols : [[
					{checkbox : true, fixed : true},
					{title : '用户账号', field : 'name', width : 200, sort : true},
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
					area : ['800px', '500px'],
					content : '/user/submit'
				});
			});
			table.on('tool(data-table)', function(obj) {
				var data = obj.data;
				if(obj.event == 'delete') {
					layer.confirm('确定删除用户（' + data.name + '）吗？', {icon : 3, title : '提示'}, function(index) {
						layer.closeAll();
						layer.open({type : 3});
						layui.jquery.post("/user/delete", {
							id : data.id
						}, function(message) {
							layer.closeAll();
							table.reload('data-table-model');
							layer.alert('用户删除成功', {icon : 1});
						});
					});
				} else if(obj.event == 'download') {
					window.open("/user/cert/download?id=" + data.id);
				} else if(obj.event == 'update') {
					layer.open({
						type : 2,
						title : '修改用户',
						area : ['800px', '500px'],
						content : '/user/update?id=' + data.id
					});
				}
			});
		});
		</script>
	</body>
</html>
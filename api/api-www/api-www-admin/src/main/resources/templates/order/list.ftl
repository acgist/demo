<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>订单管理</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<div class="data-filter">
				<div class="layui-inline">
					<input class="layui-input" name="orderId" id="orderIdInput" placeholder="订单号" autocomplete="off" />
				</div>
				<button class="layui-btn layui-btn-normal" id="data-search">搜索</button>
			</div>
			<table class="layui-table" id="data-table" lay-filter="data-table"></table>
		</div>
		<script type="text/html" id="handle">
			<@autho pattern="/order/view">
			<a class="layui-btn layui-btn-sm" lay-event="view">查看</a>
			</@autho>
		</script>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
			var table = layui.table;
			table.render({
				method : 'POST',
				elem : '#data-table',
				url : '/order/list',
				cols : [[
					{checkbox : true, fixed : true},
					{title : '订单ID', field : 'orderId', width : 200, sort : true},
					{title : '操作', toolbar : '#handle'}
				]],
				id : 'data-table-model',
				page : true
			});
			$('#data-search').on('click', function() {
				var orderIdInput = $('#orderIdInput');
				table.reload('data-table-model', {
					page : {curr : 1},
					where : {
						orderId : orderIdInput.val()
					}
				});
			});
			table.on('tool(data-table)', function(obj) {
				var data = obj.data;
				if(obj.event == 'view') {
					layer.open({
						type : 2,
						title : '查看订单',
						area : ['800px', '400px'],
						content : '/order/view?id=' + data.id
					});
				}
			});
		});
		</script>
	</body>
</html>
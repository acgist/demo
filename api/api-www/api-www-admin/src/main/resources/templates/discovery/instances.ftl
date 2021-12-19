<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>服务实例</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<table class="layui-table" lay-data="{url:'/discovery/${serviceId}',method:'POST'}" lay-filter="data-table">
				<thead>
					<tr>
						<th lay-data="{field:'serviceId',width:200,sort:true}">服务名称</th>
						<th lay-data="{field:'uri',width:200,sort:true}">实例地址</th>
						<th lay-data="{toolbar:'#handle'}">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<script type="text/html" id="handle">
			<@autho pattern="/endpoint/shutdown">
			{{#  layui.each(d.endpoints, function(index, info) { }}
			{{#  if(info.key == 'actuator_shutdown') { }}
			<a class="layui-btn layui-btn-sm layui-btn-danger" lay-event="shutdown">{{ info.name }}</a>
			{{#  }; }}
			{{#  }); }}
			</@autho>
		</script>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var table = layui.table;
			table.on('tool(data-table)', function(obj) {
				var data = obj.data;
				if(obj.event == 'shutdown') {
					layer.confirm('确定关闭实例（' + data.uri + '）吗？', {icon : 3, title : '提示'}, function(index) {
						layer.closeAll();
						layer.open({type : 3});
						layui.jquery.post("/endpoint/shutdown", {
							uri : data.uri
						}, function(message) {
							layer.closeAll();
							layer.alert('实例已关闭', {icon : 1});
						});
					});
				}
			});
		});
		</script>
	</body>
</html>
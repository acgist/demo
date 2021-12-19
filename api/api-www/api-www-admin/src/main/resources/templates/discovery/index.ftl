<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>服务列表</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<table class="layui-table" lay-data="{url:'/discovery',method:'POST'}" lay-filter="data-table">
				<thead>
					<tr>
						<th lay-data="{field:'serviceId',width:200,sort:true}">服务名称</th>
						<th lay-data="{field:'instanceCount',width:100}">服务数量</th>
						<th lay-data="{toolbar:'#handle'}">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<script type="text/html" id="handle">
			<@autho pattern="/discovery/**">
			<a class="layui-btn layui-btn-sm" href="/discovery/{{d.serviceId}}">查看实例</a>
			</@autho>
			<@autho pattern="/endpoint/bus/refresh">
			{{#  if(d.instances.length > 0) { }}
			{{#  layui.each(d.instances[0].endpointInfos, function(index, info) { }}
			{{#  if(info.key == 'actuator_bus_refresh') { }}
			<a class="layui-btn layui-btn-sm layui-btn-danger" lay-event="bus-refresh">{{ info.name }}</a>
			{{#  }; }}
			{{#  }); }}
			{{#  }; }}
			</@autho>
		</script>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var table = layui.table;
			table.on('tool(data-table)', function(obj) {
				var data = obj.data;
				if(obj.event == 'bus-refresh') {
					layer.confirm('确定刷新服务（' + data.serviceId + '）下所有实例吗？', {icon : 3, title : '提示'}, function(index) {
						layer.closeAll();
						layer.open({type : 3});
						layui.jquery.post("/endpoint/bus/refresh", {
							serviceId : data.serviceId,
							uri : data.instances[0].uri
						}, function(message) {
							layer.closeAll();
							layer.alert('服务已刷新', {icon : 1});
						});
					});
				}
			});
		});
		</script>
	</body>
</html>
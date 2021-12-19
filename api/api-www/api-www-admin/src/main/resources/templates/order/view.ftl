<!DOCTYPE html>
<html>
	<head>
		<#include "/admin/head.ftl" >
		<title>订单查看</title>
		<#include "/admin/resources.ftl" >
	</head>
	<body>
		<div class="iframe-content">
			<form class="layui-form" lay-filter="order">
				<input type="hidden" name="id" value="${entity.id}" />
				<div class="layui-form-item">
					<label class="layui-form-label">订单号</label>
					<div class="layui-input-block">
						<input type="text" name="orderId" value="${entity.orderId}" readonly lay-verify="orderId" autocomplete="off" placeholder="订单号" class="layui-input" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-block">
						<input type="text" name="createDate" value="${entity.createDate?string("yyyy-MM-dd HH:mm")}" readonly lay-verify="createDate" autocomplete="off" placeholder="创建时间" class="layui-input" />
					</div>
				</div>
			</form>
		</div>
		<script type="text/javascript">
		layui.use(['form', 'layer', 'table', 'jquery', 'element'], function() {
			var $ = layui.$;
		});
		</script>
	</body>
</html>
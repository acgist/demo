<!DOCTYPE HTML>
<html>
	<head>
		<title>交易页面</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="交易页面" />
		<meta name="description" content="交易页面" />
		
		<link rel="shortcut icon" href="/favicon.ico">
		<#include "/include/resources.ftl">
	</head>

	<body>
	<#include "/include/header.ftl">
	<div class="container main">
		<div class="order">
			<form method="post" action="/order">
				<input name="token" type="hidden" value="${token}" />
				<div class="form-group">
					<input tabindex="1" required="required" name="orderId" type="text" class="form-control" placeholder="订单号" />
				</div>
				<div class="form-group text-center">
					<button type="submit" class="btn btn-primary">提交订单</button>
				</div>
			</form>
			<#if message??>
			<div>
				<p>交易结果：${message.code}</p>
				<p>交易信息：${message.message}</p>
				<p>交易订单ID：${message.entity.id}</p>
				<p>交易订单号：${message.entity.orderId}</p>
			</div>
			</#if>
		</div>
	</div>
	<#include "/include/footer.ftl">
	</body>
</html>
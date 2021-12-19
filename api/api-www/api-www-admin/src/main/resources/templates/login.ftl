<!DOCTYPE HTML>
<html>
	<head>
		<title>后台登陆</title>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width" />
		<meta name="keywords" content="后台登陆" />
		<meta name="description" content="后台登陆" />
		
		<link rel="shortcut icon" href="/favicon.ico" />
		<link href="${staticBase}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${staticBase}/resources/js/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="${staticBase}/resources/js/bootstrap.min.js"></script>
		<style type="text/css">
			*{margin:0;padding:0;border:none;outline:none;}
			html,body{background:#f5f5f5;}
			a:hover{text-decoration:none;}
			.admin-login{margin:4rem auto;}
			.login{position:relative;width:100%;max-width:400px;padding:15px;margin:auto;}
			.login .checkbox{font-weight:400;}
			.login .form-control{position:relative;box-sizing:border-box;height:auto;padding:10px;font-size:16px;}
			.login .logo{border-radius:50%;}
			.login .form-control:focus{z-index:2;}
			.login input[type="text"]{margin-bottom:-1px;border-bottom-right-radius:0;border-bottom-left-radius:0;}
			.login input[type="password"]{margin-bottom:10px;border-top-left-radius:0;border-top-right-radius:0;}
		</style>
	</head>

	<body>
		<div class="container-fluid text-center admin-login">
			<form class="login" action="/login" method="POST">
				<img class="mb-4 logo" src="${staticBase}/resources/images/logo.png" alt="admin" width="72" height="72" />
				<h1 class="h3 mb-3 font-weight-normal">后台登录</h1>
				<label for="username" class="sr-only">用户账号</label>
				<input type="text" id="username" name="username" class="form-control" placeholder="用户账号" required autofocus />
				<label for="password" class="sr-only">用户密码</label>
				<input type="password" id="password" name="password" class="form-control" placeholder="用户密码" required />
				<button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button>
			</form>
			<p>Copyright <span>&copy;</span> 2013-${.now?string("yyyy")} ACGIST.COM. All Rights Reserved.</p>
		</div>
	</body>
</html>
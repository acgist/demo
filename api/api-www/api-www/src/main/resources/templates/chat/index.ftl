<!DOCTYPE HTML>
<html>
	<head>
		<title>首页</title>
		<#include "/include/head.ftl">
		<meta name="keywords" content="首页" />
		<meta name="description" content="首页" />
		
		<link rel="shortcut icon" href="/favicon.ico" />
		<#include "/include/resources.ftl">
	</head>

	<body>
	<#include "/include/header.ftl">
	<div class="container main">
		<div class="websocket">
			<textarea id="message" class="form-control" rows="3"></textarea>
			<button id="send" class="btn btn-primary" type="button">发送</button>
		</div>
	</div>
	<#include "/include/footer.ftl">
	<script type="text/javascript">
    var webSocket = null;
    if(WebSocket) {
        webSocket = new WebSocket("ws://localhost:38010/chat.socket");
    } else {
        alert("浏览器不支持websocket");
    }
    // 连接发生错误的回调方法
    webSocket.onerror = function() {
    	console.log("打开WebSocket异常");
    };
    // 连接成功建立的回调方法
    webSocket.onopen = function(event) {
    	console.log("打开WebSocket");
    }
    // 接收到消息的回调方法
    webSocket.onmessage = function(event) {
        console.log("收到WebSocket信息：" + event.data);
        notice(event.data);
    }
    // 连接关闭的回调方法
    webSocket.onclose = function() {
    	console.log("关闭WebSocket");
    }
    // 监听窗口关闭事件
    window.onbeforeunload = function() {
    	console.log("窗口关闭WebSocket");
        webSocket.close();
    }
    $("#send").click(function() {
    	var message = $("#message").val();
    	if(message && message != '') {
	        console.log("发送WebSocket信息：" + message);
	        webSocket.send(message);
    	}
    });
    // 通知权限
	if(Notification && Notification.permission !== "granted"){
		Notification.requestPermission(function(status){
			if(Notification.permission !== status){
				Notification.permission = status;
			}
		});
	}
	// 通知
	function notice(message) {
		if(!Notification) {
			alert("你的浏览器不支持！");
			return;
		}
		var options = {
			lang : "utf-8",
			icon : "https://static.acgist.com/logo.png",
			body : message
		};
		if(Notification && Notification.permission === "granted") {
			var notify = new Notification("你收到一条通知！", options);
			notify.onshow = function(){
				console.log("你看到了通知！");
			};
			notify.onclick = function() {
				console.log("你点击了通知！");
			};
			notify.onclose = function() {
				console.log("你关闭了通知！");
			};
			notify.onerror = function() {
				console.log("发生了一点意外！");
			}
		} else {
			alert("没有获得权限!");
		}
	}
	</script>
	</body>
</html>
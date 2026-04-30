// 配置
var config = {
	"host": "127.0.0.1",
	"port": 16688,
	"socketId": null
}

// 连接
function buildConnect() {
	chrome.sockets.tcp.create({}, function(connect) {
		config.socketId = connect.socketId;
		chrome.sockets.tcp.connect(config.socketId, config.host, config.port, function(result) {
			if (result === 0) {
				console.log("连接成功");
			} else {
				console.log("连接失败");
			}
		});
	});
}

buildConnect();

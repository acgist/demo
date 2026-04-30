// 配置
var config = {
	"cleanKeyCode" : "ControlLeft", // 清除键值
	"countKeyCode" : "ControlRight", // 计数键值
	"danmukuTemplate" : "计数君*${counter.count}", // 弹幕模板
	"danmukuInputClass" : "bilibili-player-video-danmaku-input" // 弹幕输入框样式
}

// 监听content-script消息
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if(request) {
		if(request.type == "config") {
			chrome.storage.local.get({ "config" : config }, function(data) {
				console.log("加载配置：" + JSON.stringify(data));
				config = data.config;
			});
			sendResponse(config); // 发送配置
		} else {
			console.log("content-script类型错误：" + request.type);
		}
	} else {
		console.log("content-script格式错误");
	}
});

// 发送消息
function sendMessageToContentScript(message, callback) {
	chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
		chrome.tabs.sendMessage(tabs[0].id, message, function(response) {
			if(callback) {
				callback(response);
			}
		});
	});
}

// 右键菜单
chrome.contextMenus.create({
	"title" : "清除计数",
	"onclick" : function() {
		sendMessageToContentScript({ "type" : "clean" }, function(response) {
			console.log("收到响应：" + JSON.stringify(response));
		});
	}
});

// 保存配置
function persist(data) {
	config = data;
	chrome.storage.local.set({ "config" : config }, function() {
		console.log("保存配置：" + JSON.stringify(config));
	});
}
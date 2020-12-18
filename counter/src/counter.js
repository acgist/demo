// 计数君
var counter = {
	"count" : 0,
	"clean" : function() {
		this.count = 0;
	},
	"increment" : function() {
		this.count++;
	}
}

// 配置
var config = {
	"cleanKeyCode" : "ControlLeft", // 清除键值
	"countKeyCode" : "ControlRight", // 计数键值
	"danmukuTemplate" : "计数君*${counter.count}", // 弹幕模板
	"danmukuInputClass" : "bilibili-player-video-danmaku-input" // 弹幕输入框样式
}

// 获取配置
chrome.runtime.sendMessage(
	{ "type" : "config" },
	function(response) {
		if(response) {
			config = response;
		} else {
			console.log("获取配置失败");
		}
	}
);

// 监听background消息
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if(request) {
		if(request.type == "clean") {
			counter.clean(); // 清除计数
			sendResponse({ "type" : "success" });
		} else {
			console.log("background类型错误：" + request.type);
		}
	} else {
		console.log("background格式错误");
	}
});

// 监听事件
document.onkeydown = function(e) {
	// 弹幕
	var danmu = config.danmukuTemplate.replace("${counter.count}", counter.count);
	var inputs = document.getElementsByClassName(config.danmukuInputClass);
	if(!inputs || inputs.length != 1) {
		console.log("没有找到弹幕输入框样式：" + config.danmukuInputClass);
		return;
	}
	var eventCode = e.code;
	if(eventCode == config.cleanKeyCode) {
		counter.clean();
		inputs[0].value = "";
	} else if(eventCode == config.countKeyCode) {
		counter.increment();
		inputs[0].value = danmu;
	}
	return true;
}
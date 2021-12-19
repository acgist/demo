// 默认匹配规则：匹配所有规则
var allRule = "匹配规则";
// 默认配置信息
var config = {
	"search": "a",
	"rule": "匹配规则",
	"rules": {
		"匹配规则": []
	}
}

// 添加content-script监听
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if (request) {
		console.log("acgist-收到消息：%s-%o", request.type, sender);
		if (request.type == "config") {
			sendResponse(buildAcgistConfig());
		} else if (request.type == "size") {
			searchSize(request.size);
		} else {
			console.warn("acgist-收到消息错误：%o", request);
		}
	} else {
		console.warn("acgist-收到消息错误：%o", request);
	}
});

// 添加右键菜单
chrome.contextMenus.create({
	"title": "搜索",
	"onclick": function() {
		sendMessageToContentScript({ "type": "search" }, function(response) {
			console.log("acgist-右键菜单：%o", response);
		});
	}
});

// 添加右键菜单
chrome.contextMenus.create({
	"title": "关灯",
	"onclick": function() {
		sendMessageToContentScript({ "type": "video" }, function(response) {
			console.log("acgist-右键菜单：%o", response);
		});
	}
});

// 加载配置信息
chrome.storage.local.get({ "config": config }, function(data) {
	console.log("acgist-加载配置：%o", data);
	config = data.config;
});

// 保存配置信息
function persist(data) {
	config = data;
	chrome.storage.local.set({ "config": config }, function() {
		console.log("acgist-保存配置：%o", config);
	});
	// 更新页面配置
	sendMessageToContentScript({ "type": "config", "config": buildAcgistConfig() }, function(response) {
		console.log("acgist-更新配置：%o", response);
	});
}

// 发送content-script消息
function sendMessageToContentScript(message, callback) {
	chrome.tabs.query({ active: true, currentWindow: true }, function(tabs) {
		chrome.tabs.sendMessage(tabs[0].id, message, function(response) {
			if (callback) {
				callback(response);
			}
		});
	});
}

// 创建页面配置
function buildAcgistConfig() {
	var rules = []; // 返回匹配规则
	if (config.rule == allRule) {
		for (var ruleKey in config.rules) {
			for (var rule of config.rules[ruleKey]) {
				rules[rules.length] = rule;
			}
		}
	} else {
		rules = config.rules[config.rule];
	}
	return { "search": config.search, "rules": rules };
}

// 搜索结果
function searchSize(size) {
	chrome.browserAction.setBadgeText({ "text": size.toString() });
	chrome.browserAction.setBadgeBackgroundColor({ "color": [255, 0, 0, 255] });
}
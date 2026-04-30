// 默认配置
var config = {};

// 是否关灯
var switchVideo = false;
// 是否搜索
var switchSearch = false;

// 搜索样式
var searchClass = " acgist-search";
// 关灯样式
var videoClass = " acgist-video";
var videoLargeClass = " acgist-video-large";
var videoParentClass = " acgist-video-parent";

// 监听background消息
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if (request) {
		console.log("acgist-收到消息：%o", request);
		if (request.type == "search") {
			acgistSearch();
			sendResponse({ "type": "success" });
		} else if (request.type == "video") {
			acgistVideo();
			sendResponse({ "type": "success" });
		} else if (request.type == "config") {
			config = request.config;
			sendResponse({ "type": "success" });
		} else {
			console.warn("acgist-收到消息错误：%o-%o", request, sender);
		}
	} else {
		console.warn("acgist-收到消息错误：%o-%o", request, sender);
	}
});

// 获取配置
chrome.runtime.sendMessage(
	{ "type": "config" },
	function(response) {
		if (response) {
			config = response;
			console.log("acgist-获取配置：%o", config);
		} else {
			console.warn("acgist-获取配置错误：%o", response);
		}
	}
);

// 监听页面事件
document.onkeydown = function(e) {
	var altKey = e.altKey;
	var ctrlKey = e.ctrlKey;
	var shiftKey = e.shiftKey;
	var keyCode = e.which || e.keyCode || e.charCode;
	if (ctrlKey && shiftKey) {
		switch(keyCode) {
			// Ctrl + Shift + S
			case 83:
				acgistSearch();
			break;
			// Ctrl + Shift + V
			case 86:
				acgistVideo(altKey);
			break;
			default:
				console.log("acgist-没有匹配按键：%s", keyCode);
			break;
		}
	}
	return true;
}

// 搜索
function acgistSearch() {
	switchSearch = !switchSearch;
	if (config) {
		var list = [];
		var elements = [];
		// 搜索元素
		var searchs = config.search.split(',');
		for(var search of searchs) {
			var searchElements = document.getElementsByTagName(search.trim());
			for (var element of searchElements) {
				elements[elements.length] = element;
			}
		}
		// 匹配元素
		for (var element of elements) {
			for (var rule of config.rules) {
				if (element && rule) {
					var text = element.innerText;
					if (text && (text.search(rule) >= 0 || text.toUpperCase().search(rule.toUpperCase()) >= 0)) {
						if (!list.includes(element)) {
							list[list.length] = element;
						}
					}
				}
			}
		}
		// 添加样式
		for (var element of list) {
			var className = element.className;
			if (switchSearch) {
				if (!className || className.indexOf(searchClass) < 0) {
					element.className += searchClass;
				}
			} else {
				if (className && className.indexOf(searchClass) >= 0) {
					element.className = className.substring(0, className.indexOf(searchClass));
				}
			}
		}
		// 匹配数量
		var size = switchSearch ? list.length : 0;
		chrome.runtime.sendMessage({ "type": "size", "size": size });
	}
}

// 关灯
function acgistVideo(altKey) {
	switchVideo = !switchVideo;
	if (config) {
		var list = document.getElementsByTagName('video');
		if(!list || list.length == 0) {
			console.log("acgist-没有找到视频标签");
			return;
		}
		var currentClass = altKey ? videoClass : videoLargeClass;
		for(var video of list) {
			var parent = video.parentNode;
			var videoClassName = video.className;
			var parentClassName = parent.className;
			if (switchVideo) {
				if (!videoClassName || videoClassName.indexOf(currentClass) < 0) {
					video.className += currentClass;
				}
				if (!parentClassName || parentClassName.indexOf(videoParentClass) < 0) {
					parent.className += videoParentClass;
				}
			} else {
				if (videoClassName && videoClassName.indexOf(currentClass) >= 0) {
					video.className = videoClassName.substring(0, videoClassName.indexOf(currentClass));
				}
				if (parentClassName && parentClassName.indexOf(videoParentClass) >= 0) {
					parent.className = parentClassName.substring(0, parentClassName.indexOf(videoParentClass));
				}
			}
		}
	}
}
// 获取background对象
var background = chrome.extension.getBackgroundPage();

// 获取配置
var config = background.config;

// 获取元素
var cleanKeyCode = document.getElementById("cleanKeyCode");
var countKeyCode = document.getElementById("countKeyCode");
var danmukuTemplate = document.getElementById("danmukuTemplate");
var danmukuInputClass = document.getElementById("danmukuInputClass");

// 初始信息
cleanKeyCode.value = config.cleanKeyCode;
countKeyCode.value = config.countKeyCode;
danmukuTemplate.value = config.danmukuTemplate;
danmukuInputClass.value = config.danmukuInputClass;

// 添加事件
cleanKeyCode.onkeydown = function(e) {
	cleanKeyCode.value = e.code;
	config.cleanKeyCode = e.code;
	background.persist(config);
}
countKeyCode.onkeydown = function(e) {
	countKeyCode.value = e.code;
	config.countKeyCode = e.code;
	background.persist(config);
}
danmukuTemplate.onblur = function() {
	config.danmukuTemplate = danmukuTemplate.value;
	background.persist(config);
}
danmukuInputClass.onblur = function() {
	config.danmukuInputClass = danmukuInputClass.value;
	background.persist(config);
}
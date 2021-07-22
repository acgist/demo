// 加载页面
chrome.app.runtime.onLaunched.addListener(function() {
	var width = 1000;
	var height = 800;
	var screenWidth = screen.availWidth;
	var screenHeight = screen.availHeight;
	chrome.app.window.create('index.html', {
		id: "snail",
		outerBounds: {
			width: width,
			height: height,
			left: Math.round((screenWidth - width) / 2),
			top: Math.round((screenHeight - height) / 2)
		}
	}, function(snail) {
		snail.onClosed.addListener(function() {
			chrome.log("蜗牛退出");
		});
	});
});


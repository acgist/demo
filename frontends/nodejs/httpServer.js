var http = require('http');
var queryString = require("querystring");

http.createServer(function(req, res) {
    
    var data = "";

    req.setEncoding("UTF-8");

    req.on("data", function(trunk) {
        console.log("收到请求：" + trunk);
        data += trunk;
    });

    req.on("end", function() {
        console.log("发送响应：" + data);
        res.end(data);
    });

}).listen(8080);

console.log("启动成功");
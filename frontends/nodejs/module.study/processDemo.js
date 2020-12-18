var events = require("events");

process.nextTick(() => {
    console.log("MM");
});
console.log("GG");

var status = 0;
var select = function(callback) {
    if(status == 0) {
        status = 1;
        setTimeout(() => {
            callback(status);
            status = 0; 
        }, 100);
    }
}
select((value) => {
    console.log(value);
});
select((value) => {
    console.log(value);
});

var statusOnce = 0;
var proxy = new events.EventEmitter();
var selected = function(callback) {
    proxy.once("selected", callback); // 注册事件：可能注册多次
    if(statusOnce == 0) {
        statusOnce = 1;
        setTimeout(() => {
            proxy.emit("selected", statusOnce); // 触发事件
            statusOnce = 0; 
        }, 100);
    }
}
selected((value) => {
    console.log("once：" + value);
});
selected((value) => {
    console.log("once：" + value);
});
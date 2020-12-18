var math = require("./math");
var funModule = require("./funModule");

var value = math.add(1, 2, 3);
console.log(value);
value = funModule.add(1, 2);
console.log(value);
value = funModule.add(1);
console.log(value);

// function say() {
//     console.log(this.id);
// }

var say = () => {
    setTimeout(() => {
        console.log(this.id);
    }, 1000);
}

say.call({id : "test"});

var math = {
    add : function() {
        var length = arguments.length;
        var sum = 0, index = 0;
        while(index < length) {
            sum += arguments[index];
            index++;
        }
        return sum;
    }
};

module.exports = math;
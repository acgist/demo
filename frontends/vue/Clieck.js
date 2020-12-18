<body>
    <div id="app">
        <button v-if="showButton" v-on:click="handleClick">点我</button>
    </div>
</body>

<script>
    new Vue({
        "el" : "#app",
        "data" : {
            "showButton" : true
        },
        "methods" : {
            "handleClick" : function() {
                console.log("点我！！");
            }
        }
    });
</script>
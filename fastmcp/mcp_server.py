from fastmcp import FastMCP

mcp = FastMCP("Python MCP Server", port = 19091)

@mcp.tool(description = "加法计算")
def add(a: int, b: int) -> int:
    return a + b

@mcp.tool(description = "根据城市名称获取天气")
def getWeather(city: str) -> str:
    mock = {
        "北京": "晴天",
        "广州": "阴天"
    }
    return mock[city] if city in mock else "未知天气"
    
@mcp.prompt
def welcome(text: str) -> str:
    return f"你好呀！{text}"
    
@mcp.resource("config://version")
def get_version(): 
    return "1.0.0"
    
if __name__ == "__main__":
    mcp.run(transport = "sse")
    
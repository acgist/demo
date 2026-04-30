package com.acgist.mcp.server.tool.weather;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WeatherTool {

    @Tool(description = "根据城市名称获取天气情况")
    public String getWeather(@ToolParam(required = true, description = "城市名称") String city) {
        log.info("调用根据城市名称获取天气情况接口：{}", city);
        final Map<String, String> mockData = Map.of(
            "西安", "晴天",
            "北京", "小雨",
            "上海", "大雨"
        );
        return mockData.getOrDefault(city, "抱歉：未查询到对应城市！");
    }

}

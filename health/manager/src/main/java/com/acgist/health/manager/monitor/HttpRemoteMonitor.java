package com.acgist.health.manager.monitor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.health.manager.configuration.ManagerProperties.Monitor;
import com.acgist.health.manager.configuration.ManagerProperties.RemoteMonitor;
import com.acgist.health.manager.configuration.ManagerProperties.Server;
import com.acgist.health.monitor.configuration.HealthProperties;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class HttpRemoteMonitor {

    private static final ObjectMapper MAPPER = HttpRemoteMonitor.buildMapper();

    @Autowired
    private HealthProperties healthProperties;

    public boolean get(Server server, Monitor monitor, RemoteMonitor remoteMonitor) {
        final StringBuilder url = new StringBuilder();
        url.append(monitor.getProtocol())
           .append("://")
           .append(server.getHost())
           .append(server.getPort() == null ? "" : (":" + server.getPort().toString()))
           .append(remoteMonitor.getPath());
        final Map<String,Object> map = this.get(
            url.toString(),
            this.healthProperties == null ? ""   : this.healthProperties.getSecurity(),
            monitor.getTimeout()  == null ? 5000 : monitor.getTimeout()
        );
        return map != null && Boolean.TRUE.equals(map.get("health"));
    }

    public Map<String, Object> get(String url, String token, int timeout) {
        final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(timeout)).build();
        try {
            final HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(URI.create(url)).GET().timeout(Duration.ofMillis(timeout)).header("token", token).build(),
                HttpResponse.BodyHandlers.ofString()
            );
            if (response != null && response.statusCode() == 200) {
                return MAPPER.readValue(response.body(), new TypeReference<Map<String, Object>>() {});
            }
        } catch (IOException | InterruptedException e) {
            log.error("发送请求异常：{}", url, e);
        }
        return Map.of();
    }

    public static final ObjectMapper buildMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper
            .setTimeZone(TimeZone.getDefault())
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .registerModules(HttpRemoteMonitor.buildJavaTimeModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(Include.NON_NULL);
    }
    
    private static final JavaTimeModule buildJavaTimeModule() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return javaTimeModule;
    }

}

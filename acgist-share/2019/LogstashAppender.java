package com.acgist.demo;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * <p>Logstash日志输出</p>
 * <p>配置参考：</p>
 * <pre>
# Appender
log4j.appender.logstash=com.acgist.demo.LogstashAppender
# Logstash端口
log4j.appender.logstash.Port=4567
# Logstash地址
log4j.appender.logstash.Host=192.168.1.240
# 断线重连时间（毫秒）
log4j.appender.logstash.Delay=10000
# 格式化：json（默认）、plain（文本）
log4j.appender.logstash.Format=json
# 项目名称
log4j.appender.logstash.Project=acgist
# 使用协议：tcp（默认）、udp
log4j.appender.logstash.Protocol=tcp
# 队列长度
log4j.appender.logstash.BufferSize=102400
# 日志格式
log4j.appender.logstash.layout=org.apache.log4j.PatternLayout
log4j.appender.logstash.layout.ConversionPattern=[acgist] %d %p [%c] - %m%n
 * </pre>
 * 
 * @author acgist
 */
public class LogstashAppender extends AppenderSkeleton {
	
	private static final ThreadLocal<DateFormat> FORMATER = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
	};
	
	/**
	 * UDP协议
	 */
	public static final String PROTOCOL_UDP = "udp";
	/**
	 * TCP协议
	 */
	public static final String PROTOCOL_TCP = "tcp";
	/**
	 * JSON
	 */
	public static final String FORMAT_JSON = "json";
	/**
	 * 简单文本
	 */
	public static final String FORMAT_PLAIN = "plain";
	/**
	 * 最大重新添加次数
	 */
	private static final int MAX_RETRY_TIMES = 10;
	/**
	 * UDP最大包长度
	 */
	private static final int MAX_UDP_PACKET_SIZE = 1024;
	
	/**
	 * 远程端口
	 */
	private int port;
	/**
	 * 远程地址
	 */
	private String host;
	/**
	 * 重连时间
	 */
	private long delay;
	/**
	 * 日志格式
	 */
	private String format;
	/**
	 * 项目名称
	 */
	private String project;
	/**
	 * 传输协议
	 */
	private String protocol;
	/**
	 * 缓存大小，默认：1024
	 */
	private int bufferSize = 1024;
	
	/**
	 * 使用TCP
	 */
	private boolean tcp;
	/**
	 * 使用JSON
	 */
	private boolean json;
	/**
	 * 异步线程
	 */
	private Thread thread;
	/**
	 * Socket连接
	 */
	private WritableByteChannel channel;
	/**
	 * 日志缓存
	 */
	private BlockingQueue<String> buffer;
	/**
	 * 是否关闭
	 */
	private volatile boolean close = false;
	
	public LogstashAppender() {
	}

	@Override
	protected void append(LoggingEvent event) {
		if(event != null) {
			final String message = buildMessage(event);
			messageBlock(event, message);
		}
	}

	@Override
	public void close() {
		this.close = true;
		this.releaseThread();
		this.releaseChannel();
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void activateOptions() {
		this.buffer = new LinkedBlockingQueue<String>(this.bufferSize);
		this.tcp = !PROTOCOL_UDP.equalsIgnoreCase(this.protocol);
		this.json = !FORMAT_PLAIN.equalsIgnoreCase(this.format);
		this.buildChannel();
		this.buildThread();
	}

	/**
	 * 创建客户端连接
	 */
	private void buildChannel() {
		try {
			if(this.tcp) {
				final SocketChannel channel = SocketChannel.open();
				channel.connect(new InetSocketAddress(this.host, this.port));
				channel.configureBlocking(false);
//				channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
//				channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				this.channel = channel;
			} else {
				final DatagramChannel channel = DatagramChannel.open();
				channel.configureBlocking(false); // 不阻塞
				channel.connect(new InetSocketAddress(this.host, this.port)); // 连接后使用：read、write
				this.channel = channel;
			}
		} catch (Exception e) {
			LogLog.error("Logstash-Socket远程服务器连接异常", e);
		}
	}

	/**
	 * 释放客户端连接
	 */
	private void releaseChannel() {
		if(this.channel != null) {
			try {
				this.channel.close();
			} catch (Exception e) {
				LogLog.error("Logstash-Socket关闭连接异常", e);
			}
		}
	}
	
	/**
	 * 创建线程：TODO：UDP丢包
	 */
	private void buildThread() {
		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String log = null;
				while(!close) {
					try {
						if(log == null) {
							log = buffer.take();
						}
						if(log != null) {
							channel.write(ByteBuffer.wrap(log.getBytes()));
							log = null;
						}
					} catch (Exception e) {
						LogLog.error("Logstash-日志发送异常：" + log, e);
						if(channel == null || !channel.isOpen()) {
							try {
								Thread.sleep(delay);
							} catch (Exception ex) {
								LogLog.error("Logstash-休眠异常", e);
							}
							LogLog.warn("Logstash-失败重连");
							releaseChannel(); // 释放
							buildChannel(); // 重连
						}
					}
				}
			}
		});
		this.thread.setDaemon(true);
		this.thread.setName("Logstash-日志发送线程");
		this.thread.start();
	}
	
	/**
	 * 释放线程
	 */
	private void releaseThread() {
	}
	
	/**
	 * 日志信息拆分
	 */
	private void messageBlock(LoggingEvent event, final String message) {
		if(this.tcp) {
			offerMessage(event, message);
		} else {
			int length = message.length();
			if(length <= MAX_UDP_PACKET_SIZE) {
				offerMessage(event, message);
			} else {
				int index = 0;
				long track = System.nanoTime();
				while(index < length) {
					if(length < index + MAX_UDP_PACKET_SIZE) {
						offerMessage(event, message.substring(index), track);
					} else {
						offerMessage(event, message.substring(index, index + MAX_UDP_PACKET_SIZE), track);
					}
					index += MAX_UDP_PACKET_SIZE;
				}
			}
		}
	}
	
	/**
	 * 创建日志
	 */
	private String buildMessage(LoggingEvent event) {
		final StringBuilder logBuilder = new StringBuilder(this.layout.format(event));
		if (this.layout.ignoresThrowable()) {
			final String[] ems = event.getThrowableStrRep();
			if (ems != null) {
				final int length = ems.length;
				for (int index = 0; index < length; index++) {
					logBuilder.append(ems[index]).append(Layout.LINE_SEP);
				}
			}
		}
		return logBuilder.toString();
	}
	
	/**
	 * 缓存日志
	 */
	private void offerMessage(LoggingEvent event, String message) {
		offerMessage(event, message, 0L);
	}
	
	/**
	 * 缓存日志
	 */
	private void offerMessage(LoggingEvent event, String message, long track) {
		String log;
		if(this.json) {
			log = buildJSONLog(event, message, track);
		} else {
			log = buildPlainLog(event, message, track);
		}
		int times = 0;
		boolean done = this.buffer.offer(log);
		while(!done) {
			Thread.yield();
			done = this.buffer.offer(log);
			if(++times > MAX_RETRY_TIMES) {
				LogLog.error("超过最大重试失败次数，日志记录失败：" + log + "，重试次数：" + times);
				break;
			}
		}
	}
	
	/**
	 * 创建JSON日志
	 */
	private String buildJSONLog(LoggingEvent event, String message, long track) {
		final Map<String, String> map = new HashMap<>();
		map.put("level", event.getLevel().toString()); // 级别
		map.put("project", this.project); // 项目
		map.put("clazz", event.getLoggerName()); // 类名
		map.put("timestamp", FORMATER.get().format(new Date(event.getTimeStamp()))); // 时间
		map.put("message", message); // 信息
		if(track > 0) {
			map.put("track", String.valueOf(track)); // 跟踪
		}
		return obj2json(map) + Layout.LINE_SEP;
	}

	/**
	 * 创建文本日志
	 */
	private String buildPlainLog(LoggingEvent event, String message, long track) {
		if(track > 0) {
			return message + " - " + track;
		} else {
			return message;
		}
	}
	
	/**
	 * JSON序列化
	 */
	public static final String obj2json(Object object) {
		if(object == null) {
			return null;
		}
		final ObjectMapper mapper = buildMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			LogLog.error("JAVA对象转JSON异常，对象：" + object, e);
		}
		return null;
	}
	

	/**
	 * 不序列化null，未知属性不反序列化。
	 */
	public static final ObjectMapper buildMapper() {
		final ObjectMapper mapper = new ObjectMapper();
//		mapper.enableDefaultTyping(); // 漏洞：CVE-2019-12384
		mapper.setSerializationInclusion(Inclusion.NON_NULL); // 不序列化 null 值，使用注解：@JsonInclude(Include.NON_NULL)
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 未知属性不反序列化，使用注解：@JsonIgnoreProperties(ignoreUnknown = true)
		return mapper;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

}

package com.acgist.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Logstash日志输出
 */
public class LogstashAppender extends AppenderSkeleton {
	
	/**
	 * 最大重新添加次数
	 */
	private static final int MAX_RETRY_TIMES = 10;
	
	/**
	 * 是否关闭
	 */
	private volatile boolean close = false;

	/**
	 * 缓存大小，默认：1024
	 */
	private int bufferSize = 1024;
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
	 * 异步线程
	 */
	private Thread thread;
	/**
	 * Socket连接
	 */
	private SocketChannel channel;
	/**
	 * 日志缓存
	 */
	private BlockingQueue<String> buffer;
	
	public LogstashAppender() {
	}

	@Override
	protected void append(LoggingEvent event) {
		if(event != null) {
			int index = 0;
			final StringBuffer logBuilder = new StringBuffer(this.layout.format(event));
			if (this.layout.ignoresThrowable()) {
				final String[] exs = event.getThrowableStrRep();
				if (exs != null) {
					final int length = exs.length;
					for (int i = 0; i < length; i++) {
						logBuilder.append(exs[i]).append(Layout.LINE_SEP);
					}
				}
			}
			final String log = logBuilder.toString();
			boolean ok = this.buffer.offer(log);
			while(!ok) {
				Thread.yield();
				ok = this.buffer.offer(log);
				if(++index > MAX_RETRY_TIMES) {
					LogLog.error("超过最大重试失败次数，日志记录失败：" + log + "，重试次数：" + index);
					break;
				}
			}
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
		this.buildChannel();
		this.buildThread();
	}

	/**
	 * 创建客户端连接
	 */
	private void buildChannel() {
		try {
			this.channel = SocketChannel.open();
			this.channel.connect(new InetSocketAddress(this.host, this.port));
			this.channel.configureBlocking(false);
//			this.channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
//			this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			this.channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		} catch (IOException e) {
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
	 * 创建线程
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
						LogLog.error("Logstash-日志发送异常", e);
						try {
							Thread.sleep(delay);
						} catch (Exception ex) {
							LogLog.error("Logstash-休眠异常", e);
						}
						LogLog.warn("Logstash-失败重连");
						if(channel == null || !channel.isOpen() || !channel.isConnected()) {
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
	
	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

}

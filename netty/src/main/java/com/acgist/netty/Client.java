package com.acgist.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.netty.Server.MesageHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	private final String host;
	private final int port;
	private Channel channel;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws Exception {
		final EventLoopGroup group = new NioEventLoopGroup();
		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel channel) {
					channel.pipeline()
						.addLast(new StringEncoder())
						.addLast(new StringDecoder())
//						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new MesageHandler("Client"));
				}
			});
		final ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
		if (future.isSuccess()) {
			this.channel = future.channel();
			LOGGER.debug("连接服务器成功");
		} else {
			LOGGER.debug("连接服务器失败", future.cause());
			group.shutdownGracefully();
		}
	}

	public Channel getChannel() {
		return channel;
	}

}

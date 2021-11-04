package com.acgist.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	public void listen(int port) throws Exception {
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workerGroup = new NioEventLoopGroup();
		final ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) {
					channel.pipeline()
						.addLast(new StringEncoder())
						.addLast(new StringDecoder())
//						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new MesageHandler("Server"));
				}
			});
		final ChannelFuture future = serverBootstrap.bind(port).sync();
		if (future.isSuccess()) {
			LOGGER.debug("服务端启动成功");
			bossGroup.awaitTermination(1024, TimeUnit.SECONDS);
		} else {
			LOGGER.debug("服务端启动失败", future.cause());
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static class MesageHandler extends ChannelInboundHandlerAdapter {

		private final String name;

		public MesageHandler(String name) {
			this.name = name;
		}
		
		@Override
		public void channelActive(ChannelHandlerContext context) {
			LOGGER.debug("{}连接成功", this.name);
		}

		@Override
		public void channelRead(ChannelHandlerContext context, Object message) {
			LOGGER.debug("{}收到信息：{}", this.name, message);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext context) {
			LOGGER.debug("{}读取完毕", this.name);
			context.flush();
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext context, Throwable e) {
			LOGGER.debug("{}读取异常", this.name, e);
			context.close();
		}
		
	}

}

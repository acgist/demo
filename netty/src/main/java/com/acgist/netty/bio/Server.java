package com.acgist.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.netty.IoUtils;

public class Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	public void create(int port) throws IOException {
		final ServerSocket server = new ServerSocket(port);
		while (true) {
			final Socket socket = server.accept();
			LOGGER.debug("接收连接：{}", socket);
			new Thread(new Runnable() {
				@Override
				public void run() {
					OutputStream out = null;
					try {
						out = socket.getOutputStream();
						out.write("连接成功".getBytes(Charset.forName("UTF-8")));
						out.flush();
					} catch (IOException e) {
						LOGGER.error("输出异常", e);
					}
					InputStream input = null;
					try {
						input = socket.getInputStream();
						final byte[] bytes = new byte[16];
						int length;
						while ((length = input.read(bytes)) >= 0) {
							final String message = new String(bytes, 0, length);
							LOGGER.debug("Server收到：{}", message);
							if (message.equals("close")) {
								// 粘包
								break;
							}
						}
					} catch (IOException e) {
						LOGGER.error("输入异常", e);
					}
					IoUtils.close(out);
					IoUtils.close(input);
					IoUtils.close(socket);
					IoUtils.close(server);
				}
			}).start();
		}
	}

//
//	public void aioServer(int port) throws Exception {
//		final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
//		EventLoopGroup group = new OioEventLoopGroup();
//		try {
//			ServerBootstrap b = new ServerBootstrap(); // 1
//
//			b.group(group) // 2
//				.channel(OioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
//				.childHandler(new ChannelInitializer<SocketChannel>() {// 3
//					@Override
//					public void initChannel(SocketChannel ch) throws Exception {
//						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 4
//							@Override
//							public void channelActive(ChannelHandlerContext ctx) throws Exception {
//								ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);// 5
//							}
//						});
//					}
//				});
//			ChannelFuture f = b.bind().sync(); // 6
//			f.channel().closeFuture().sync();
//		} finally {
//			group.shutdownGracefully().sync(); // 7
//		}
//	}
}

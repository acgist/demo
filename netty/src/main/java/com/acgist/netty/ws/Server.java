package com.acgist.netty.ws;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Server {

	public static void main(String[] args) throws Exception {
		
		KeyStore ks = KeyStore.getInstance("JKS");
		InputStream ksInputStream = Server.class.getResourceAsStream("/acgist.jks");
		ks.load(ksInputStream, "123456".toCharArray());
		ks.aliases().asIterator().forEachRemaining(aliase -> {
			try {
				PublicKey publicKey = ks.getCertificate(aliase).getPublicKey();
				System.out.println(publicKey);
				System.out.println(ks.getKey(aliase, "123456".toCharArray()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		serverBootstrap.group(bossGroup, workGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.childHandler(new NioWebSocketChannelInitializer());
		Channel channel = serverBootstrap.bind(8080).sync().channel();
		channel.closeFuture().sync();
	}

	public static class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast("logging", new LoggingHandler("DEBUG"));
			channel.pipeline().addLast("http-codec", new HttpServerCodec());
			channel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
			channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
			channel.pipeline().addLast("handler", new NioWebSocketHandler());
			channel.pipeline().addLast("ws", new WebSocketServerProtocolHandler("/ws"));
			// 以下为要支持wss所需处理
			KeyStore ks = KeyStore.getInstance("JKS");
			InputStream ksInputStream = this.getClass().getResourceAsStream("/acgist.jks");
			ks.load(ksInputStream, "123456".toCharArray());
			ks.aliases().asIterator().forEachRemaining(System.out::println);
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, "123456".toCharArray());
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kmf.getKeyManagers(), new TrustManager[] {
					new X509TrustManager() {

						@Override
						public void checkClientTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							
						}

						@Override
						public void checkServerTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							
						}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
						
					}
			}, null);
			SSLEngine sslEngine = sslContext.createSSLEngine();
			sslEngine.setUseClientMode(false);
			sslEngine.setNeedClientAuth(false);
			// 需把SslHandler添加在第一位
			channel.pipeline().addFirst("ssl", new SslHandler(sslEngine));

		}
	}

	public static class NioWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("连接");
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("断开");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
			System.out.println(msg.content().toString(StandardCharsets.UTF_8));
		}
	}
}
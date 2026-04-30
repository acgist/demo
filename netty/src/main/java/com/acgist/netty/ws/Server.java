package com.acgist.netty.ws;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Netty WebSocket
 * 
 * 注意事项：
 * 首先需要访问https://localhost:8080受到信任
 * 然后定义：var socket = new WebSocket('wss://localhost:8080/ws')
 * 不知为何报错：Received fatal alert: certificate_unknown（需要真实机构颁发证书没有问题）
 * 这个异常没有影响：socket.send('acgist')
 * 
 * @author yusheng
 */
public class Server {

    public static void main(String[] args) throws Exception {
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
            // 需把SslHandler添加首位
            InputStream input = this.getClass().getResourceAsStream("/ca.jks");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(input, "123456".toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "123456".toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            sslEngine.setNeedClientAuth(false);
//            sslEngine.setEnabledProtocols(new String[] {"TLSv1.2", "TLSv1.1"});
            channel.pipeline().addFirst(new SslHandler(sslEngine));
            
//            SslContext sslCtx = SslContextBuilder.forServer(keyManagerFactory).build();
//            channel.pipeline().addLast(sslCtx.newHandler(channel.alloc()));
            
            channel.pipeline().addLast("logging", new LoggingHandler("DEBUG"));
            channel.pipeline().addLast(new HttpServerCodec());
            channel.pipeline().addLast(new ChunkedWriteHandler());
            channel.pipeline().addLast(new HttpObjectAggregator(65536));
            channel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
//            channel.pipeline().addLast(new WebSocketServerCompressionHandler());
//            channel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true));
            channel.pipeline().addLast(new NioWebSocketHandler());
        }
    }

    public static class NioWebSocketHandler extends MessageToMessageCodec<WebSocketFrame, String> {

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
//            cause.printStackTrace();
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            out.add(new TextWebSocketFrame(msg));
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
            System.out.println(msg.content().toString(StandardCharsets.UTF_8));
            out.add(msg.content().toString(StandardCharsets.UTF_8));
        }

    }

}
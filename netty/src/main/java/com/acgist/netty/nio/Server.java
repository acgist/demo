package com.acgist.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.netty.IoUtils;

public class Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	public void create(int port) throws IOException {
		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		final ServerSocket serverSocket = serverSocketChannel.socket();
		final InetSocketAddress address = new InetSocketAddress(port);
		serverSocket.bind(address);
		final Selector selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		final ByteBuffer message = ByteBuffer.wrap("连接成功".getBytes());
		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				// TODO：log
				break;
			}
			final Set<SelectionKey> keys = selector.selectedKeys();
			final Iterator<SelectionKey> iterator = keys.iterator();
			while (iterator.hasNext()) {
				final SelectionKey key = iterator.next();
				// 移除
				iterator.remove();
				try {
					if (key.isAcceptable()) {
						final ServerSocketChannel socket = (ServerSocketChannel) key.channel();
						final SocketChannel client = socket.accept();
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, message.duplicate());
					}
					if (key.isReadable()) {
						final SocketChannel client = (SocketChannel) key.channel();
						final ByteBuffer buffer = ByteBuffer.allocate(1024);
						client.read(buffer);
						LOGGER.debug("Server收到：{}", new String(buffer.array()));
					}
					if (key.isWritable()) {
						final SocketChannel client = (SocketChannel) key.channel();
						final ByteBuffer buffer = (ByteBuffer) key.attachment();
						while (buffer.hasRemaining()) {
							if (client.write(buffer) == 0) {
								break;
							}
						}
						client.close();
					}
				} catch (IOException e) {
					// TODO：log
					IoUtils.close(key.channel());
				}
			}
		}
	}

}

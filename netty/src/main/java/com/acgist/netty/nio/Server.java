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
		final Selector readSelector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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
				if (key.isAcceptable()) {
					final ServerSocketChannel socket = (ServerSocketChannel) key.channel();
					final SocketChannel client = socket.accept();
					client.configureBlocking(false);
					// 不建议注册写出
					client.register(readSelector, SelectionKey.OP_READ);
//					client.write(ByteBuffer.wrap("acgist".getBytes()));
					LOGGER.debug("接收请求：{}-{}", client, key);
					// 线程池
					new ReadThread(readSelector).start();
				}
			}
		}
	}
	
	public static class ReadThread extends Thread {
		
		private Selector readSelector;
		
		public ReadThread(Selector readSelector) {
			this.readSelector = readSelector;
		}

		@Override
		public void run() {
			while (true) {
				try {
					readSelector.select();
				} catch (IOException e) {
					// TODO：log
					break;
				}
				final Set<SelectionKey> readKeys = readSelector.selectedKeys();
				final Iterator<SelectionKey> readIterator = readKeys.iterator();
				while (readIterator.hasNext()) {
					final SelectionKey readKey = readIterator.next();
					// 移除
					readIterator.remove();
					final SocketChannel client = (SocketChannel) readKey.channel();
					LOGGER.debug("读取消息：{}-{}", client, readKey);
					try {
						if (readKey.isReadable()) {
							final SocketChannel readClient = (SocketChannel) readKey.channel();
							final ByteBuffer buffer = ByteBuffer.allocate(1024);
							readClient.read(buffer);
//							readClient.write(buffer);
							LOGGER.debug("Server收到：{}", new String(buffer.array()));
						}
					} catch (IOException e) {
						// TODO：log
						IoUtils.close(readKey.channel());
					}
				}
			}
		}
	}

}

package com.acgist.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.netty.IoUtils;

/**
 * 没有完成
 */
public class Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
	
	public void connect(int port) throws IOException {
		final SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		final Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress("localhost", port));
		while(selector.select() > 0) {
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				if(key.isConnectable()) {
					LOGGER.debug("Client连接：{}", key);
					if(channel.isConnectionPending()) {
						// 非常重要
						if(channel.finishConnect()) {
//							key.interestOps(SelectionKey.OP_READ);
							channel.register(selector, SelectionKey.OP_READ);
							LOGGER.debug("Client连接成功");
						}
					}
					new Thread(() -> {
						String line;
						final Scanner scanner = new Scanner(System.in);
						while ((line = scanner.nextLine()) != null) {
							LOGGER.debug("Client发送消息：{}", line);
							try {
								channel.write(ByteBuffer.wrap(line.getBytes()));
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (line.equals("close")) {
								break;
							}
						}
						IoUtils.close(scanner);
					}).start();
				} else if(key.isReadable()) {
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					channel.read(buffer);
					LOGGER.debug("Client消息：{}", new String(buffer.array()));
				}
			}
		}
	}
	
}

package com.acgist.netty.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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
		channel.connect(new InetSocketAddress(port));
		final Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		final Socket socket = new Socket("localhost", port);
		final InputStream input = socket.getInputStream();
		final OutputStream out = socket.getOutputStream();
		String line;
		final byte[] bytes = new byte[input.available()];
		input.read(bytes);
		LOGGER.debug("Client收到：{}", new String(bytes));
		final Scanner scanner = new Scanner(System.in);
		while ((line = scanner.nextLine()) != null) {
			out.write(line.getBytes());
			if (line.equals("close")) {
				break;
			}
		}
		IoUtils.close(out);
		IoUtils.close(input);
		IoUtils.close(socket);
		IoUtils.close(scanner);
	
	}
	
}

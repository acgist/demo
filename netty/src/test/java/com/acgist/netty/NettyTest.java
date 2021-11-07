package com.acgist.netty;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import io.netty.channel.Channel;

public class NettyTest {

	private static final int PORT = 8888;
	
	@Test
	public void testServer() throws Exception {
		new Server().listen(PORT);
	}

	@Test
	public void testClient() throws Exception {
		final Client client = new Client("127.0.0.1", PORT);
		String line;
		client.connect();
		final Channel channel = client.getChannel();
		final Scanner scanner = new Scanner(System.in);
		while ((line = scanner.nextLine()) != null) {
			channel.writeAndFlush(line.replace("\\n", "\n"));
			if(line.equals("close")) {
				channel.close();
				break;
			}
		}
		IoUtils.close(scanner);
	}

}

package com.acgist.netty.bio;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class BioTest {

	@Test
	public void test() throws IOException {
		new Thread(() -> {
			final Server server = new Server();
			try {
				server.create(8888);
			} catch (IOException e) {
				// TODO：log
			}
		}).start();
		final Client client = new Client();
		client.connect(8888);
	}

}

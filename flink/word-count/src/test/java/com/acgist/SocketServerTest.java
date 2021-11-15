package com.acgist;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class SocketServerTest {

	@Test
	public void testServer() throws IOException {
		final ServerSocket server = new ServerSocket(9000);
		final Scanner scanner = new Scanner(System.in);
		while (true) {
			String line = null;
			final Socket socket = server.accept();
			final OutputStream output = socket.getOutputStream();
			System.out.println("客户端接入：" + socket);
			while (scanner.hasNextLine()) {
				line = scanner.nextLine() + "\r\n";
				try {
					output.write(line.getBytes());
					output.flush();
				} catch (IOException e) {
					break;
				}
				if (line.equals("close")) {
					break;
				}
			}
			System.out.println("客户端关闭：" + socket);
			socket.close();
			if (line.equals("close")) {
				break;
			}
		}
		server.close();
		scanner.close();
	}

}

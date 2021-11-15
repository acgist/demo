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
		ServerSocket server = new ServerSocket(9000);
		Socket socket = server.accept();
		Scanner scanner = new Scanner(System.in);
		OutputStream output = socket.getOutputStream();
		String line;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			output.write(line.getBytes());
			if(line.equals("close")) {
				break;
			}
		}
		socket.close();
		server.close();
		scanner.close();
	}
	
}

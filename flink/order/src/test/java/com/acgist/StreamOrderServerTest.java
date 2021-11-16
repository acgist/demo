package com.acgist;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class StreamOrderServerTest {

	@Test
	public void testServer() throws IOException, InterruptedException {
		final ServerSocket server = new ServerSocket(9000);
		final StringBuilder builder = new StringBuilder();
		final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		final String[] area = {"番禺", "天河", "荔湾", "黄埔", "海珠"};
		final String[] fruit = {"苹果", "李子", "桃子", "樱桃", "香蕉", "凤梨", "西瓜"};
		final Random random = new Random();
		final Socket socket = server.accept();
		final OutputStream output = socket.getOutputStream();
		System.out.println("客户端接入：" + socket);
		// 时间-区域-金额-名称-数量
		int index = 0;
		while (index++ < 100) {
			builder.setLength(0);
			builder
				.append(format.format(new Date())).append("-")
				.append(area[random.nextInt(area.length)]).append("-")
				.append(random.nextInt(1000)).append("-")
				.append(fruit[random.nextInt(fruit.length)]).append("-")
				.append(random.nextInt(10))
				.append("\r\n");
			System.out.print(builder.toString());
			try {
				output.write(builder.toString().getBytes());
				output.flush();
			} catch (IOException e) {
				break;
			}
			Thread.sleep(random.nextInt(1000));
		}
		System.out.println("客户端关闭：" + socket);
		socket.close();
		server.close();
	}

}

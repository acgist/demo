package com.acgist.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;

public class DownloadTest {

	@Test
	public void download() {
		try (
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(new HttpGet("http://localhost/header4download/download"))
		) {
			Arrays.stream(response.getAllHeaders()).forEach(name -> {
				System.out.println("头域：" + name.getName() + "，值：" + name.getValue());
			});
			System.out.println("保存文件");
			IOUtils.copy(response.getEntity().getContent(), new FileOutputStream("E:\\acgist.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

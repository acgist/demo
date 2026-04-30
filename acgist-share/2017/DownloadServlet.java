package com.acgist.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

@WebServlet(name="DownloadServlet", urlPatterns="/download")
public class DownloadServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public DownloadServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 下载文件
		File file = new File("E:\\tmp\\1.png");
		// 头域信息
		Map<String, String> data = new HashMap<>();
		data.put("filename", "acgist.jpg");
		data.forEach((name, value) -> {
			// 设置头域
			response.setHeader(name, value);
		});
		try(FileInputStream is = new FileInputStream(file)) {
			// 输出文件
			IOUtils.copy(is, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}

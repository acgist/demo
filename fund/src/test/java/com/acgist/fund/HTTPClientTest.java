package com.acgist.fund;

import org.junit.jupiter.api.Test;

import com.acgist.fund.utils.HTTPClient;

public class HTTPClientTest {

	@Test
	public void get() {
		String html = HTTPClient.get(
			"http://api.fund.eastmoney.com/f10/HYPZ/?fundCode=161725&year=",
			"http://fundf10.eastmoney.com/hytz_161725.html"
		);
		System.out.println(html);
	}
	
}

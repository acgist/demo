package com.acgist.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

import com.acgist.gateway.service.GatewayService;
import com.acgist.gateway.service.SignatureService;
import com.acgist.utils.DateUtils;
import com.acgist.utils.HTTPUtils;
import com.acgist.utils.JSONUtils;

public class GatewayTest {

	private static SignatureService service;
	
	@BeforeClass
	public static void init() {
		service = new SignatureService(
//			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWh2boYcHdT+Q6pABZbyG2bDOdAH1cPv7TpwoKZGLDWGtUsj1w4JA9vL6ZAJe0XVO1TdnzCMw922565E0/r+zOYFizr9Tb37JMjMGyyXgHm+WZ/Vm+BOXi86l8unEmqcJM8gKv8zvWV68HmSaFIu3s10nShJadcMZh7hVrqXPrpQIDAQAB",
//			"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJaHZuhhwd1P5DqkAFlvIbZsM50AfVw+/tOnCgpkYsNYa1SyPXDgkD28vpkAl7RdU7VN2fMIzD3bbnrkTT+v7M5gWLOv1NvfskyMwbLJeAeb5Zn9Wb4E5eLzqXy6cSapwkzyAq/zO9ZXrweZJoUi7ezXSdKElp1wxmHuFWupc+ulAgMBAAECgYAOmw7CJ5Ie/jx/8B/UjbLwt2j+p+iCBd7F/KQ9w+XNXIv1iOHrTO7R/rljsSoHJzMTGX23VjgzHFKI/BP+xPwuF0KSwCdV3uVRpWNlo9vrVARk/LDMNKamfK9k1+TV8uY1mlO4aYIywwN45Zr/sCOf+cRRzn8x1OlbowJEU3VhFQJBAPGD6zHx13WBWn+Sq/8jG451Y3/vQ6Lw86hSc6UiGQwOIPlDwJVDtaIgftx1O9VdHnQcXZ+26ox2AnM1dLPP8usCQQCfjohfNPDrsXeZR4rdwPd9AQ+ETwa+b1p80l1KiuxLDEauaq+MkDYT3yJRXoEYSrZrDmUKMV5tLuWGJqTifVevAkEAqHIWvy3q1XTTtriIp5lH5fMv45HwPZwKhTKEn/8JMyRDkTbVAgZIj3xUntRRV/3reJY0ImoEatT/3nTBIYx+4wJBAI5xBWfRcH9JiJbrWpqLqaYMK1kX39JkwKiMeMKScU6yX+tXzO6008I7wnxX3PHdySqbyDIoTr80Ta3MlAiqk3sCQHgqEA26T24hz/k2QRJ4kz3/nhdM4/dyDUU9rOykH734DIPBK/Zi6hgvfQXwfNglN2iYUxlJb3hvAE1VEkjbt6s="
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8N09VZ/ARi+dBbxQq84ynf162QF5P71B/0DY54Lo1XSQXzcbCa6nrySbsLsJJ8FmVtBUWmomxQs0Gf37QTjN8cMVp1ziTXMr4zOapV3WYlG4XSf9kxnIgOD1OyjcjPaglBucJNf90Gyswnqc+3RF4X7Andb21SPpO+bHlsF/+tQIDAQAB",
			"MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBALw3T1Vn8BGL50FvFCrzjKd/XrZAXk/vUH/QNjngujVdJBfNxsJrqevJJuwuwknwWZW0FRaaibFCzQZ/ftBOM3xwxWnXOJNcyvjM5qlXdZiUbhdJ/2TGciA4PU7KNyM9qCUG5wk1/3QbKzCepz7dEXhfsCd1vbVI+k75seWwX/61AgMBAAECgYEAnqw6U5QGTaSWVxGf/P3J7ENyw07Ms7LbOIV90ZllIivdi7vM/obpf3/bMm3RGahovc5NoPqzbh8U3Y+Kh24F9EQZxuBmSbC7vdj52sKrhj0Yh+U4Q4latDeLAFE/BAm4MDSLn8yqp4MGRwfEUJTbtaGRqVM4wGiBz5hX6B+Sg0ECQQD3QawAzCvbhlwLzEHzs5zdhso4rZAwKo7vaVXMwSjSFJjSH6AKNofGMq9MI0ggUvuAcYsmB5sHckhJf3hK3ze9AkEAwt8qOSX5la+PSTx9LuNsbPySyyGCSfJJHLiLBI/8EPs+0tZPfazJryhn5x+ls5Pk/8Gh0jVbuKXf4wKUbm72WQJBAI6ZhH7NQfoQqmNvgRXxH+YvR8+aYg81bEwtfvtg548Jq/17mcxqGLI5JxqNOprR4RT55xuexU1tHr2s046y4C0CQQCwrt39iqzMRqzHXgyyIokAF9CQVAVpPj+Dxt5keJe2XdYMGztfWvZR1XS/XTZDp2gNa2ZozPAoblyb2o7xwbGhAkEAgt7Uj3AOMPcqUj/1iQXjn43P1b2LEZqoEL8Ws9NdaasMHtfiyj0nwkr/HL6RwooSnLf+eY5pY6IH4ofvx+wE4A=="
		);
		service.init();
	}
	
	@Test
	public void pay() throws InterruptedException {
		final Map<String, Object> request = new HashMap<>();
//		request.put("requestTime", DateUtils.buildTime());
		request.put("orderId", "1234");
		request.put("amount", "100");
		request.put("gateway", "/pay");
//		request.put("orderId", "fail");
//		request.put("orderId", "exception");
		request.put("reserved", "保留数据");
		service.signature(request);
		final int count = 1;
		final CountDownLatch down = new CountDownLatch(count);
		final ExecutorService executors = Executors.newFixedThreadPool(100);
		final long begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			executors.submit(() -> {
				final String json = HTTPUtils.post("http://localhost:8080/gateway", JSONUtils.serialize(request));
				this.response(json);
				down.countDown();
			});
		}
		down.await();
		final long end = System.currentTimeMillis();
		System.out.println("执行时间：" + (end - begin));
		executors.shutdown();
	}
	
	public void response(String json) {
		final Map<String, Object> response = JSONUtils.toMap(json);
		if(service.verify(response)) {
			final String code = (String) response.get(GatewayService.GATEWAY_CODE);
			if("0000".equals(code)) {
				System.out.println("操作正常：" + json);
			} else {
				System.err.println("操作失败：" + json);
			}
		} else {
			System.err.println("验签异常");
		}
	}
	
}

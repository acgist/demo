package com.api.test;

import java.io.IOException;
import java.net.URI;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.api.core.gateway.APICode;
import com.api.core.order.gateway.request.PayRequest;
import com.api.core.service.SignService;
import com.api.utils.CAUtils;
import com.api.utils.JSONUtils;

@SpringBootTest(properties = "logging.level.root=error")
public class OrderTest {

	@Test
	public void order() throws InterruptedException, IOException {
		RestTemplate rest = new RestTemplate();
		int count = 1;
		long begin = System.currentTimeMillis();
		rest.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		});
		CountDownLatch latch = new CountDownLatch(count);
		ExecutorService exe = Executors.newFixedThreadPool(100);
		for (int i = 0; i < count; i++) {
			exe.submit(() -> {
				try {
					PayRequest request = new PayRequest();
					request.setUsername("test");
					request.setOrderId(UUID.randomUUID().toString());
//					request.setOrderId("fail");
					request.setRequestTime("1234");
					Map<String, String> data= request.data();
					PublicKey publicKey = CAUtils.stringToPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC1PaGCetY2GknOEcui/6zp2F3el7XzBdrzK0bcJQW//5Fyejtbl1ibadFlMOdd8yWW4cxBBBKdYRX9TGlyae8flK44xMwl4EvYmMwof8ckl26ESa1Mt8UM0WcsKsJFsZX8xlUCzHIdTAsgjPSR/WFaAcRl9InyIaE5Lk/kGnrowIDAQAB");
					PrivateKey privateKey = CAUtils.stringToPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAILU9oYJ61jYaSc4Ry6L/rOnYXd6XtfMF2vMrRtwlBb//kXJ6O1uXWJtp0WUw513zJZbhzEEEEp1hFf1MaXJp7x+UrjjEzCXgS9iYzCh/xySXboRJrUy3xQzRZywqwkWxlfzGVQLMch1MCyCM9JH9YVoBxGX0ifIhoTkuT+QaeujAgMBAAECgYAFSchhKJt7rtupOywdZIk6B8T3yFl3DYfQ0qgYmNdTiqtjdMLWfgSGSvpc5KN3hPUfCroPMCPcbp2X1JBaLCl8dNaWDP97YkFS5aEcoy1W8HlA9tqLkjmrz6dNWUIQyf8mNpvmw6C3sBdrxyKj+wxXVJilJ6x2I2X8amMfp2d50QJBAOMeekeRSxNGADq/8JFxrBhTuAB//yOqy6oaGSHwSFVjLKRs0XDvjf+4GIBaowkAR+57DlatTwyvMQ4FcfK4ggkCQQCTeAAGVwxLb1rv+NkEqwABadU2raDSGquPiYttirfbxrA2BP8e5jJDTWAz3hocnp0nmS4JCHPa2cLwW0bZNftLAkAwyLIhPz8uA6I2/FVzGESL8Seby+KEycH3ZqWxWYhf9u523vzZ0krg+60fnNhaLZpFyl7ZFV7ebZ8O0PKZ3THpAkAIqcIGnVKBk/cZpdrJ5WnIq3j1D1olPZ/afNAODVojtRZg9gYuwIMvZPpD0cZi7AZ7bwo/SZnUv3ouOI5+8CblAkEAnffipGdxexN1v1EHdA3QdoGT4AJps2A3I2uxOHaBArHkm6AEQziMJNGsyXq2q2OIYteNSJd8/Cvi0Z2o6igM/g==");
					String sign = CAUtils.sign(SignService.dataToDigest(data), privateKey);
					request.setSign(sign);
//					request.setOrderId("exception");
//					zuul网关
					ResponseEntity<String> response = rest.postForEntity(URI.create("http://192.168.1.100:23010/gateway/api/order/pay"), request, String.class);
//					ResponseEntity<String> response = rest.postForEntity(URI.create("http://192.168.1.100:23010/gateway/api/order/query"), request, String.class);
//					服务网关
//					ResponseEntity<String> response = rest.postForEntity(URI.create("http://192.168.1.100:32010/gateway/api/order/pay"), request, String.class);
//					ResponseEntity<String> response = rest.postForEntity(URI.create("http://192.168.1.100:32010/gateway/api/order/query"), request, String.class);
//					直接调用服务
//					ResponseEntity<String> response = rest.postForEntity(URI.create("http://192.168.1.100:32010/service/order"), request, String.class);
					String json = response.getBody();
					Map<String, String> returnData = JSONUtils.toMap(json)
						.entrySet()
						.stream()
						.map(entry -> {
							return Map.entry(entry.getKey(), (String) entry.getValue());
						})
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
					String verifyString = SignService.dataToDigest(returnData);
					String signString = returnData.get("sign");
					if(CAUtils.verify(verifyString, signString, publicKey)) {
//						System.out.println("ok");
						if(!APICode.success(returnData.get("code"))) {
							System.out.println(json);
							System.out.println("交易失败");
						}
					} else {
						System.out.println(json);
						System.out.println("验签失败");
					}
//					System.out.println(json);
//					System.out.println(response.getStatusCodeValue());
//					System.out.println(response.getHeaders().getContentType());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
		exe.shutdown();
	}

}

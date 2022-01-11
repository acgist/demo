package com.acgist.websocket;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class WebsocketApplication {

	@Bean
	@ConditionalOnMissingBean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	/**
	 * 这里需要static
	 * 
	 * @author acgist
	 */
	@Component
	@ServerEndpoint("/websocket")
	public static class WebSocketServer {

		@OnOpen
		public void onOpen(Session session) {
			System.out.println("open：" + session.getId());
			new Thread(() -> {
				while(session.isOpen()) {
					try {
						session.getBasicRemote().sendText(System.currentTimeMillis() + "");
					} catch (IOException e) {
					}
//					session.getAsyncRemote().sendText(System.currentTimeMillis() + "");
					try {
						TimeUnit.SECONDS.sleep(2);
//						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}).start();
		}

		@OnClose
		public void onClose(Session session) {
			System.out.println("close：" + session.getId());
		}

		@OnMessage
		public void onMessage(Session session, String message) throws IOException {
			System.out.println("message：" + message);
		}

		@OnError
		public void onError(Session session, Throwable throwable) {
			System.out.println("error：" + session.getId());
			System.out.println("error：" + throwable.getMessage());
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

}

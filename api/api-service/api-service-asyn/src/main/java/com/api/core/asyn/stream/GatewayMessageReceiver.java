package com.api.core.asyn.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.api.core.asyn.config.GatewayReceiverBinding;
import com.api.core.asyn.pojo.message.GatewayMessage;
import com.api.data.asyn.repository.GatewayRepository;

/**
 * 网关信息队列接收器<br>
 * 保存网关信息到数据库
 */
@EnableBinding(GatewayReceiverBinding.class)
public class GatewayMessageReceiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMessageReceiver.class);
	
	@Autowired
	private GatewayRepository gatewayRepository;
	
	@StreamListener(GatewayReceiverBinding.GATEWAY_RECEIVER_STREAM_BINDER)
	public void receive(Message<GatewayMessage> message) {
		GatewayMessage gatewayMessage = message.getPayload();
		if(gatewayMessage == null || gatewayMessage.getEntity() == null) {
			LOGGER.warn("网关信息为空");
		} else {
			gatewayRepository.save(gatewayMessage.getEntity());
		}
	}
	
}

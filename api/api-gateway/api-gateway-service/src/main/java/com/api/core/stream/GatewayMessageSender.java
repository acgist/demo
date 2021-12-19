package com.api.core.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.api.core.asyn.config.GatewaySenderBinding;
import com.api.core.asyn.pojo.message.GatewayMessage;
import com.api.data.asyn.pojo.entity.GatewayEntity;

/**
 * 网关信息放入消息队列
 */
@EnableBinding(GatewaySenderBinding.class)
public class GatewayMessageSender {

    @Autowired
    @Output(GatewaySenderBinding.GATEWAY_SENDER_STREAM_BINDER)
    private MessageChannel channel;
	
	public void send(GatewayEntity entity) {
		GatewayMessage message = new GatewayMessage();
		message.setEntity(entity);
		channel.send(MessageBuilder.withPayload(message).build());
	}
	
}

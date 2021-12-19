package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 网关信息发送
 */
public interface GatewaySenderBinding {

	String GATEWAY_SENDER_STREAM_BINDER = "api_message_gateway_sender";
	
    @Output(GATEWAY_SENDER_STREAM_BINDER)
    MessageChannel output();

}

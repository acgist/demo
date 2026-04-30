package com.api.core.asyn.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 网关信息接收
 */
public interface GatewayReceiverBinding {

	String GATEWAY_RECEIVER_STREAM_BINDER = "api_message_gateway_receiver";
	
    @Input(GATEWAY_RECEIVER_STREAM_BINDER)
    SubscribableChannel input();

}

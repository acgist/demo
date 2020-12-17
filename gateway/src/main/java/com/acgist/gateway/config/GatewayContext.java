package com.acgist.gateway.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.acgist.gateway.executor.GatewayExecutor;

@Component
public class GatewayContext implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof GatewayExecutor) {
			System.out.println(beanName);
			System.out.println(bean);
		}
		return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
	}
	
}

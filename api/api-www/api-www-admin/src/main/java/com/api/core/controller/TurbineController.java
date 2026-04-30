package com.api.core.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Flux;
import rx.subjects.PublishSubject;

/**
 * controller - 替换默认TurbineController
 */
public class TurbineController extends org.springframework.cloud.netflix.turbine.stream.TurbineController {

	public TurbineController(PublishSubject<Map<String, Object>> hystrixSubject) {
		super(hystrixSubject);
	}

	/**
	 * 修改turbine.stream地址
	 */
	@GetMapping(value = "/actuator/turbine.stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> stream() {
		return super.stream();
	}

}

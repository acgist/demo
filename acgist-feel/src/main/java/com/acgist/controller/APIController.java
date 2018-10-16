package com.acgist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.bean.vo.Message;
import com.acgist.bean.vo.MessageBuilder;
import com.acgist.service.PredictionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 接口
 */
@Api(value = "心情预测")
@RestController
@RequestMapping(value = "/api")
public class APIController {

	@Autowired
	private PredictionService predictionService;
	
	@ApiOperation(value = "心情预测", notes = "通过提交的人脸数据推测心情，如果同一个用户ID一天多次提交人脸数据，录取最后一次数据。")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uid", required = true, dataType = "String", paramType = "form", value = "用户ID，用户唯一标识。"),
		@ApiImplicitParam(name = "face", required = true, dataType = "String", paramType = "form", value = "用户人脸数据，图片通过base64编码的数据。")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Message.class)
	})
	@RequestMapping(value = "/face/push", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "application/json")
	public Map<String, String> push(String uid, String face) {
		predictionService.prediction(uid, face);
		return MessageBuilder.success().buildMessage();
	}
	
	@ApiOperation(value = "心情纠正", notes = "通过提交今天的心情纠正预测结果。")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uid", required = true, dataType = "String", paramType = "form", value = "用户ID，用户唯一标识。"),
		@ApiImplicitParam(name = "feel", required = true, dataType = "String", paramType = "form", value = "用户今天的心情，JSON数组。")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK", response = Message.class)
	})
	@RequestMapping(value = "/face/tick", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "application/json")
	public Map<String, String> tick(String uid, String feel) {
		return MessageBuilder.success().buildMessage();
	}
	
}

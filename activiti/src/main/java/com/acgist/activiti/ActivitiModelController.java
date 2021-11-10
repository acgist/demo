package com.acgist.activiti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 编辑页面
 */
@RestController
public class ActivitiModelController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiModelController.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 国际化
	 * 
	 * @return 国际化内容
	 */
	@RequestMapping(value = "/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public @ResponseBody String stencilset() {
		try (final InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.zh.json")) {
			return IOUtils.toString(stencilsetStream, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			LOGGER.error("国际化异常", e);
		}
		return null;
	}

	@RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET, produces = "application/json")
	public ObjectNode load(@PathVariable String modelId) {
		final Model model = this.repositoryService.getModel(modelId);
		if (model == null) {
			// TODO：自定义提示
			return null;
		}
		ObjectNode modelNode = null;
		try {
			if (StringUtils.isNotEmpty(model.getMetaInfo())) {
				modelNode = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());
			} else {
				modelNode = this.objectMapper.createObjectNode();
				modelNode.put(ModelDataJsonConstants.MODEL_NAME, model.getName());
			}
			modelNode.put(ModelDataJsonConstants.MODEL_ID, model.getId());
			final byte[] jsonData = this.repositoryService.getModelEditorSource(model.getId());
			final ObjectNode jsonModel = (ObjectNode) this.objectMapper.readTree(new String(jsonData, StandardCharsets.UTF_8));
			modelNode.set("model", jsonModel);
		} catch (JsonProcessingException e) {
			LOGGER.error("加载流程模板异常：{}", modelId, e);
		}
		return modelNode;
	}

	@RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId, String name, String description, String json_xml, String svg_xml) {
		final Model model = this.repositoryService.getModel(modelId);
		if (model == null) {
			LOGGER.warn("保存流程模板失败：{}", modelId);
			return;
		}
		// JSON
		try {
			final ObjectNode modelJson = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());
			modelJson.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			model.setName(name);
			model.setMetaInfo(modelJson.toString());
			this.repositoryService.saveModel(model);
			this.repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes(StandardCharsets.UTF_8));
		} catch (JsonProcessingException e) {
			LOGGER.error("保存流程模板异常：{}", modelId, e);
		}
		// SVG
		try (
			final InputStream svgInputStream = new ByteArrayInputStream(svg_xml.getBytes(StandardCharsets.UTF_8));
			final ByteArrayOutputStream svgByteArrayOutputStream = new ByteArrayOutputStream();
		) {
			final PNGTranscoder pngTranscoder = new PNGTranscoder();
			final TranscoderInput svgTranscoderInput = new TranscoderInput(svgInputStream);
			final TranscoderOutput svgTranscoderOutput = new TranscoderOutput(svgByteArrayOutputStream);
			pngTranscoder.transcode(svgTranscoderInput, svgTranscoderOutput);
			final byte[] svgBytes = svgByteArrayOutputStream.toByteArray();
			this.repositoryService.addModelEditorSourceExtra(model.getId(), svgBytes);
		} catch (IOException | TranscoderException e) {
			LOGGER.error("保存流程模板异常：{}", modelId, e);
		}
	}
}

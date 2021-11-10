package com.acgist.activiti;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程定义：model、deployment、processDefinition
 */
@SpringBootTest
public class RepostitoryTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RepostitoryTest.class);

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 创建流程<br />
	 * 页面修改访问：redirect:/modeler.html?modelId=id
	 */
	@Test
	public void testModelBuild() {
		String name = "请假";
		String description = "请假流程";
		String revision = "1";
		final Model model = this.repositoryService.newModel();
		final ObjectNode modelNode = this.objectMapper.createObjectNode();
		modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
		modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		model.setName(name);
//		model.setKey(key);
//		model.setCategory(category); // 设置分类
		model.setMetaInfo(modelNode.toString());
		this.repositoryService.saveModel(model);
		final ObjectNode stencilSetNode = this.objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		final ObjectNode editorNode = this.objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		editorNode.set("stencilset", stencilSetNode);
		this.repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes());
		LOGGER.info("创建模板：{}", model.getId());
	}
	
	@Test
	public void testModelList() {
//		this.repositoryService.createModelQuery().count();
//		this.repositoryService.createModelQuery().asc().listPage(0, 0);
//		this.repositoryService.createModelQuery().singleResult();
		this.repositoryService.createModelQuery().list().forEach(model -> {
			LOGGER.info("模板：{}", model);
		});
	}
	
	@Test
	public void testModelDelete() {
		this.repositoryService.createModelQuery().list().forEach(model -> {
//			this.repositoryService.deleteDeployment(null);
			this.repositoryService.deleteModel(model.getId());
		});
		assertTrue(this.repositoryService.createModelQuery().count() == 0L);
	}
	
	@Test
	public void testDeployment() throws IOException {
		final String id = "85001";
		// 部署之前请使用页面画好流程
		final Model model = this.repositoryService.getModel(id);
		final byte[] modelBytes = this.repositoryService.getModelEditorSource(id);
		final JsonNode editorNode = this.objectMapper.readTree(modelBytes);
		final BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
		final BpmnModel bpmnModel = bpmnJsonConverter.convertToBpmnModel(editorNode);
		final BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		// 注意后缀不能删除
		final String processName = model.getName() + ".bpmn20.xml";
		final byte[] bpmnModelBytes = bpmnXMLConverter.convertToXML(bpmnModel);
		final Deployment deployment = this.repositoryService.createDeployment()
			.name(processName)
			.addString(processName, new String(bpmnModelBytes))
			.deploy();
		// 更新模型
		model.setDeploymentId(deployment.getId());
		this.repositoryService.saveModel(model);
	}
	
	@Test
	public void testDeploymentList() {
		this.repositoryService.createDeploymentQuery().list().forEach(deployment -> {
			LOGGER.info("流程部署：{}", deployment);
		});
		this.repositoryService.createProcessDefinitionQuery().list().forEach(processDefinition -> {
			LOGGER.info("流程定义：{}", processDefinition);
		});
	}
	
	@Test
	public void testDeploymentFile() throws IOException {
		final String id = "请假:2:105004";
		final File png = new File("./activiti.png");
		final File xml = new File("./activiti.bpmn20.xml");
		FileOutputStream output = new FileOutputStream(png);
		InputStream input = this.repositoryService.getProcessDiagram(id);
		final BufferedImage image = ImageIO.read(input);
		ImageIO.write(image, "png", output);
		input.close();
		output.close();
		output = new FileOutputStream(xml);
		input = this.repositoryService.getProcessModel(id);
		IOUtils.copy(input, output);
		input.close();
		output.close();
	}
	
	@Test
	public void testDeploymentDelete() {
		this.repositoryService.createDeploymentQuery().list().forEach(deployment -> {
			// 级联删除：清空所有信息
			this.repositoryService.deleteDeployment(deployment.getId(), true);
		});
	}
	
}

package com.acgist.activiti;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 流程：列表、添加、修改、删除 用户：开始、执行、列表
 */
@Controller
@RequestMapping("/activiti")
public class ActivitiController {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RepositoryService repositoryService;

	@RequestMapping("/create")
	public String create(String name, String description) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		if (StringUtils.isEmpty(description)) {
			description = name;
		}
		String id = null;
		String key = name;
		String revision = "1";
		final Model model = this.repositoryService.newModel();
		final ObjectNode modelNode = this.objectMapper.createObjectNode();
		modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
		modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		model.setKey(key);
		model.setName(name);
//		model.setCategory(category);
		model.setMetaInfo(modelNode.toString());
		this.repositoryService.saveModel(model);
		id = model.getId();
		final ObjectNode stencilSetNode = this.objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		final ObjectNode editorNode = this.objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		editorNode.set("stencilset", stencilSetNode);
		this.repositoryService.addModelEditorSource(id, editorNode.toString().getBytes());
		return "redirect:/modeler.html?modelId=" + id;
	}

}

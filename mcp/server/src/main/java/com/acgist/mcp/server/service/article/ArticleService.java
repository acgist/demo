package com.acgist.mcp.server.service.article;

import java.util.List;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    
    private final OllamaApi ollamaApi;
    
    @Tool(description = "批量生成标题")
    public List<String> batchGenerateTitle(String title) {
        final String systemPrompt = """
# 角色
你是一位专业的文章润色助手，擅长提升文章的语言表达、逻辑结构和整体流畅性。你的任务是根据用户提供的原始文本，进行细致的润色，使文章更加清晰、生动和有说服力。
## 技能
### 技能 1: 语言润色
- 优化文章中的词汇选择，使其更加精准和生动。
- 调整句子结构，使文章更加流畅自然。
- 修正语法错误和拼写错误，确保文章的专业性和准确性。
### 技能 2: 逻辑结构优化
- 重新组织段落顺序，使文章的逻辑更加清晰。
- 添加或删除内容，以增强文章的连贯性和一致性。
- 提供适当的过渡句，使各部分之间的衔接更加自然。
### 技能 3: 内容丰富与精简
- 根据需要添加更多细节，使文章内容更加丰富和具体。
- 删除冗余信息，使文章更加简洁明了。
- 保持文章的主题和核心思想不变，同时提升其吸引力和可读性。
### 技能 4: 风格调整
- 根据用户的指示调整文章的风格，如正式、幽默、轻松等。
- 确保文章的风格与目标受众相匹配。
- 保持一致的语气和语调，增强文章的整体效果。
## 限制
- 只对用户提供的一篇文章进行润色，不涉及其他文档。
- 润色过程中保持原文的核心内容和意图不变。
- 不得添加或删除与文章主题无关的内容。
- 如果用户提供了特定的风格要求，请严格遵循这些要求进行润色。
- 在润色时，注意保留用户的个人风格和声音，避免过度修改。
""";
        final String userPrompt = "用户输入内容如下：" + title;
        try {
            return List.of(this.callWithMessage(systemPrompt, userPrompt));
        } catch (Exception e) {
            log.error("系统异常", e);
            return List.of("系统异常：" + e.getMessage());
        }
    }

    @Tool(description = "文章润色")
    public List<String> polishTheArticle(String content) {
        final String systemPrompt = """
# 角色
你是一位专业的文章润色助手，擅长提升文章的语言表达、逻辑结构和整体流畅性。你的任务是根据用户提供的原始文本，进行细致的润色，使文章更加清晰、生动和有说服力。
## 技能
### 技能 1: 语言润色
- 优化文章中的词汇选择，使其更加精准和生动。
- 调整句子结构，使文章更加流畅自然。
- 修正语法错误和拼写错误，确保文章的专业性和准确性。
### 技能 2: 逻辑结构优化
- 重新组织段落顺序，使文章的逻辑更加清晰。
- 添加或删除内容，以增强文章的连贯性和一致性。
- 提供适当的过渡句，使各部分之间的衔接更加自然。
### 技能 3: 内容丰富与精简
- 根据需要添加更多细节，使文章内容更加丰富和具体。
- 删除冗余信息，使文章更加简洁明了。
- 保持文章的主题和核心思想不变，同时提升其吸引力和可读性。
### 技能 4: 风格调整
- 根据用户的指示调整文章的风格，如正式、幽默、轻松等。
- 确保文章的风格与目标受众相匹配。
- 保持一致的语气和语调，增强文章的整体效果。
## 限制
- 只对用户提供的一篇文章进行润色，不涉及其他文档。
- 润色过程中保持原文的核心内容和意图不变。
- 不得添加或删除与文章主题无关的内容。
- 如果用户提供了特定的风格要求，请严格遵循这些要求进行润色。
- 在润色时，注意保留用户的个人风格和声音，避免过度修改。
""";
        final String userPrompt = "原始文本内容如下：" + content;
        try {
            return List.of(this.callWithMessage(systemPrompt, userPrompt));
        } catch (Exception e) {
            log.error("系统异常", e);
            return List.of("系统异常：" + e.getMessage());
        }
    }

    private String callWithMessage(String systemPrompt, String userPrompt) throws Exception {
        final ChatRequest request = ChatRequest.builder("deepseek-r1:7b").messages(List.of(
            Message.builder(Role.USER).content(userPrompt).build(),
            Message.builder(Role.SYSTEM).content(userPrompt).build()
        )).build();
        final ChatResponse response = this.ollamaApi.chat(request);
        log.debug("执行时间：{}", response.getTotalDuration());
        final String content = response.message().content();
        return content == null || content.isEmpty() ? "没有答案" : content;
    }
    
}

package org.goormthon.seasonthon.nocheongmaru.global.openai.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.config.RestClientConfig;
import org.goormthon.seasonthon.nocheongmaru.global.exception.openai.ContentViolationException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.openai.OpenAiHttpClientException;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.dto.ChatCompletionRequest;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.dto.ChatCompletionResponse;
import org.goormthon.seasonthon.nocheongmaru.global.openai.provider.dto.ChatMessage;
import org.goormthon.seasonthon.nocheongmaru.global.openai.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FilteringProvider {
    
    @Value("${openai.model}")
    private String model;
    
    private final RestClientConfig restClientConfig;
    private final ObjectMapper objectMapper;
    
    public void validateViolent(String content) {
        try {
            RestClient client = restClientConfig.openAiRestClient();
            List<ChatMessage> messages = buildMessages(content);
            ChatCompletionRequest request = new ChatCompletionRequest(model, messages, 0.0, ChatCompletionRequest.JsonResponseFormat.JSON_OBJECT);
            ChatCompletionResponse response = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ChatCompletionResponse.class);
            
            String contentJson = extractContent(response);
            if (contentJson == null || contentJson.isBlank()) {
                throw new OpenAiHttpClientException();
            }
            JsonNode node = objectMapper.readTree(contentJson);
            boolean violation = node.path("violation").asBoolean(false);
            if (violation) {
                throw new ContentViolationException();
            }
        } catch (ContentViolationException e) {
            throw e;
        } catch (Exception e) {
            log.error("OpenAI chat moderation error: {}", e.getMessage());
            throw new OpenAiHttpClientException();
        }
    }
    
    private String extractContent(ChatCompletionResponse response) {
        if (response == null || response.choices() == null || response.choices().isEmpty()) {
            return null;
        }
        var msg = response.choices().getFirst().message();
        return msg != null ? msg.content() : null;
    }
    
    private List<ChatMessage> buildMessages(String content) {
        String system = PromptTemplate.SAFETY_SYSTEM.text();
        String user = PromptTemplate.SAFETY_USER.format(content);
        return List.of(
            new ChatMessage("system", system),
            new ChatMessage("user", user)
        );
    }
    
}

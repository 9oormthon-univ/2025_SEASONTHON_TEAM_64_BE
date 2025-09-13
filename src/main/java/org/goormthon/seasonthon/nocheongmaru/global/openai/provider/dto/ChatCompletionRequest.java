package org.goormthon.seasonthon.nocheongmaru.global.openai.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatCompletionRequest(
    String model,
    List<ChatMessage> messages,
    Double temperature,
    JsonResponseFormat response_format
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record JsonResponseFormat(String type) {
        public static final JsonResponseFormat JSON_OBJECT = new JsonResponseFormat("json_object");
    }
}


package org.goormthon.seasonthon.nocheongmaru.global.openai.provider.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatMessage(
    String role,
    String content
) {}


package org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record KakaoGeocodingApiResponse(
    List<Document> documents
) {}

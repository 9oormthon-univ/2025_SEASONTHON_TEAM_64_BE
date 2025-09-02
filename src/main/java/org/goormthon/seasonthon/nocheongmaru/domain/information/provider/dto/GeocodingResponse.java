package org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto;

import lombok.Builder;

@Builder
public record GeocodingResponse(
    
    Double latitude,
    Double longitude
    
) {}

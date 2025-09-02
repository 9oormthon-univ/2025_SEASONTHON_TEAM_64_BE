package org.goormthon.seasonthon.nocheongmaru.domain.information.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.response.Document;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.response.KakaoGeocodingApiResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.information.provider.dto.GeocodingResponse;
import org.goormthon.seasonthon.nocheongmaru.global.config.RestClientConfig;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.AddressNotFoundException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.information.KakaoHttpClientException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoGeocodingProvider {
    
    private static final String GEOCODING_URL =
        "/local/search/address.json?analyze_type=exact&page=1&size=10&query=%s";
    
    private final RestClientConfig restClientConfig;
    
    public GeocodingResponse getGeocodingByAddress(String address) {
        try {
            String url = String.format(GEOCODING_URL, address);
            
            KakaoGeocodingApiResponse response = restClientConfig.kakaoRestClient()
                .get()
                .uri(url)
                .retrieve()
                .body(KakaoGeocodingApiResponse.class);
            
            if (response == null || response.documents().isEmpty()) {
                throw new AddressNotFoundException();
            }
            
            Document addr = response.documents().getFirst();
            
            return GeocodingResponse.builder()
                .latitude(Double.parseDouble(addr.y()))
                .longitude(Double.parseDouble(addr.x()))
                .build();
            
        } catch (Exception e) {
            log.error("Kakao Geocoding API error: {}", e.getMessage());
            throw new KakaoHttpClientException();
        }
    }
    
}



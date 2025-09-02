package org.goormthon.seasonthon.nocheongmaru.domain.information.repository;

import org.goormthon.seasonthon.nocheongmaru.domain.information.service.dto.response.InformationResponse;

import java.util.List;

public interface InformationCustomRepository {
    
    List<InformationResponse> getInformationList(Long lastId, String category, Boolean sortByRecent);
    
}

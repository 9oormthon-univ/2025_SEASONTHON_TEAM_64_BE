package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.mission.entity.Mission;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class FeedGenerator {
    
    private final S3StorageUtil s3StorageUtil;
    private final FeedRepository feedRepository;
    
    @Transactional
    public Long generateFeed(Member member, Mission mission, String description, MultipartFile imageFile) {
        String imageUrl = s3StorageUtil.uploadFileToS3(imageFile);
        
        Feed feed = Feed.builder()
            .member(member)
            .mission(mission)
            .description(description)
            .imageUrl(imageUrl)
            .build();
        feedRepository.save(feed);
        
        return feed.getId();
    }
    
}

package org.goormthon.seasonthon.nocheongmaru.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.entity.Feed;
import org.goormthon.seasonthon.nocheongmaru.domain.feed.repository.feed.FeedRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.global.exception.feed.IsNotFeedOwnerException;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class FeedEditor {
    
    private final S3StorageUtil s3StorageUtil;
    private final FeedRepository feedRepository;
    
    @Transactional
    public void modifyFeed(Member member, Long feedId, String description, MultipartFile imageFile) {
        validateFeedOwnership(member.getId(), feedId);
        Feed feed = feedRepository.findById(feedId);
        
        String prevImageUrl = feed.getImageUrl();
        s3StorageUtil.deleteFileFromS3(prevImageUrl);
        
        String newImageUrl = s3StorageUtil.uploadFileToS3(imageFile);
        feed.modifyFeed(description, newImageUrl);
    }
    
    @Transactional
    public void deleteFeed(Member member, Long feedId) {
        validateFeedOwnership(member.getId(), feedId);
        Feed feed = feedRepository.findById(feedId);
        
        String imageUrl = feed.getImageUrl();
        s3StorageUtil.deleteFileFromS3(imageUrl);
        
        feedRepository.delete(feed);
    }
    
    private void validateFeedOwnership(Long memberId, Long feedId) {
        if(!feedRepository.existsByMemberIdAndId(memberId, feedId)) {
            throw new IsNotFeedOwnerException();
        }
    }
    
}

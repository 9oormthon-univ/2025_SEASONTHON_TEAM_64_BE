package org.goormthon.seasonthon.nocheongmaru.domain.image.service;

import org.goormthon.seasonthon.nocheongmaru.IntegrationTestSupport;
import org.goormthon.seasonthon.nocheongmaru.domain.image.entity.Image;
import org.goormthon.seasonthon.nocheongmaru.domain.image.repository.ImageRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Category;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;
import org.goormthon.seasonthon.nocheongmaru.domain.information.repository.InformationRepository;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Member;
import org.goormthon.seasonthon.nocheongmaru.domain.member.entity.Role;
import org.goormthon.seasonthon.nocheongmaru.domain.member.repository.MemberRepository;
import org.goormthon.seasonthon.nocheongmaru.global.s3.S3StorageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

class ImageManagerTest extends IntegrationTestSupport {

    @Autowired
    private ImageManager imageManager;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockitoBean
    private S3StorageUtil s3StorageUtil;

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        informationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("이미지 목록을 저장한다.")
    @Test
    void saveImages() {
        // given
        Information information = persistInformation();
        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", MediaType.IMAGE_PNG_VALUE, "i1".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", MediaType.IMAGE_PNG_VALUE, "i2".getBytes());
        given(s3StorageUtil.uploadFileToS3(image1)).willReturn("s3://bucket/image1.png");
        given(s3StorageUtil.uploadFileToS3(image2)).willReturn("s3://bucket/image2.png");

        // when
        imageManager.saveImages(information, List.of(image1, image2));

        // then
        List<Image> saved = imageRepository.findAllByInformationId(information.getId());
        assertThat(saved).hasSize(2);
        assertThat(saved.stream().map(Image::getImageUrl)).containsExactlyInAnyOrder("s3://bucket/image1.png", "s3://bucket/image2.png");
    }

    @DisplayName("이미지 목록이 null/empty/빈파일이면 저장하지 않는다.")
    @Test
    void saveImages_WithoutFiles() {
        // given
        Information information = persistInformation();
        MockMultipartFile emptyFile = new MockMultipartFile("empty", "empty.png", MediaType.IMAGE_PNG_VALUE, new byte[0]);

        // when
        imageManager.saveImages(information, null);
        imageManager.saveImages(information, List.of());
        imageManager.saveImages(information, List.of(emptyFile));

        // then
        List<Image> saved = imageRepository.findAllByInformationId(information.getId());
        assertThat(saved).isEmpty();
        verify(s3StorageUtil, never()).uploadFileToS3(any());
    }

    @DisplayName("이미지 목록을 교체하면 기존 S3/DB 이미지를 삭제하고 새 이미지를 저장한다.")
    @Test
    void replaceImages() {
        // given
        Information information = persistInformation();
        imageRepository.save(Image.builder().imageUrl("s3://bucket/old1.png").information(information).build());
        imageRepository.save(Image.builder().imageUrl("s3://bucket/old2.png").information(information).build());

        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.png", MediaType.IMAGE_PNG_VALUE, "i1".getBytes());
        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.png", MediaType.IMAGE_PNG_VALUE, "i2".getBytes());
        given(s3StorageUtil.uploadFileToS3(image1)).willReturn("s3://bucket/new1.png");
        given(s3StorageUtil.uploadFileToS3(image2)).willReturn("s3://bucket/new2.png");

        // when
        imageManager.replaceImages(information.getId(), information, List.of(image1, image2));

        // then
        verify(s3StorageUtil, times(1)).deleteFileFromS3("s3://bucket/old1.png");
        verify(s3StorageUtil, times(1)).deleteFileFromS3("s3://bucket/old2.png");

        List<Image> saved = imageRepository.findAllByInformationId(information.getId());
        assertThat(saved).hasSize(2);
        assertThat(saved.stream().map(Image::getImageUrl)).containsExactlyInAnyOrder("s3://bucket/new1.png", "s3://bucket/new2.png");
    }

    @DisplayName("정보 삭제 시 연관 이미지들을 S3와 DB에서 모두 삭제한다.")
    @Test
    void deleteAllByInformationId() {
        // given
        Information information = persistInformation();
        imageRepository.save(Image.builder().imageUrl("s3://bucket/old1.png").information(information).build());
        imageRepository.save(Image.builder().imageUrl("s3://bucket/old2.png").information(information).build());

        // when
        imageManager.deleteAllByInformationId(information.getId());

        // then
        verify(s3StorageUtil, times(1)).deleteFileFromS3("s3://bucket/old1.png");
        verify(s3StorageUtil, times(1)).deleteFileFromS3("s3://bucket/old2.png");
        assertThat(imageRepository.findAllByInformationId(information.getId())).isEmpty();
    }

    @DisplayName("연관 이미지가 없으면 삭제를 시도하지 않는다.")
    @Test
    void deleteAllByInformationId_NoImages() {
        // given
        Information information = persistInformation();

        // when
        imageManager.deleteAllByInformationId(information.getId());

        // then
        verify(s3StorageUtil, never()).deleteFileFromS3(any());
        assertThat(imageRepository.findAllByInformationId(information.getId())).isEmpty();
    }

    private Information persistInformation() {
        Member member = Member.builder()
            .nickname("nickname")
            .email("test@test.com")
            .profileImageURL("profileImageURL")
            .role(Role.ROLE_USER)
            .build();
        memberRepository.save(member);

        Information information = Information.builder()
            .member(member)
            .title("title")
            .description("description")
            .address("address")
            .category(Category.HOSPITAL_FACILITIES)
            .latitude(37.1)
            .longitude(127.1)
            .build();
        informationRepository.save(information);
        return information;
    }
}
package org.goormthon.seasonthon.nocheongmaru.global.s3;

import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.exception.s3.ImageDeleteException;
import org.goormthon.seasonthon.nocheongmaru.global.exception.s3.ImageUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class S3StorageUtil {
    
    private final S3Client s3Client;
    private final String bucketName;
    
    public S3StorageUtil(
        S3Client s3Client,
        @Value("${cloud.aws.s3.bucket}") String bucketName
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }
    
    public String uploadFileToS3(MultipartFile file) {
        String fileName = generateFileName(file.getOriginalFilename());
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(file.getContentType())
            .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("파일 업로드 성공: {}", fileName);
        } catch (IOException e) {
            log.error("내부 서버 파일 업로드 실패: {}", e.getMessage(), e);
            throw new ImageUploadException();
        } catch (S3Exception e) {
            log.error("S3 파일 업로드 실패: {}", e.awsErrorDetails().errorMessage(), e);
            throw new ImageUploadException();
        }
        
        return getFileURL(fileName);
    }
    
    public void deleteFileFromS3(String fileUrl) {
        try {
            String fileName = extractFileNameFromURL(fileUrl);
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(fileName));
            log.info("파일 삭제 성공: {}", fileName);
        } catch (S3Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.awsErrorDetails().errorMessage(), e);
            throw new ImageDeleteException();
        }
    }
    
    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    
    private String generateFileName(String originalFilename) {
        return UUID.randomUUID() + getExtension(originalFilename);
    }
    
    private String extractFileNameFromURL(String fileURL) {
        int lastSlashIndex = fileURL.lastIndexOf("/");
        if (lastSlashIndex != -1 && lastSlashIndex + 1 < fileURL.length()) {
            return fileURL.substring(lastSlashIndex + 1);
        }
        return fileURL;
    }
    
    private String getFileURL(String fileName) {
        GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(fileName).build();
        return String.valueOf(s3Client.utilities()
            .getUrl(getUrlRequest)
        );
    }
    
}

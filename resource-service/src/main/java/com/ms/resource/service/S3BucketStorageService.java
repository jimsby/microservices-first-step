package com.ms.resource.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ms.resource.dto.StorageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class S3BucketStorageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(File file, StorageDto storageDto) {
        try {
            String path = Optional.ofNullable(storageDto.getPath()).isEmpty() ? "" : storageDto.getPath().replaceFirst("/", "") + "/";
            amazonS3Client.putObject(new PutObjectRequest(storageDto.getBucket(),path + file.getName(), file));
        } catch (AmazonServiceException e) {
            log.error("S3 Failed to upload the file: " + file.getName());
            throw new RuntimeException("Failed to upload the file", e);
        }
    }

    public byte[] downloadFile(String fileName, StorageDto storageDto) {
        try {
            String path = Optional.ofNullable(storageDto.getPath()).isEmpty() ? "" : storageDto.getPath().replaceFirst("/", "") + "/";
            S3Object s3Object = amazonS3Client.getObject(storageDto.getBucket(), path + fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException | AmazonServiceException e) {
            log.error("S3 Failed to download the file: " + fileName);
            throw new RuntimeException("Failed to download the file", e);
        }
    }


    public void deleteFile(String fileName, StorageDto storageDto) {
        String path = Optional.ofNullable(storageDto.getPath()).isEmpty() ? "" : storageDto.getPath().replaceFirst("/", "") + "/";
        amazonS3Client.deleteObject(storageDto.getBucket(), path + fileName);
    }
}

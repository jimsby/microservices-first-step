package com.ms.resource.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.ms.resource.util.FileUtil.convertMultiPartFileToFile;

@Service
@Slf4j
public class S3BucketStorageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    public void uploadFile(File file) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file));
        } catch (AmazonServiceException e) {
            log.error("S3 Failed to upload the file: " + file.getName());
            throw new RuntimeException("Failed to upload the file", e);
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucketName, fileName);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException | AmazonServiceException e) {
            log.error("S3 Failed to download the file: " + fileName);
            throw new RuntimeException("Failed to download the file", e);
        }
    }


    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
    }
}

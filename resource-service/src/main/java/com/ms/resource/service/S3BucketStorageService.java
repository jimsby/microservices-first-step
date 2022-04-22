package com.ms.resource.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ms.resource.dto.StorageDto;
import com.ms.resource.model.AudioFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

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

    public void moveToPermanent(AudioFile audioFile, StorageDto source, StorageDto destination) {
        String pathSource = Optional.ofNullable(source.getPath()).isEmpty()
                ? audioFile.getFileName()
                : source.getPath().replaceFirst("/", "") + "/" + audioFile.getFileName();

        String pathDestination = Optional.ofNullable(destination.getPath()).isEmpty()
                ? audioFile.getFileName()
                : destination.getPath().replaceFirst("/", "") + "/" + audioFile.getFileName();

/*
//          This method doesn't work with path param, and spaces in filename
 */
//        amazonS3Client.copyObject(source.getBucket(), pathSource, destination.getBucket(), pathDestination);

        moveToPermanent(pathSource, source, pathDestination, destination);
        amazonS3Client.deleteObject(source.getBucket(), pathSource);
    }

    @Deprecated
    private void moveToPermanent(String pathSource, StorageDto source, String pathDestination, StorageDto destination) {
        S3Object s3Object = amazonS3Client.getObject(source.getBucket(), pathSource);
        File tmp = null;
        try {
            tmp = File.createTempFile(UUID.randomUUID().toString(), "");
            FileUtils.copyInputStreamToFile(s3Object.getObjectContent(), tmp);
            amazonS3Client.putObject(destination.getBucket(), pathDestination, tmp);
            tmp.delete();
        } catch (IOException ignore) {

        }finally {
            tmp.delete();
        }
    }
}

package com.ms.resource.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class S3BucketStorageServiceTest {
    private static File mp3 = new File("src/test/resources/file_example_MP3_700KB.mp3");
    private static String bucketName = "testBucket";

    @Mock
    AmazonS3 s3;

    @InjectMocks
    S3BucketStorageService service;

    @Test
    void whenInitializingAWSS3Service_thenNotNull() {
        assertNotNull(s3);
    }

    @Test
    void testFileAvailability(){
        System.out.println(mp3.length());
    }

    @BeforeEach
    void setBucketName(){
        ReflectionTestUtils.setField(service, "bucketName", bucketName);
    }

    @Test
    void uploadFile() {
        PutObjectResult result = mock(PutObjectResult.class);
        when(s3.putObject((PutObjectRequest) any())).thenReturn(result);

        service.uploadFile(mp3);
        verify(s3).putObject((PutObjectRequest) any());
    }

    @Test
    void downloadFile() throws IOException {
        S3Object object = new S3Object();
        object.setObjectContent(new FileInputStream(mp3));
        when(s3.getObject(bucketName, mp3.getName())).thenReturn(object);

        assertEquals(service.downloadFile(mp3.getName()).length, FileUtils.readFileToByteArray(mp3).length);
    }

    @Test
    void deleteFile() {
        service.deleteFile(mp3.getName());

        verify(s3).deleteObject(bucketName, mp3.getName());
    }
}
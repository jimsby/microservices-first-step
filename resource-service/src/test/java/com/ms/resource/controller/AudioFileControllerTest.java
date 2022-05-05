package com.ms.resource.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.resource.config.RabbitMockConfig;
import com.ms.resource.config.S3MockConfig;
import com.ms.resource.config.StorageServiceConfig;
import com.ms.resource.dto.ResponseCustomIdsDto;
import com.ms.resource.dto.StorageDto;
import com.ms.resource.model.AudioFile;
import com.ms.resource.repository.AudioFileRepository;
import com.ms.resource.service.S3BucketStorageService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.yaml")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import({S3MockConfig.class, RabbitMockConfig.class, StorageServiceConfig.class})
class AudioFileControllerTest {

    private static File mp3 = new File("src/test/resources/file_example_MP3_700KB.mp3");
    private static StorageDto stagingStorage = new StorageDto(1, "STAGING", "testmp3bucket");
    private static StorageDto permanentStorage = new StorageDto(2, "PERMANENT", "permanentbucket");
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private AudioFileRepository repository;

    @Autowired
    S3BucketStorageService s3BucketStorageService;



    @BeforeEach
    void setUp(){
        repository.save(new AudioFile(1, mp3.getName(), 1));
        repository.save(new AudioFile(2, "2_" + mp3.getName(), 1));
        s3BucketStorageService.uploadFile(mp3, stagingStorage);
    }

    @Test
    void uploadFile() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file",
                        mp3.getName(),
                        String.valueOf(MediaType.APPLICATION_OCTET_STREAM),
                        FileUtils.readFileToByteArray(mp3));
        mvc.perform(multipart("/resources")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ResponseCustomIdsDto(1))));
    }

    @Test
    void getNotFound() throws Exception {
        mvc.perform(get("/resources/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void downloadFile() throws Exception {
        mvc.perform(get("/resources/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string("Content-Length", String.valueOf(mp3.length())));
    }

    @Test
    void deleteFile() throws Exception {
        mvc.perform(delete("/resources?ids=1,10,11,12,13"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ResponseCustomIdsDto(List.of(1)))));
    }
}
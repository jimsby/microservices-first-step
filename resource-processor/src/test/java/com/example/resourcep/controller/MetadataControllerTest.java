package com.example.resourcep.controller;

import com.example.resourcep.config.RabbitMockConfig;
import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@SpringBootTest
@WireMockTest(httpPort = 9561)
@Import(RabbitMockConfig.class)
class MetadataControllerTest {
    private static File mp3 = new File("src/test/resources/file_example_MP3_700KB.mp3");

    @Autowired
    MetadataController controller;

    @Autowired
    ObjectMapper mapper;

    @Test
    @Disabled
    void createMetadata(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        stubFor(get(urlMatching("/resources/1"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("content-disposition", "filename=\"filename.mp3\"")
                        .withBody(FileUtils.readFileToByteArray(mp3))));
        stubFor(post(urlMatching("/songs"))
                .willReturn(aResponse().withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mapper.writeValueAsString(new ResponseCustomIdsDto(1)))));

        controller.createMetadata(new ResponseCustomIdsDto(1));

        verify(getRequestedFor(urlEqualTo("/resources/1")));
        verify(postRequestedFor(urlEqualTo("/songs")));

    }
}
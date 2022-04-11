package com.ms.songs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.songs.dto.ResponseCustomIdsDto;
import com.ms.songs.dto.SongMetadataDto;
import com.ms.songs.model.SongMetadata;
import com.ms.songs.repository.SongMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.yaml")
@AutoConfigureMockMvc
class SongMetadataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private SongMetadataRepository repository;

    @BeforeEach
    void baseInit(){
        repository.save(new SongMetadata(12, "NameTest", "ArtistTest",
                "AlbumTest", 2.5, 2022));
        repository.save(new SongMetadata(13, "NameTest2", "ArtistTest2",
                "AlbumTest2", 3.5, 2021));
    }

    @Test
    void createMetadata() throws Exception {
        SongMetadataDto testData = new SongMetadataDto(123, "NameTest3", "ArtistTest3",
                "AlbumTest3", 4.5, 2020);

        mvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ResponseCustomIdsDto(123))));
    }

    @Test
    void getNotFound() throws Exception {
        mvc.perform(get("/songs/11"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getMetadata() throws Exception {
        mvc.perform(get("/songs/12"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ArtistTest")));
    }

    @Test
    void deleteMetadata() throws Exception {
        mvc.perform(delete("/songs?ids=10,11,12,13"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ResponseCustomIdsDto(List.of(12,13)))));
    }

    @Test
    void deleteError500() throws Exception {
        mvc.perform(delete("/songs?ids="))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}
package com.ms.songs.service;

import com.ms.songs.dto.SongMetadataDto;
import com.ms.songs.model.SongMetadata;
import com.ms.songs.repository.SongMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongMetadataServiceTest {

    @InjectMocks
    private SongMetadataService songMetadataService;

    @Mock
    private SongMetadataRepository songMetadataRepository;

    @Test
    void createDuplicate() {
        SongMetadataDto testDto = new SongMetadataDto();
        testDto.setResourceId(12);
        testDto.setName("Test Sample");
        SongMetadata test = new SongMetadata();
        test.setId(12);
        test.setName("Test Sample");

        when(songMetadataRepository.findById(12)).thenReturn(Optional.of(test));

        assertEquals(songMetadataService.create(testDto), 12);
    }

    @Test
    void get() {
        SongMetadataDto testDto = new SongMetadataDto();
        testDto.setResourceId(12);
        testDto.setName("Test Sample");
        SongMetadata test = new SongMetadata();
        test.setId(12);
        test.setName("Test Sample");

        when(songMetadataRepository.findById(12)).thenReturn(Optional.ofNullable(test));

        assertEquals(songMetadataService.get(12), testDto);
    }

    @Test
    void delete() {
        SongMetadata test = new SongMetadata();
        test.setId(12);
        test.setName("Test Sample");

        when(songMetadataRepository.findById(12)).thenReturn(Optional.of(test));

        assertTrue(songMetadataService.delete(12));
    }
}
package com.ms.resource.service;

import com.ms.resource.model.AudioFile;
import com.ms.resource.repository.AudioFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AudioFileServiceTest {

    @Mock
    AudioFileRepository audioFileRepository;

    @InjectMocks
    AudioFileService audioFileService;

    @BeforeEach
    void setMockOutput() {
        String fileName = "file_example_MP3_700KB.mp3";
        AudioFile testFile1 = new AudioFile(1, fileName, 1);
        lenient().when(audioFileRepository.findByFileName(fileName))
                .thenReturn(Optional.of(testFile1));
        lenient().when(audioFileRepository.save(new AudioFile(fileName, 1)))
                .thenReturn(testFile1);
        lenient().when(audioFileRepository.findById(1))
                .thenReturn(Optional.of(testFile1));
        lenient().when(audioFileRepository.findById(1))
                .thenReturn(Optional.of(testFile1));
    }

    @Test
    void createAudioFile() {
        assertEquals(audioFileService.createAudioFile("file_example_MP3_700KB.mp3", 1), 1);
    }

    @Test
    void getAudioFile() {
        Optional<AudioFile> optional = audioFileService.getAudioFile(1);

        assertTrue(optional.isPresent());
        assertEquals(optional.get().getId(), 1);
        assertEquals(optional.get().getFileName(), "file_example_MP3_700KB.mp3");
    }

    @Test
    void getAudioFileEmpty() {
        Optional<AudioFile> optional = audioFileService.getAudioFile(2);

        assertTrue(optional.isEmpty());
    }

    @Test
    void delete() {
        audioFileService.delete(1);

        verify(audioFileRepository).deleteById(1);
    }
}
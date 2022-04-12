package com.example.resourcep.service;

import org.apache.tika.metadata.Metadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProcessorServiceTest {
    private static File mp3 = new File("src/test/resources/file_example_MP3_700KB.mp3");

    @Autowired
    ProcessorService service;

    @Test
    void getMetadata() {
        Metadata metadata = service.getMetadata(mp3);
        assertEquals(metadata.get("dc:title"), "Impact Moderato");
        assertEquals(metadata.get("xmpDM:album"), "YouTube Audio Library");
        assertEquals(metadata.get("xmpDM:artist"), "Kevin MacLeod");
    }
}
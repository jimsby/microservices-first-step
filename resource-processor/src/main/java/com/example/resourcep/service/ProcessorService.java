package com.example.resourcep.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
@Data
@AllArgsConstructor
public class ProcessorService {
    private static final String AUDIO_TYPE = "audio/mpeg";
    private Mp3Parser parser;
    private BodyContentHandler handler;
    private Metadata metadata;
    private ParseContext context;
    private Tika tika;

    public Metadata getMetadata(File file){

        try(FileInputStream inputStream = new FileInputStream(file)) {
            parser.parse(inputStream, handler, metadata, context);
            if(tika.detect(file).equals(AUDIO_TYPE)){
                return metadata;
            }
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }
        return new Metadata();
    }
}

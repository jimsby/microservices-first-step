package com.example.resourcep.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MetadataService {
    public static final String AUDIO_TYPE = "audio/mpeg";

    public Metadata getMetadata(File file){
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try(FileInputStream inputStream = new FileInputStream(file)) {
            parser.parse(inputStream, handler, metadata, context);
            Tika tika = new Tika();
            if(tika.detect(file).equals(AUDIO_TYPE)) return metadata;
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package com.example.resourcep.config;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Mp3Parser parser(){
        return new Mp3Parser();
    }

    @Bean
    public BodyContentHandler handler(){
        return new BodyContentHandler();
    }

    @Bean
    public Metadata metadata() {
        return new Metadata();
    }

    @Bean
    public ParseContext context() {
        return new ParseContext();
    }

    @Bean
    public Tika tika(){
        return new Tika();
    }
}

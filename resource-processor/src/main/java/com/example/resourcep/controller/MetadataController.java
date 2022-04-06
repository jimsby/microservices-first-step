package com.example.resourcep.controller;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.example.resourcep.dto.SongMetadataDto;
import com.example.resourcep.service.ProcessorService;
import com.example.resourcep.service.ResourceService;
import com.example.resourcep.service.SongService;
import com.example.resourcep.util.ResourceConverter;
import feign.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
@Data
@Slf4j
@AllArgsConstructor
public class MetadataController {
    private ResourceService resourceService;
    private ProcessorService metadataService;
    private SongService songService;

    public void createMetadata(ResponseCustomIdsDto dto){
        Integer id = dto.getId();
        Response response = resourceService.downloadFile(id);
        File file = ResourceConverter.toFile(response);
        Metadata metadata = metadataService.getMetadata(file);
        file.delete();
        SongMetadataDto sendSongDto = ResourceConverter.toMetadataDto(metadata, id);
        ResponseCustomIdsDto responseSongId = songService.create(sendSongDto);
        if(responseSongId.getId().equals(id)) {
            log.info("Song Service create Metadata (id: " + id + ")");
        }

    }
}

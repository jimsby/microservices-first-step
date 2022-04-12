package com.example.resourcep.util;

import com.example.resourcep.dto.SongMetadataDto;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tika.metadata.Metadata;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ResourceConverter {
    public static File toFile(Response response) {
        String fileName = response.headers()
                .get("content-disposition")
                .toString()
                .split("filename=\"")[1]
                .split("\"")[0];

        try {
            File convertedFile = new File(Objects.requireNonNull(fileName));
            FileUtils.copyInputStreamToFile(response.body().asInputStream(), convertedFile);
            return convertedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SongMetadataDto toMetadataDto(Metadata meta, Integer id){
        SongMetadataDto dto = new SongMetadataDto();
        dto.setResourceId(id);
        dto.setName(meta.get("dc:title"));
        dto.setAlbum(meta.get("xmpDM:album"));
        dto.setArtist(meta.get("xmpDM:artist"));
        try {
            dto.setYear(Integer.valueOf(meta.get("xmpDM:releaseDate")));
        } catch (RuntimeException ignore){}
        try {
            dto.setLength(Double.valueOf(meta.get("xmpDM:duration")));
        } catch (RuntimeException ignore){}

        return dto;
    }
}

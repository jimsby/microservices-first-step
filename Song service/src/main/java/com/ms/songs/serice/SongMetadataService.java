package com.ms.songs.serice;

import com.ms.songs.dto.SongMetadataDto;
import com.ms.songs.model.SongMetadata;
import com.ms.songs.repository.SongMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongMetadataService {
    @Autowired
    private SongMetadataRepository songMetadataRepository;

    public Integer create(SongMetadataDto dto){
        SongMetadata metadata = songMetadataRepository.findById(dto.getResourceId())
                .orElseGet(() -> songMetadataRepository.save(convert(dto)));
        return metadata.getId();
    }

    public SongMetadataDto get(Integer id){
        Optional<SongMetadata> metadata = songMetadataRepository.findById(id);
        if(metadata.isEmpty()) {
            return null;
        }else {
            return convert(metadata.get());
        }
    }

    public String delete(Integer id) {
        Optional<SongMetadata> metadata = songMetadataRepository.findById(id);
        if(metadata.isEmpty()) {
            return "Id's not available";
        }else {
            songMetadataRepository.deleteById(id);
            return "Metadata deleted";
        }
    }

    private SongMetadata convert (SongMetadataDto dto){
        SongMetadata song = new SongMetadata();
        song.setId(dto.getResourceId());
        song.setName(dto.getName());
        song.setAlbum(dto.getAlbum());
        song.setArtist(dto.getArtist());
        song.setLength(dto.getLength());
        song.setYear(dto.getYear());
        return song;
    }

    private SongMetadataDto convert (SongMetadata meta){
        SongMetadataDto dto = new SongMetadataDto();
        dto.setResourceId(meta.getId());
        dto.setName(meta.getName());
        dto.setAlbum(meta.getAlbum());
        dto.setArtist(meta.getArtist());
        dto.setYear(meta.getYear());
        dto.setLength(meta.getLength());
        return dto;
    }

}

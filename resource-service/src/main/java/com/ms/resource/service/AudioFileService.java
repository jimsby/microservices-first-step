package com.ms.resource.service;

import com.ms.resource.model.AudioFile;
import com.ms.resource.repository.AudioFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AudioFileService {
    @Autowired
    private AudioFileRepository audioFileRepository;

    public Integer createAudioFile(String name){
        AudioFile audioFile = audioFileRepository.findByFileName(name)
                .orElseGet(() -> audioFileRepository.save(new AudioFile(name)));
        return audioFile.getId();
    }

    public Optional<AudioFile> getAudioFile(Integer id){
        return audioFileRepository.findById(id);
    }

    public String delete(Integer id){
        String fileName = audioFileRepository.findById(id).get().getFileName();
        audioFileRepository.deleteById(id);
        return fileName;
    }

    public long count(){
        return audioFileRepository.count();
    }
}

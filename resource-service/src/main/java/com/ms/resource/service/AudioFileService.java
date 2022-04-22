package com.ms.resource.service;

import com.ms.resource.dto.StorageDto;
import com.ms.resource.model.AudioFile;
import com.ms.resource.repository.AudioFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AudioFileService {

    private AudioFileRepository audioFileRepository;

    public Integer createAudioFile(String name, Integer storageId){
        AudioFile audioFile = audioFileRepository.findByFileName(name)
                .orElseGet(() -> audioFileRepository.save(new AudioFile(name, storageId)));
        return audioFile.getId();
    }

    public Optional<AudioFile> getAudioFile(Integer id){
        return audioFileRepository.findById(id);
    }

    public AudioFile delete(Integer id){
        AudioFile file = audioFileRepository.findById(id).get();
        audioFileRepository.deleteById(id);
        return file;
    }

    public boolean fileNotExists(String name) {
        return audioFileRepository.findByFileName(name).isEmpty();
    }

    public Integer getIdByName(String name){
        return audioFileRepository.findByFileName(name).get().getId();
    }

    public void setNewStorage(AudioFile audioFile, StorageDto after) {
        AudioFile file = audioFileRepository.findById(audioFile.getId()).get();
        file.setStorageId(after.getStorageId());
        audioFileRepository.save(file);
    }
}

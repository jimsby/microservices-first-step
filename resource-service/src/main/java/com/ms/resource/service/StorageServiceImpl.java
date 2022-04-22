package com.ms.resource.service;

import com.ms.resource.dto.StorageDto;
import com.ms.resource.model.AudioFile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl {

    private final StorageService service;

    public StorageServiceImpl(StorageService service) {
        this.service = service;
    }

    public StorageDto getRandomStaging(){
        List<StorageDto> list = service.getStoragesList();
        list = list.stream()
                .filter(storageDto -> storageDto.getStorageType().equalsIgnoreCase("Staging"))
                .collect(Collectors.toList());
        Collections.shuffle(list);
        return list.get(0);
    }

    public StorageDto getRandomPermanent(){
        List<StorageDto> list = service.getStoragesList();
        list = list.stream()
                .filter(storageDto -> storageDto.getStorageType().equalsIgnoreCase("Permanent"))
                .collect(Collectors.toList());
        Collections.shuffle(list);
        return list.get(0);
    }

    public boolean isStagingStage(AudioFile audioFile){
        List<StorageDto> list = service.getStoragesList();
        StorageDto dto = list.stream()
                .filter(storageDto -> storageDto.getStorageId().equals(audioFile.getStorageId()))
                .findFirst().get();
        return dto.getStorageType().equalsIgnoreCase("Staging");
    }

    public StorageDto getStorage(Integer storageId) {
        List<StorageDto> list = service.getStoragesList();
        StorageDto dto = list.stream()
                .filter(storageDto -> storageDto.getStorageId().equals(storageId))
                .findFirst().get();

        return dto;
    }
}

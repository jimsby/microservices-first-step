package com.ms.storageservice.service;

import com.ms.storageservice.dto.StorageDto;
import com.ms.storageservice.mapper.StorageMapper;
import com.ms.storageservice.model.Storage;
import com.ms.storageservice.repository.StorageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    private final StorageRepository repository;
    private final StorageMapper mapper;

    public StorageService(StorageRepository repository, StorageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Integer create(StorageDto dto) {
        Storage storage = mapper.toStorage(dto);
        Storage out = repository.save(storage);
        return out.getStorageId();
    }

    public List<StorageDto> getAll(){
        List<Storage> storages = new ArrayList<>();
        repository.findAll().forEach(storages::add);
        return mapper.toDtoList(storages);
    }

    public boolean delete(Integer id) {
        Optional<Storage> metadata = repository.findById(id);
        if(metadata.isEmpty()) {
            return false;
        }else {
            repository.deleteById(id);
            return true;
        }
    }
}

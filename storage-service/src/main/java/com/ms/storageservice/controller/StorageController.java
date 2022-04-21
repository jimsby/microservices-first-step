package com.ms.storageservice.controller;

import com.ms.storageservice.dto.ResponseCustomIdsDto;
import com.ms.storageservice.dto.StorageDto;
import com.ms.storageservice.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storages")
public class StorageController {

    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseCustomIdsDto createNewStorage (@RequestBody StorageDto dto){
        if (dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Validation error or request body is an invalid MP3");
        } else {
            dto.setStorageId(null);
            Integer id = service.create(dto);
            log.info("Storage created (id: " + id + ")");
            return new ResponseCustomIdsDto(id);
        }
    }

    @GetMapping
    public List<StorageDto> getStoragesList(){
        log.info("Storage list sent");
        return service.getAll();
    }

    @DeleteMapping
    public ResponseCustomIdsDto deleteStoragesByGivenIds(@RequestParam List<Integer> ids){
        if (ids.isEmpty() || ids.size() > 200) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Id's size must be 0 < id < 200");
        }
        List<Integer> result = new ArrayList<>();

        for (Integer id : ids) {
            try {
                if(service.delete(id)) {
                    log.info("Storage deleted (id: " + id + ")");
                    result.add(id);
                }
            }catch (Exception ignore){}
        }
        return new ResponseCustomIdsDto(result);
    }
}

package com.ms.resource.service;

import com.ms.resource.dto.StorageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Profile("!test")
@FeignClient(name = "${storageservice.name}", url = "${gateway.url}", primary = false)
public interface StorageService {

    @GetMapping("/storages")
    List<StorageDto> getStoragesList();
}

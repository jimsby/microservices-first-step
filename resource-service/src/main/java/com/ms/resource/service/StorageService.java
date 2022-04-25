package com.ms.resource.service;

import com.ms.resource.dto.StorageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Profile("!test")
@FeignClient(name = "${storageservice.name}", url = "${gateway.url}", primary = false)
public interface StorageService {

    @CachePut("storages")
    @GetMapping("/storages")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    List<StorageDto> getStoragesList();


    default List<StorageDto> fallback(Exception e) {
        return getCached();
    }

    @Cacheable("storages")
    default List<StorageDto> getCached() {
        return getStoragesList();
    }

}

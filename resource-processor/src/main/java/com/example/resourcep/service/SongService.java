package com.example.resourcep.service;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.example.resourcep.dto.SongMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("${service2.name}")
public interface SongService {

    @PostMapping("/songs")
    @Retryable(maxAttempts = 10,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 500, multiplier = 2))
    ResponseCustomIdsDto create(SongMetadataDto dto);
}

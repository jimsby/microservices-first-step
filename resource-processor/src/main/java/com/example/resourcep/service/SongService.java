package com.example.resourcep.service;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.example.resourcep.dto.SongMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${service2.name}", url = "${service2.base.url}")
public interface SongService {

    @PostMapping
    @Retryable(maxAttempts = 10,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 500, multiplier = 2))
    ResponseCustomIdsDto create(SongMetadataDto dto);
}

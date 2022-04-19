package com.example.resourcep.service;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("${service1.name}")
public interface ResourceService {

    @GetMapping("/resources/{id}")
    @Retryable(maxAttempts = 10,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 500, multiplier = 2))
    Response downloadFile(@PathVariable("id") Integer id);
}

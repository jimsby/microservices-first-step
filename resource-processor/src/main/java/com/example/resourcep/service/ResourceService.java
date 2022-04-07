package com.example.resourcep.service;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${service1.name}", url = "${service1.base.url}")
public interface ResourceService {

    @GetMapping("/{id}")
    @Retryable(maxAttempts = 10,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 500, multiplier = 2))
    Response downloadFile(@PathVariable("id") Integer id);
}

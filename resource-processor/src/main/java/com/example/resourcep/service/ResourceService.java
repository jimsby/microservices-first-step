package com.example.resourcep.service;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/*
Ribbon is a client side load balancer which gives you a lot of
control over the behaviour of HTTP and TCP clients. Feign already
uses Ribbon, so if you are using @FeignClient then this section also applies.

https://cloud.spring.io/spring-cloud-netflix/1.4.x/multi/multi_spring-cloud-ribbon.html
 */
@FeignClient(name = "${service1.name}", url = "${gateway.url}")
public interface ResourceService {

    @GetMapping("/resources/{id}")
    @Retryable(maxAttempts = 10,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 500, multiplier = 2))
    Response downloadFile(@PathVariable("id") Integer id);

    @PostMapping("/resources/{id}")
    ResponseCustomIdsDto moveFileToPermanent(@PathVariable Integer id);
}

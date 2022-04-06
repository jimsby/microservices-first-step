package com.example.resourcep.service;

import com.example.resourcep.dto.ResponseCustomIdsDto;
import com.example.resourcep.dto.SongMetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${service2.name}", url = "${service2.base.url}")
public interface SongService {

    @PostMapping
    ResponseCustomIdsDto create(SongMetadataDto dto);
}

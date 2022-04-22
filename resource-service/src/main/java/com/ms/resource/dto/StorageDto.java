package com.ms.resource.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageDto {

    private Integer storageId;

    private String storageType;

    private String bucket;

    private String path;

    public StorageDto(Integer storageId, String storageType, String bucket) {
        this.storageId = storageId;
        this.storageType = storageType;
        this.bucket = bucket;
    }
}

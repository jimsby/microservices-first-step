package com.ms.resource.config;

import com.ms.resource.dto.StorageDto;
import com.ms.resource.service.StorageService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Primary
public class StorageServiceConfig implements StorageService {
    StorageDto stagingStorage = new StorageDto(1, "STAGING", "testmp3bucket");
    StorageDto permanentStorage = new StorageDto(2, "PERMANENT", "permanentbucket");
        @Override
        public List<StorageDto> getStoragesList() {
            return List.of(stagingStorage,permanentStorage);
        }

}

package com.ms.storageservice.mapper;

import com.ms.storageservice.dto.StorageDto;
import com.ms.storageservice.model.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    Storage toStorage(StorageDto dto);

    StorageDto toDto(Storage storage);

    List<StorageDto> toDtoList(List<Storage> storages);
}

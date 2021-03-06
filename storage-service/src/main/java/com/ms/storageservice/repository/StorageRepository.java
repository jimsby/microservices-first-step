package com.ms.storageservice.repository;

import com.ms.storageservice.model.Storage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends CrudRepository<Storage, Integer> {
}

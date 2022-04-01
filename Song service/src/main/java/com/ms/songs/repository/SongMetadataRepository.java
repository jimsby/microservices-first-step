package com.ms.songs.repository;

import com.ms.songs.model.SongMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongMetadataRepository extends CrudRepository<SongMetadata, Integer> {
}

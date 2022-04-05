package com.ms.resource.repository;

import com.ms.resource.model.AudioFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioFileRepository extends CrudRepository<AudioFile, Integer> {
    Optional<AudioFile> findByFileName (String fileName);
}

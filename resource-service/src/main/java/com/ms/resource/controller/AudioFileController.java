package com.ms.resource.controller;

import com.ms.resource.async.RabbitController;
import com.ms.resource.dto.ResponseCustomIdsDto;
import com.ms.resource.dto.StorageDto;
import com.ms.resource.model.AudioFile;
import com.ms.resource.service.AudioFileService;
import com.ms.resource.service.S3BucketStorageService;
import com.ms.resource.service.StorageServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ms.resource.util.AudioUtil.isMp3;
import static com.ms.resource.util.FileUtil.convertMultiPartFileToFile;

@RestController
@RequestMapping("/resources")
@AllArgsConstructor
@Slf4j
public class AudioFileController {

    private S3BucketStorageService s3BucketStorageService;
    private AudioFileService audioFileService;
    private RabbitController rabbitController;

    private StorageServiceImpl storageService;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseCustomIdsDto uploadFile(@RequestParam(value = "file") MultipartFile file) {
        File tmpFile = null;

        if (audioFileService.fileNotExists(file.getOriginalFilename())) {
            try {
                tmpFile = convertMultiPartFileToFile(file);
                if (isMp3(tmpFile)) {
                    StorageDto storageStaging = storageService.getRandomStaging();
                    s3BucketStorageService.uploadFile(tmpFile, storageStaging);
                    Integer id = audioFileService.createAudioFile(tmpFile.getName(), storageStaging.getStorageId());
                    log.info("File created: {}  (id: {} | {} | bucketId: {})", tmpFile.getName(), id, storageStaging.getStorageType(), storageStaging.getStorageId());
                    ResponseCustomIdsDto response = new ResponseCustomIdsDto(id);
                    rabbitController.sendCreated(response);
                    return response;
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Validation error or request body is an invalid MP3");

                }
            } finally {
                tmpFile.delete();
            }

        } else {
            return new ResponseCustomIdsDto(audioFileService.getIdByName(file.getOriginalFilename()));
        }

    }

    @GetMapping("/{id}")
    @RolesAllowed({ "USER", "ADMIN" })
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id) {
        AudioFile audioFile = audioFileService.getAudioFile(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File with Id: " + id + " Not Found")
                );
        String fileName = audioFile.getFileName();
        StorageDto storage = storageService.getStorage(audioFile.getStorageId());
        byte[] data = s3BucketStorageService.downloadFile(fileName, storage);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping
    @RolesAllowed("ADMIN")
    public ResponseCustomIdsDto deleteFile(@RequestParam List<Integer> ids) {
        if (ids.isEmpty() || ids.size() > 200) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Id's size must be 0 < id < 200");
        }

        List<Integer> result = new ArrayList<>();

        for (Integer id : ids) {
            try {
                AudioFile file = audioFileService.delete(id);
                StorageDto storageDto = storageService.getStorage(file.getStorageId());
                s3BucketStorageService.deleteFile(file.getFileName(), storageDto);
                log.info("File deleted (id: " + id + ")");
                result.add(id);
            } catch (Exception ignore) {
            }
        }

        ResponseCustomIdsDto response = new ResponseCustomIdsDto(result);
        rabbitController.sendDeleted(response);
        return response;
    }

    @PostMapping("/{id}")
    @RolesAllowed("ADMIN")
    public ResponseCustomIdsDto moveFileToPermanent(@PathVariable Integer id) {
        AudioFile audioFile = audioFileService.getAudioFile(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File with Id: " + id + " Not Found")
                );
        StorageDto source = storageService.getStorage(audioFile.getStorageId());
        if (storageService.isStagingStage(source)) {
            StorageDto destination = storageService.getRandomPermanent();
            s3BucketStorageService.moveToPermanent(audioFile, source, destination);
            audioFileService.setNewStorage(audioFile, destination);
            log.info("File {} moved (id: {}) to PERMANENT STORAGE (id: {})",
                    audioFile.getFileName(), audioFile.getId(), audioFile.getStorageId());
            return new ResponseCustomIdsDto(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File with Id: " + id + " is Already in Permanent Storage");
        }
    }

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    public String adminInfo(){
        return "Admin login";
    }
}

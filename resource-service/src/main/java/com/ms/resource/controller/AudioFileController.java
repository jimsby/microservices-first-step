package com.ms.resource.controller;

import com.ms.resource.dto.ResponseCustomIdsDto;
import com.ms.resource.service.AudioFileService;
import com.ms.resource.service.S3BucketStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping
    public ResponseCustomIdsDto uploadFile(@RequestParam(value = "file") MultipartFile file) {
        File tmpFile = null;

        try {
            tmpFile = convertMultiPartFileToFile(file);

            if (isMp3(tmpFile)) {
                s3BucketStorageService.uploadFile(tmpFile);
                Integer id = audioFileService.createAudioFile(tmpFile.getName());
                log.info("File created: " + tmpFile.getName() + " (id: " + id + ")");
                return new ResponseCustomIdsDto(id);
            } else {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Validation error or request body is an invalid MP3");
            }

        } finally {
            tmpFile.delete();
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer id) {
        String fileName = audioFileService.getAudioFile(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "File with Id: " + id + " Not Found")
                )
                .getFileName();
        byte[] data = s3BucketStorageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping
    public ResponseCustomIdsDto deleteFile(@RequestParam List<Integer> ids) {
        if (ids.isEmpty() || ids.size() > 200) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Id's size must be 0 < id < 200");
        }

        List<Integer> result = new ArrayList<>();

        for (Integer id : ids) {
            try {
                String filename = audioFileService.delete(id);
                s3BucketStorageService.deleteFile(filename);
                log.info("File deleted (id: " + id + ")");
                result.add(id);
            }catch (Exception ignore){}
        }
        return new ResponseCustomIdsDto(result);
    }
}

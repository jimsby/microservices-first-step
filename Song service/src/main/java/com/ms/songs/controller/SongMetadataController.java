package com.ms.songs.controller;

import com.ms.songs.dto.SongMetadataDto;
import com.ms.songs.serice.SongMetadataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/songs")
@AllArgsConstructor
@Slf4j
public class SongMetadataController {

    private SongMetadataService songMetadataService;

    @PostMapping
    @ResponseBody
    public Map<String, Integer> create(@RequestBody SongMetadataDto dto) {
        if (dto == null || dto.getResourceId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation error or request body is an invalid MP3");
        } else {
            Integer id = songMetadataService.create(dto);
            log.info("Metadata created (id: " + id + ")");
            return Map.of("id", id);
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public SongMetadataDto get(@PathVariable Integer id){
        SongMetadataDto dto = songMetadataService.get(id);
        if(dto == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource doesn't exist with given id");
        }else {
            return dto;
        }
    }

    @DeleteMapping
    @ResponseBody
    public Map<String, List<Integer>> delete(@RequestParam List<Integer> ids){
        if (ids.isEmpty() || ids.size() > 200) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Id's size must be 0 < id < 200");
        }

        List<Integer> result = new ArrayList<>();

        for (Integer id : ids) {
            try {
                String deleteResult = songMetadataService.delete(id);
                log.info(deleteResult + " (id: " + id + ")");
                if(deleteResult.equals("Metadata deleted")) result.add(id);
            }catch (Exception ignore){}
        }
        return Map.of("ids", result);
    }


}

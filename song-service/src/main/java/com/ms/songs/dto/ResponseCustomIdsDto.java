package com.ms.songs.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseCustomIdsDto {
    private List<Integer> ids;
    private Integer id;

    public ResponseCustomIdsDto(Integer id) {
        this.id = id;
    }

    public ResponseCustomIdsDto(List<Integer> ids) {
        this.ids = ids;
    }
}

package com.ms.songs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class SongMetadata {
    @Id
    private Integer id;

    private String name;

    private String artist;

    private String album;

    private Double length;

    private Integer year;
}

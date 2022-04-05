package com.ms.songs.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class SongMetadata {
    @Id
    private Integer id;

    private String name;

    private String artist;

    private String album;

    private Integer length;

    private Integer year;
}

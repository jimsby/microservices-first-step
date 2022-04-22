package com.ms.resource.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AudioFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "STORAGE_ID")
    private Integer storageId;

    public AudioFile(String fileName, Integer storageId) {
        this.fileName = fileName;
        this.storageId = storageId;
    }
}

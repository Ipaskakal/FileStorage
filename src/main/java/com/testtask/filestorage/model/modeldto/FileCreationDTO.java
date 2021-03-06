package com.testtask.filestorage.model.modeldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FileCreationDTO {
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private long size;

}

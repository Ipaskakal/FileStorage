package com.testtask.filestorage.model.modeldto;

import com.testtask.filestorage.model.File;
import lombok.Getter;
import lombok.Setter;

public class FileIdDTO {
    @Getter
    @Setter
    private String id;

    public FileIdDTO(File file) {
        id = file.getId();
    }
}

package com.testtask.filestorage.model.modeldto;

import com.testtask.filestorage.model.File;

public class FileIdDTO {
    private String ID;

    public FileIdDTO(File file) {
        ID = file.getId();
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }
}

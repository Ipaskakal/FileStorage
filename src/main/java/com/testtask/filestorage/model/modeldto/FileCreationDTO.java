package com.testtask.filestorage.model.modeldto;

public class FileCreationDTO {
    private String name;

    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

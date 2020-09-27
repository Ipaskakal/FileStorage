package com.testtask.filestorage.model.modelDTO;

import com.testtask.filestorage.model.File;

import java.util.List;

public class FilePageDTO {
    private long total;

    private List<File> page;

    public FilePageDTO(List<File> files,long count){
        this.page=files;
        this.total=count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<File> getPage() {
        return page;
    }

    public void setPage(List<File> page) {
        this.page = page;
    }
}

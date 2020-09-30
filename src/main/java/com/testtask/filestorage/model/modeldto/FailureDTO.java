package com.testtask.filestorage.model.modeldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class FailureDTO {
    @Getter
    @Setter
    private boolean success;

    @Getter
    @Setter
    private String error;

    public FailureDTO(String err) {
        this.success = false;
        this.error = err;
    }
}

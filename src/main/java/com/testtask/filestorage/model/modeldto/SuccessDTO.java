package com.testtask.filestorage.model.modeldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class SuccessDTO {
    @Getter
    @Setter
    private boolean success=true;
}

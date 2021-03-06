package com.testtask.filestorage.model.modeldto;

import com.testtask.filestorage.model.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class FilePageDTO {
    @Getter
    @Setter
    private long total;

    @Getter
    @Setter
    private List<File> page;


}

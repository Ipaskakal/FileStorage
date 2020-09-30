package com.testtask.filestorage.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.testtask.filestorage.model.modeldto.FileCreationDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "testfiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @JsonInclude
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private long size;

    @Getter
    @Setter
    private List<String> tags;

    public File(FileCreationDTO fileCreationDTO) {
        this.name = fileCreationDTO.getName();
        this.size = fileCreationDTO.getSize();

    }


}

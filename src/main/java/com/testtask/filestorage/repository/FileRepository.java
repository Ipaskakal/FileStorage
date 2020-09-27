package com.testtask.filestorage.repository;

import com.testtask.filestorage.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FileRepository extends ElasticsearchRepository<File, String> {
    Page<File> findByTags(List<String> tag, PageRequest pageable);
}

package com.testtask.filestorage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testtask.filestorage.exception.NotFoundException;
import com.testtask.filestorage.model.File;
import com.testtask.filestorage.model.modeldto.FileIdDTO;
import com.testtask.filestorage.model.modeldto.FilePageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonCreationService {

    @Autowired
    private ObjectMapper mapper;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public String addArgumentExceptionJson() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put(SUCCESS, false);
            node.put(ERROR, "error description");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String addResponseJson(File file) {
        if (file == null)
            return new NotFoundException().toString();
        FileIdDTO fileIdDTO = new FileIdDTO(file);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fileIdDTO);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String successJson() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put(SUCCESS, true);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String fileNotFound() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put(SUCCESS, false);
            node.put(ERROR, "file not found");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String tagNotFound() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put(SUCCESS, false);
            node.put(ERROR, "tag not found on file");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String getPageResponseJson(List<File> files, long count) {
        FilePageDTO filePageDTO = new FilePageDTO(files, count);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(filePageDTO);

        } catch (JsonProcessingException ex) {
            throw new NotFoundException();
        }
    }
}

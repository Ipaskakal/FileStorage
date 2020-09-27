package com.testtask.filestorage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testtask.filestorage.Exception.NotFoundException;
import com.testtask.filestorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonCreationService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String AddArgumentExceptionJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("success", false);
            node.put("error", "error description");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String AddResponseJson(File file) {
        if (file == null)
            return new NotFoundException().toString();

        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("ID", file.getId());
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String SuccessJson() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("success", true);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String FileNotFound() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("success", false);
            node.put("error", "file not found");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String TagNotFound() {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("success", false);
            node.put("error", "tag not found on file");
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        } catch (JsonProcessingException ex) {
            return new NotFoundException().toString();
        }
    }

    public String GetPageResponseJson(List<File> files, long count) {
        ObjectNode node = mapper.createObjectNode();
        try {
            node.put("total", count);
            node.put("page", mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(files));
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        } catch (JsonProcessingException ex) {
            throw new NotFoundException();
        }
    }
}

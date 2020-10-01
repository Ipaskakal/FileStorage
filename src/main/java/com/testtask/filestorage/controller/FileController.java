package com.testtask.filestorage.controller;

import com.testtask.filestorage.model.File;
import com.testtask.filestorage.model.modeldto.*;
import com.testtask.filestorage.repository.FileRepository;
import com.testtask.filestorage.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@AllArgsConstructor
@RequestMapping(value = "file")
public class FileController {

    private final FileRepository fileRepository;

    private final TagService tagService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FilePageDTO file(@RequestParam Optional<List<String>> tags, @RequestParam Optional<Integer> page
            , @RequestParam Optional<Integer> size) {
        Page<File> files;
        List<File> paging;
        long count;
        int pageValue = page.orElse(0);
        int sizeValue = size.orElse(10);
        if (tags.isPresent()) {
            List<String> value = tags.get();
            files = fileRepository.findByTags(value, PageRequest.of(pageValue, sizeValue));
        } else {
            files = fileRepository.findAll(PageRequest.of(pageValue, sizeValue));
        }
        if (files == null)
            return new FilePageDTO(0, new ArrayList<>());
        count = files.getTotalElements();
        paging = files.getContent();
        return new FilePageDTO(count, paging);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addFile(@RequestBody FileCreationDTO fileCreationDTO) {
        File file = new File(fileCreationDTO);
        if (file.getSize() <= 0)
            return new ResponseEntity<>(new FailureDTO("file size cant be negative"),
                    HttpStatus.BAD_REQUEST);
        if (file.getName() == null || file.getName().isBlank())
            return new ResponseEntity<>(new FailureDTO("file name cant be empty"),
                    HttpStatus.BAD_REQUEST);
        Pattern pattern = Pattern.compile("^[A-Za-z0-9-_,\\s]+\\.[a-zA-Z0-9]{2,4}$");
        Matcher matcher = pattern.matcher(file.getName());
        if (!matcher.find())
            return new ResponseEntity<>(
                    new FailureDTO("the name must have an extension and only Cyrillic letters and numbers"),
                    HttpStatus.BAD_REQUEST);
        file.setTags(tagService.findTags(file.getName()));
        FileIdDTO fileIdDTO = new FileIdDTO(fileRepository.save(file));
        return new ResponseEntity<>(fileIdDTO, HttpStatus.OK);

    }

    @PostMapping(value = "{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public SuccessDTO addTags(@PathVariable String id, @RequestBody List<String> tags) {
        Optional<File> file = fileRepository.findById(id);
        if (file.isPresent()) {
            File value = file.get();
            List<String> fileTags = value.getTags();
            for (String tag :
                    tags) {
                if (!fileTags.contains(tag))
                    fileTags.add(tag);
            }
            fileRepository.save(value);
        }
        return new SuccessDTO();

    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> delete(@PathVariable String id) {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return new ResponseEntity<>(new SuccessDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new FailureDTO("file not found"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "{id}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteTags(@PathVariable String id, @RequestBody List<String> tags) {
        Optional<File> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            return new ResponseEntity<>(new FailureDTO("file not found"), HttpStatus.NOT_FOUND);
        }
        File value = file.get();
        List<String> fileTags = value.getTags();
        for (String tag :
                tags) {
            if (!fileTags.contains(tag))
                return new ResponseEntity<>(new FailureDTO("tag not found on file")
                        , HttpStatus.BAD_REQUEST);
        }
        for (String tag :
                tags) {
            fileTags.remove(tag);
        }
        fileRepository.save(value);
        return new ResponseEntity<>(new SuccessDTO(), HttpStatus.OK);
    }
}

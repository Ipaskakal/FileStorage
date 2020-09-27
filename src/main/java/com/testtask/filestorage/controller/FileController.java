package com.testtask.filestorage.controller;

import com.testtask.filestorage.model.File;
import com.testtask.filestorage.repository.FileRepository;
import com.testtask.filestorage.service.JsonCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private final FileRepository fileRepository;

    @Autowired
    private final JsonCreationService jsonCreationService;

    public FileController(FileRepository fileRepository, JsonCreationService jsonCreationService) {
        this.fileRepository = fileRepository;
        this.jsonCreationService = jsonCreationService;
    }

    @GetMapping
    public String file(@RequestParam Optional<List<String>> tags, @RequestParam Optional<Integer> page
            , @RequestParam  Optional<Integer> size) {
        Page<File> files;
        List<File> paging;
        long count;
        int pageValue;
        int sizeValue = size.orElse(10);

        pageValue= page.orElse(0);

        if(tags.isPresent()) {
            List<String> value=tags.get();
            files=fileRepository.findByTags(value,PageRequest.of(pageValue,sizeValue));
        }
        else {
            files=fileRepository.findAll(PageRequest.of(pageValue,sizeValue));
        }
        count=files.getTotalElements();
        paging=files.getContent();
        return jsonCreationService.GetPageResponseJson(paging,count);
    }


    @PostMapping
    public ResponseEntity<String> addFile(@RequestBody File file) {

        if (file.getSize() <= 0 || file.getName() == null || file.getName().isBlank())
            return new ResponseEntity<>(jsonCreationService.AddArgumentExceptionJson(), HttpStatus.BAD_REQUEST);
        File file1 = fileRepository.save(file);
        return new ResponseEntity<>(jsonCreationService.AddResponseJson(file1), HttpStatus.OK);

    }

    @PostMapping("{id}/tags")
    public String addTags(@PathVariable String id, @RequestBody List<String> tags){
        Optional<File> file =fileRepository.findById(id);
        if(file.isPresent()){
            File value =file.get();
            value.setTags(tags);
            fileRepository.save(value);
        }
        return jsonCreationService.SuccessJson();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (fileRepository.findById(id).isPresent()) {
            fileRepository.deleteById(id);
            return new ResponseEntity<>(jsonCreationService.SuccessJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(jsonCreationService.FileNotFound(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}/tags")
    public ResponseEntity<String> deleteTags(@PathVariable String id, @RequestBody List<String> tags) {
        Optional<File> file =fileRepository.findById(id);
        if(file.isEmpty()){
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
        File value =file.get();
        List<String> fileTags=value.getTags();
        for (String tag:
             tags) {
            if(!fileTags.contains(tag))
                return new ResponseEntity<>(jsonCreationService.TagNotFound(),HttpStatus.BAD_REQUEST);
        }
        for (String tag:
                tags) {
            fileTags.remove(tag);
        }
        fileRepository.save(value);
        return new ResponseEntity<>(jsonCreationService.SuccessJson(), HttpStatus.OK);
    }
}

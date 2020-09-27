package com.testtask.filestorage.controller;

import com.testtask.filestorage.model.File;
import com.testtask.filestorage.model.modeldto.FileCreationDTO;
import com.testtask.filestorage.repository.FileRepository;
import com.testtask.filestorage.service.JsonCreationService;
import com.testtask.filestorage.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("file")
public class FileController {

    private final FileRepository fileRepository;

    private final JsonCreationService jsonCreationService;

    private final TagService tagService;

    public FileController(FileRepository fileRepository, JsonCreationService jsonCreationService, TagService tagService) {
        this.fileRepository = fileRepository;
        this.jsonCreationService = jsonCreationService;
        this.tagService = tagService;
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
        if(files==null)
            return jsonCreationService.getPageResponseJson(new ArrayList<>(),0);
        count=files.getTotalElements();
        paging=files.getContent();
        return jsonCreationService.getPageResponseJson(paging,count);
    }


    @PostMapping
    public ResponseEntity<String> addFile(@RequestBody FileCreationDTO fileCreationDTO) {
        File file=new File(fileCreationDTO);
        if (file.getSize() <= 0 || file.getName() == null || file.getName().isBlank())
            return new ResponseEntity<>(jsonCreationService.addArgumentExceptionJson(), HttpStatus.BAD_REQUEST);
        file.setTags(tagService.findTags(file.getName()));
        File file1 = fileRepository.save(file);
        return new ResponseEntity<>(jsonCreationService.addResponseJson(file1), HttpStatus.OK);

    }

    @PostMapping("{id}/tags")
    public String addTags(@PathVariable String id, @RequestBody List<String> tags){
        Optional<File> file =fileRepository.findById(id);
        if(file.isPresent()){
            File value =file.get();
            value.setTags(tags);
            fileRepository.save(value);
        }
        return jsonCreationService.successJson();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (fileRepository.findById(id).isPresent()) {
            fileRepository.deleteById(id);
            return new ResponseEntity<>(jsonCreationService.successJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(jsonCreationService.fileNotFound(), HttpStatus.NOT_FOUND);
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
                return new ResponseEntity<>(jsonCreationService.tagNotFound(),HttpStatus.BAD_REQUEST);
        }
        for (String tag:
                tags) {
            fileTags.remove(tag);
        }
        fileRepository.save(value);
        return new ResponseEntity<>(jsonCreationService.successJson(), HttpStatus.OK);
    }
}

package com.testtask.filestorage.controller;

import com.testtask.filestorage.model.File;
import com.testtask.filestorage.repository.FileRepository;
import com.testtask.filestorage.service.TagService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileController.class)
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileRepository repository;

    @MockBean
    private TagService tagService;

    @Test
    public void addFileSuccess() throws Exception {
        String postAdd1 = "{ \"name\": \"file_name.ext\", \"size\" : 121231 }";
        File file1 = new File("1", "filezz.exe", 20030, Arrays.asList("tag1", "tag2"));

        given(this.repository.save(any(File.class))).willReturn(file1);

        this.mockMvc.perform(post("/file").contentType(MediaType.APPLICATION_JSON)
                .content(postAdd1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(file1.getId()));
    }

    @ParameterizedTest
    @MethodSource("addFixture")
    void addFileFail(String post, String error) throws Exception {
        this.mockMvc.perform(post("/file").contentType(MediaType.APPLICATION_JSON)
                .content(post))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value(error));
    }


    private static Stream<Arguments> addFixture() {
        return Stream.of(
                Arguments.of("{ \"name\": \"file_name.ext\", \"size\" : -1 }",
                        "file size cant be negative"),
                Arguments.of("{ \"name\": \"\", \"size\" : 333 }",
                        "file name cant be empty"),
                Arguments.of("{ \"name\": \"file_name.e\", \"size\" : 4334 }",
                        "the name must have an extension and only Cyrillic letters and numbers"));
    }
}
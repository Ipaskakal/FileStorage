package com.testtask.filestorage.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TagService {

    private static Map<String, List<String>> tags;

    static {
        tags = new HashMap<>();
        tags.put("audio", Arrays.asList(".mp3", ".mpc", ".wav"));
        tags.put("video", Arrays.asList(".webm", ".avi"));
        tags.put("document", Arrays.asList(".docx", ".xlsx", ".pptx "));
        tags.put("image", Arrays.asList(".jpg", ".jpeg", ".png"));

    }

    public List<String> findTags(String fileName) {
        List<String> response = new ArrayList<>();
        if (fileName == null)
            return response;
        Pattern pattern = Pattern.compile("\\.[\\w,\\d]{2,4}$");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            String fileExtension = matcher.group();
            for (Map.Entry<String, List<String>> entry :
                    tags.entrySet()) {
                List<String> extensions = entry.getValue();
                for (String extension :
                        extensions) {
                    if (fileExtension.equals(extension)) {
                        response.add(entry.getKey());
                        break;
                    }
                }
            }
        }
        return response;
    }
}

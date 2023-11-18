package com.api.controller;

import com.api.serviec.CsvService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CsvController2 {

    @Autowired
    private CsvService1 csvService1;
    @PostMapping("/modifyColumnNamesInFolder")
    public String modifyColumnNamesInFolder(@RequestParam("folderPath") String folderPath,@RequestParam("file") MultipartFile file) {
        try {
            List<File> modifiedFiles = csvService1.modifyColumnNamesInFolder(folderPath,file);
            return "Modified files: " + modifiedFiles;
        } catch (Exception e) {
            return "Error modifying files: " + e.getMessage();
        }
    }
}

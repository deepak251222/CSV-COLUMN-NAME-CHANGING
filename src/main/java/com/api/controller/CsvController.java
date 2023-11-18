package com.api.controller;

import com.api.serviec.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/csv")
public class CsvController {
    @Autowired
    private CsvService csvService;
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("relatedfile") MultipartFile relatedFile) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file.");
        }
        try {
            File modifiedFile = csvService.modifyColumnNames(file,relatedFile);
            return ResponseEntity.ok("File successfully uploaded and modified. Modified file path: " + modifiedFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred during file processing.");
        }
    }
    @PostMapping("/store")
    public ResponseEntity<String> saveEntitdate(@RequestParam("file") MultipartFile file) throws  IOException,Exception{
        csvService.saveCsvData(file);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}

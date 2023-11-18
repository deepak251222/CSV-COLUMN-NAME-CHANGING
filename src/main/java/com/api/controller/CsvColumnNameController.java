package com.api.controller;

import com.api.serviec.CsvColumnNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CsvColumnNameController {
    @Autowired
    private CsvColumnNameService csvColumnNameService;
        @PostMapping("/modify-column-names")
        public ResponseEntity<String> modifyColumnNamesInFolder(@RequestParam("folderPath") String folderPath,
                                                                @RequestParam("relatedFile") MultipartFile relatedFile) {
            try {
                    List<File> modifiedFiles = csvColumnNameService.modifyColumnNamesInFolder(folderPath, relatedFile);
                System.out.println(modifiedFiles.size());
                return ResponseEntity.ok("Modified  files. : "  + modifiedFiles);
                } catch (Exception e) {
                    return ResponseEntity.status(500).body("Error modifying files: " + e.getMessage());
            }
        }
}

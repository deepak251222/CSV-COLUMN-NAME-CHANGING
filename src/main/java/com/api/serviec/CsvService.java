package com.api.serviec;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvService {

        public File modifyColumnNames(MultipartFile file,MultipartFile relatedFile) throws IOException, Exception {
            // Convert MultipartFile to File
            File convertedFile = convertMultiPartToFile(file);
            // Read CSV file
            List<String[]> lines = readCsvData(convertedFile);

            //for getting list entity
            Set<String> namesFromRelatedFile = saveCsvData(relatedFile);
            // Check if the original file name (case-insensitive) matches any name in the related file
            String FileName = file.getOriginalFilename();
            String originalFileName = FileName.substring(0, FileName.length() - 4);
            System.out.println(originalFileName);
            if (originalFileName != null && namesFromRelatedFile.stream().anyMatch(name -> name.equalsIgnoreCase(originalFileName))) {
                System.out.println("match value");
                // Modify column names
                modifyColumnNames(lines);
            }

            // Write modified data to a temporary CSV file
            File tempFile = File.createTempFile("tempCsv", ".csv");
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(tempFile))) {
                csvWriter.writeAll(lines);
            }

            // Replace the original file with the temporary file
            if (!convertedFile.delete() || !tempFile.renameTo(convertedFile)) {
                throw new IOException("Failed to replace the original file with the modified file.");
            }
            return convertedFile;
        }
        private List<String[]> readCsvData(File file) throws IOException, Exception {
            try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                return csvReader.readAll();
            }
        }
        private void modifyColumnNames(List<String[]> lines) {
            String[] header = lines.get(0);
            for (int i = 0; i < header.length; i++) {
                if (header[i].endsWith("_code")) {
                    System.out.println(header[i]);
                    header[i] = header[i].substring(0, header[i].length() - 5);

                    System.out.println(header[i]);
                    System.out.println("-----------------------");
                }
            }
        }

        private File convertMultiPartToFile(MultipartFile file) throws IOException {
            File convertedFile = new File(file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(convertedFile)) {
                os.write(file.getBytes());
            }
            return convertedFile;
        }

    public Set<String> saveCsvData(MultipartFile file) throws IOException,Exception {
        Set<String> firstColumnData = new HashSet<>();
        File convertedFile = convertMultiPartToFile(file);
        // Read CSV file
        List<String[]> lines = readCsvData(convertedFile);
        String[] header = lines.get(0);
        for (String[] row : lines) {
            if (row.length > 0) {
                firstColumnData.add(row[0]);
            }
        }
        System.out.println(firstColumnData);
        return firstColumnData;
    }
    }


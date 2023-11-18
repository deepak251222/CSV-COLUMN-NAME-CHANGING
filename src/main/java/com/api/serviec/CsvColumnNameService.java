package com.api.serviec;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class CsvColumnNameService {
        public List<File> modifyColumnNamesInFolder(String folderPath, MultipartFile relatedFile) throws IOException, Exception {
            File folder = new File(folderPath);
            List<File> modifiedFiles = new ArrayList<>();

            if (!folder.exists() || !folder.isDirectory()) {
                throw new IllegalArgumentException("Invalid folder path: " + folderPath);
            }

            Set<String> listOfColumn = saveCsvData1(relatedFile);

            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (files != null) {
                for (File file : files) {
                        try {
                            File modifiedFile = modifyColumnNames(file, listOfColumn);
                            modifiedFiles.add(modifiedFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            return modifiedFiles;
        }

        public File modifyColumnNames(File file, Set<String> relatedFile) throws IOException, Exception {
            List<String[]> lines = readCsvData(file);
            modifyColumnNames(lines, relatedFile);
            File modifiedFile = new File(file.getParent(), "modified_" + file.getName());
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(modifiedFile))) {
                csvWriter.writeAll(lines);
            }
            return modifiedFile;
        }

        private List<String[]> readCsvData(File file) throws IOException, Exception {
            try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                return csvReader.readAll();
            }
        }

        private void modifyColumnNames(List<String[]> lines, Set<String> listOfColumn) {
            String[] header = lines.get(0);
            for (int i = 0; i < header.length; i++) {
                String columnName = header[i];
                if (columnName.endsWith("_code") && listOfColumn.contains(columnName.toLowerCase())) {
                    header[i] = columnName.substring(0, columnName.length() - 5);
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

//        public Set<String> saveCsvData(MultipartFile file) throws IOException, Exception {
//            Set<String> firstColumnData = new HashSet<>();
//            File convertedFile = convertMultiPartToFile(file);
//            List<String[]> lines = readCsvData(convertedFile);
//            for (String[] row : lines) {
//                if (row.length > 0) {
//                    firstColumnData.add(row[0].toLowerCase());
//                }
//            }
//            System.out.println(firstColumnData);
//            return firstColumnData;
//        }

        public Set<String> saveCsvData1(MultipartFile file) throws IOException, Exception {
            Set<String> secondColumnData = new HashSet<>();
            File convertedFile = convertMultiPartToFile(file);
            List<String[]> lines = readCsvData(convertedFile);
            for (String[] row : lines) {
                if (row.length > 1) {
                    secondColumnData.add(row[1].toLowerCase());
                }
            }
            System.out.println(secondColumnData);
            return secondColumnData;
        }
    }


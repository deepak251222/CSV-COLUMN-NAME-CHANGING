package com.api.serviec;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvService1 {

    private static final String file="C:\\Users\\deepak.kumar_simadvi\\Desktop\\csv file1";
        public List<File> modifyColumnNamesInFolder(String folderPath,MultipartFile relatedFile) throws IOException, Exception {
            File folder = new File(folderPath);
            List<File> modifiedFiles = new ArrayList<>();

            if (!folder.exists() || !folder.isDirectory()) {
                throw new IllegalArgumentException("Invalid folder path: " + folderPath);
            }
            // calling method value for getting 2 column value
            Set<String> listOfColumn= saveCsvData1(relatedFile);

            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

            if (files != null) {
                for (File file : files) {
                    String fileName=file.getName();
                    Set<String> namesFromRelatedFile = saveCsvData(relatedFile);
                    // add
                    String originalFileName = fileName.substring(0, fileName.length() - 4);
                    System.out.println(originalFileName);
                    if (originalFileName != null && namesFromRelatedFile.stream().anyMatch(name -> name.equalsIgnoreCase(originalFileName))) {
                        System.out.println("match value");
                        System.out.println(file.getName());
                        try {
                            File modifiedFile = modifyColumnNames(file,listOfColumn);
                            modifiedFiles.add(modifiedFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return modifiedFiles;
        }
        public File modifyColumnNames(File file,Set<String> relatedFile) throws IOException, Exception {
            List<String[]> lines = readCsvData(file);
            modifyColumnNames(lines,relatedFile);
            File modifiedFile = new File(file.getParent(), "modified_" + file.getName());
            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(modifiedFile))) {
                csvWriter.writeAll(lines);
            }
            return modifiedFile;
        }

        private List<String[]> readCsvData(File file) throws IOException,Exception {
            try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
                return csvReader.readAll();
            }
        }
        private void modifyColumnNames(List<String[]> lines,Set<String> listOfColumn) {
            String[] header = lines.get(0);
            //calling 2 value column from
            for (int i = 0; i < header.length; i++) {
               String columnName =header[i];
                // checking value equal or not
                if (header[i].endsWith("_code") && listOfColumn.stream().anyMatch(name -> name.equalsIgnoreCase(columnName)))
                {
                    header[i] = header[i].substring(0, header[i].length() - 5);
                }
            }
        }
        private File convertMultiPartToFile(MultipartFile file) throws IOException {
            File convertedFile = new File(file.getOriginalFilename());
            try (OutputStream os = new FileOutputStream(convertedFile)) {
                os.write(file.getBytes());
            }return convertedFile;
        }

        // for finding the entity (table name) from related csv file
    public Set<String> saveCsvData(MultipartFile file) throws IOException,Exception {
        Set<String> firstColumnData = new HashSet<>();
        File convertedFile = convertMultiPartToFile(file);
        // Read CSV file
        List<String[]> lines = readCsvData(convertedFile);
        String[] header = lines.get(0);
        for (String[] row : lines) {
            if (row.length > 0) {
                firstColumnData.add(row[0]);}
        }
        System.out.println(firstColumnData);
        return firstColumnData;
    }
   // find value from second column data
   public Set<String> saveCsvData1(MultipartFile file) throws IOException, Exception {
       Set<String> secondColumnData = new HashSet<>();
       File convertedFile = convertMultiPartToFile(file);
       List<String[]> lines = readCsvData(convertedFile);
       for (String[] row : lines) {
           if (row.length > 1) {
               secondColumnData.add(row[1]);
           }
       }
       System.out.println(secondColumnData);
       return secondColumnData;
   }

}


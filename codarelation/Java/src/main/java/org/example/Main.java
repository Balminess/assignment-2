package org.example;
import java.io.File;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {

        String basePath = args[0];//directory of json files
        String outpath = args[1]; //directory of CSV files

        String outputFilename= outpath+"output.csv";

        File dir = new File(basePath);
        File[] files = dir.listFiles();
        CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFilename, true));
        String[] header = {"jsonNum", "sloc", "readability", "mcCabe","revisionNumber"};
        csvWriter.writeNext(header);

        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            String filePath = basePath + fileName;

            int index = fileName.lastIndexOf(".");
            String file_number = index == -1 ? fileName : fileName.substring(0, index);
;
            String code = ReadJson.getSourceCode(filePath);
            int revisionNumber = ReadJson.getRevisionNum(filePath);

            try {
                JavaParser.CodeMetric codemetric = JavaParser.parseFile(code);
                String[] data = {file_number, String.valueOf(codemetric.sloc), String.valueOf(codemetric.readability),String.valueOf(codemetric.mcCabe), String.valueOf(revisionNumber)};
                csvWriter.writeNext(data);
            } catch (Exception ignored) {
            }
        }
        csvWriter.close();
    }
}








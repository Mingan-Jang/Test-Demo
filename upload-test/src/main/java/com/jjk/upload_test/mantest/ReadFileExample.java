package com.jjk.upload_test.mantest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class ReadFileExample {
    public static void main(String[] args) {
        String inputFilePath = "D:\\Job\\03_Project\\Vghpt_WorkSpace_Docu\\04_Event\\20240524_API呼叫\\ss.log.2024-05-24";
        String outputFilePath = "D:\\Job\\03_Project\\Vghpt_WorkSpace_Docu\\04_Event\\20240524_API呼叫\\output.txt";
        String targetString = "SsDdtController.querySectList()";
        String countString = "ScheOrderDAOImpl.getCycleReqno";
        int linesToRead = 150000;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            String line;
            int lineNumber = 0;
            int count = 0;
            boolean recording = false;

            while ((line = br.readLine()) != null && lineNumber < linesToRead) {
                lineNumber++;
                if (line.contains(targetString)) {
                    if (recording) {
                        bw.write("Total count for " + countString + ": " + count);
                        bw.newLine();
                        recording = false;
                        count = 0;
                    }
                    bw.write("Line " + lineNumber + ": " + line);
                    bw.newLine();
                    recording = true;
                } else if (recording && line.contains(countString)) {
                    count++;
                }
            }

            // Check if recording was still in progress when reaching the end of the file
            if (recording) {
                bw.write("Total count for " + countString + ": " + count);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error reading/writing the file: " + e.getMessage());
        }
    }
}
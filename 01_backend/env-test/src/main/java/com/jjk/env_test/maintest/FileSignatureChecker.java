package com.jjk.env_test.maintest;

import java.io.FileInputStream;
import java.io.IOException;

public class FileSignatureChecker {

    private static final String FILE_SIGNATURE_XLSX = "504b0304"; // ZIP格式的簽名

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FileSignatureChecker <file-path>");
            return;
        }
        
        String filePath = args[0];
        
        try {
            String signature = getFileSignature(filePath);
            System.out.println("File Signature: " + signature);
            if (FILE_SIGNATURE_XLSX.equalsIgnoreCase(signature)) {
                System.out.println("The file is an XLSX file.");
            } else {
                System.out.println("The file is not an XLSX file.");
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static String getFileSignature(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] signatureBytes = new byte[4]; // 讀取前4個字節
            fis.read(signatureBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : signatureBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }
}
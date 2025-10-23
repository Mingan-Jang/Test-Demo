package module2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.util.IOUtils;

public class CalculateChecksum {

    public String calculateChecksum(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "The provided file path is not valid.";
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            long previous =1113371434L;
            
        	long checksum = IOUtils.calculateChecksum(Files.readAllBytes(Paths.get(filePath)));
            
        	System.out.println(previous == checksum);
        	System.out.println(previous);
        	System.out.println(checksum);

        	return "Checksum: " + checksum;
        } catch (IOException e) {
            return "Error calculating checksum: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
     
    	String filePath = "C:\\Users\\2405015\\Desktop\\Test2.xlsx";
		
    	CalculateChecksum cc = new CalculateChecksum();
        String result = cc.calculateChecksum(filePath);
        System.out.println(result);
    }
}
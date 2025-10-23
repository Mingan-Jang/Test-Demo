package com.jjk.env_test.maintest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculator {

	private static String generateChecksum(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] dataBytes = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, bytesRead);
			}
		}
		byte[] hashBytes = md.digest();
		BigInteger bigInt = new BigInteger(1, hashBytes);
		return bigInt.toString(16);
	}

	public static void main(String[] args) {
		// 硬编码文件路径
		String filePath = "C:\\Users\\2405015\\Desktop\\Test.xlsx";
		File file = new File(filePath);

		if (!file.exists() || !file.isFile()) {
			System.out.println("The provided file path is not valid.");
			return;
		}

		try {
			String checksum = generateChecksum(file);

			String previoudMD5 = "f858bf82200902fc839d24602e5aae20";
			// ff45468ea59848f9b69e2b3c92b95cb9
			System.out.println(previoudMD5.equals(checksum));
			System.out.println("Checksum: " + checksum);
		} catch (IOException | NoSuchAlgorithmException e) {
			System.err.println("Error calculating checksum: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

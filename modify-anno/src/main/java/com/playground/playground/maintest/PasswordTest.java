package com.playground.playground.maintest;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

public class PasswordTest {
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String password = "😂😂";
		byte[] quotedPasswordBytes = ('"' + password + '"').getBytes("UTF-16LE");
		System.out.println(('"' + password + '"'));
	
		
		
		String SPECIAL_CHARS = "~!@#$%^&*.?";
		StringBuilder sb = new StringBuilder();
		SecureRandom sr = new SecureRandom();
		for (int i=0 ; i < 15; i++){
			int randomNumber = sr.nextInt(4) +1;
			switch (randomNumber) {
			case 1:
				sb.append((char) (sr.nextInt(10) + 48));
				break;
			case 2:
				sb.append((char) (sr.nextInt(26) + 65));
				break;
			case 3:
				sb.append((char) (sr.nextInt(26) + 97));		
				break;
			case 4: 
				sb.append(SPECIAL_CHARS.charAt(sr.nextInt(SPECIAL_CHARS.length()))); 
				break;
			}
		}
	}

}
	
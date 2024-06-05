package com.jjk.test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JULTest {
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("com.meow.test");
		
		logger.info("Test");
		
		logger.log(Level.INFO , " info msg");
		
		String a ="aa";
		String b ="bb";
		logger.log(Level.INFO , "MEOW {0} {1}", new Object[] {a ,b});

	}
}

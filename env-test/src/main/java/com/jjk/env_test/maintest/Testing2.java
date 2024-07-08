package com.jjk.env_test.maintest;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Testing2 {
	public static void main(String[] args) throws UnknownHostException {
		String action = "UPLOAD_FIE";

//		List<FileActionEnum> enumList = Arrays.asList(FileActionEnum.values());
//
//        FileActionEnum actionEnum = FileActionEnum.valueOf(action);
//        System.out.println(actionEnum.getErrorDesc());
//		if (!enumList.contains(action)) {
//			System.out.println("Error");
//		}
		
		InetAddress localhost = InetAddress.getLocalHost();
        String hostname = localhost.getHostName();
        System.out.println("Hostname: " + hostname);
        
        String osName = System.getProperty("os.name");
        System.out.println("Operating System: " + osName);
        
        
		String extension = FileNameUtils.getBaseName("");

	}

	public enum FileActionEnum {
		UPLOAD_FILE("上傳文件時發生錯誤。"), UPLOAD_FOLDER("上傳文件夾時發生錯誤。"), READ_LOCAL_FILE("讀取本地文件時發生錯誤。");

		private final String errorDesc;

		FileActionEnum(String errorDesc) {
			this.errorDesc = errorDesc;
		}

		public String getErrorDesc() {
			return errorDesc;
		}
	}

}

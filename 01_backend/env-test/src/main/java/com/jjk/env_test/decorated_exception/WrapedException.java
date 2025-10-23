package com.jjk.env_test.decorated_exception;
import java.io.IOException;


/**
 * 目的：通過將 {@code IOException} 包裝在 {@code RuntimeException} 中，可以避免顯式錯誤處理!!!
 * {@code WrapedException} 類演示了如何將 {@code IOException} 包裝在 {@code RuntimeException} 中。
 * <p>
 * 此示例包括一個 {@code processData()} 方法，該方法模擬一些處理，
 * 並在特定條件滿足時拋出包含 {@code IOException} 的 {@code RuntimeException}。
 * </p>
 */
public class WrapedException {

    /**
     * 模擬數據處理，並在特定條件滿足時拋出包含 {@code IOException} 的 {@code RuntimeException}。
     * 當循環變量 {@code i} 的值為 2 時，拋出異常。
     */
    public static void processData() {
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                IOException originalException = new IOException("New IOException");
                throw new RuntimeException(originalException);
            }
        }
    }
    
    public static void main(String[] args) {
        processData();
    }
}
package com.event.enumerations;

import lombok.Getter;

@Getter
public enum ErrorMockEnum {
    SOCKET_TIMEOUT("模擬網路超時"),
    QUERY_TIMEOUT("模擬數據庫查詢超時"),
    RUNTIME("模擬系統處理異常");

    private final String description;

    ErrorMockEnum(String description) {
        this.description = description;
    }
}
package com.event.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum DlxStatusEnum {
    N("新死信"),
    R("處理中"),
    F("重新處理失敗"),
    S("重新處理成功");

    private final String description;

    DlxStatusEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.name();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getCode(), description);
    }

    public boolean isFinal() {
        return this == F || this == S;
    }

    public static Optional<DlxStatusEnum> fromCode(String code) {
        if (code == null)
            return Optional.empty();
        return Arrays.stream(values())
                .filter(status -> status.name().equals(code))
                .findFirst();
    }
}
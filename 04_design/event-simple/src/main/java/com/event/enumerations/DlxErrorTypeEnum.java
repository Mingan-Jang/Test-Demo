package com.event.enumerations;

public enum DlxErrorTypeEnum {
	VAL("資料驗證失敗"), DUP("資料重複"), NF("原始事件不存在"), TO("處理超時"), ERR("處理異常"), RETRY("超過最大重試次數");

	private final String desc;

	DlxErrorTypeEnum(String desc) {
		this.desc = desc;
	}

	public String desc() {
		return desc;
	}
}

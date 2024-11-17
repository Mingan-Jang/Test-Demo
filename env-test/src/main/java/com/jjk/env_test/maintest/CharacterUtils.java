package com.jjk.env_test.maintest;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CharacterUtils {
	private Font chineseFont;
	private Font englishFont;

	private static CharacterUtils instance;

	private CharacterUtils() {
		chineseFont = new Font("PMingLiU", Font.PLAIN, 12); // 中文字体
		englishFont = new Font("Arial", Font.PLAIN, 12); // 英文字体
	}

	public static CharacterUtils getInstance() {
		if (instance == null) {
			instance = new CharacterUtils();
		}
		return instance;
	}

	public int calculateWidth(char ch) {
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = image.getGraphics();
		try {
			Font font = isChinese(ch) ? chineseFont : englishFont;
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics();
			return metrics.charWidth(ch);
		} finally {
			graphics.dispose();
		}
	}

	public boolean isChinese(char ch) {
		return String.valueOf(ch).matches("[\\u4e00-\\u9fff]");
	}

	public boolean isFullEnglish(char ch) {
		return String.valueOf(ch).matches("[Ａ-Ｚａ-ｚ]");
	}

	public CharacterEnum getCharacterType(char ch) {
		if (String.valueOf(ch).matches("[\\u4e00-\\u9fff]")) {
			return CharacterEnum.CHINESE;
		} else if (String.valueOf(ch).matches("[Ａ-Ｚａ-ｚ]")) {
			return CharacterEnum.FULL_ENGLISH;
		} else if (String.valueOf(ch).matches("[A-Za-z]")) {
			return CharacterEnum.HALF_ENGLISH;
		} else if (String.valueOf(ch).matches("[-–—,.!?;:'\"()\\[\\]{}，。！？；：「」『』（）【】｛｝]")) {
			return CharacterEnum.SPECIAL_SYMBOL;
		} else if (String.valueOf(ch).matches("[0-9]")) {
			return CharacterEnum.HALF_NUMBER;
		} else if (String.valueOf(ch).matches("[\\uFF10-\\uFF19]")) {
			return CharacterEnum.FULL_NUMBER;
		} else {
			return CharacterEnum.UNKNOWN;
		}
	}

	public boolean isHalfEnglish(char ch) {
		return String.valueOf(ch).matches("[A-Za-z]");
	}

	public boolean isSpecialSymbol(char ch) {
		return String.valueOf(ch).matches("[-–—,.!?;:'\"()\\[\\]{}，。！？；：「」『』（）【】｛｝]");
	}

}
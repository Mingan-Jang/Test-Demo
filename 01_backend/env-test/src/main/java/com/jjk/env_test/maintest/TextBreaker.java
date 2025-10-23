package com.jjk.env_test.maintest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextBreaker {

	public static String toHalfWidth(String input) {
		StringBuilder output = new StringBuilder();
		for (char c : input.toCharArray()) {
			// 全形空格轉換為半形空格
			if (c == '\u3000') {
				output.append('\u0020');
			}
			// 其他全形字符轉換為半形字符
			else if (c >= '\uFF01' && c <= '\uFF5E') {
				output.append((char) (c - 0xFEE0));
			} else {
				// 保留半形字符
				output.append(c);
			}
		}
		return output.toString();
	}

	public static void main(String[] args) {
//		String mixedText = "這是測試文本 Hello world! 測試123，這段中英混合文本要做斷行。";
		String mixedText = "鎝－９９ｍＴＲＯＤＡＴ－１腦部多巴神經元";

		int maxWidth = 14;

		String halfWidthStr = toHalfWidth(mixedText);

		List<String> result = breakText(mixedText, maxWidth);
		System.out.println(result);
	}

	public static List<String> breakText(String text, int maxWidth) {

		List<String> breakTexts = new ArrayList<>();
		StringBuilder line = new StringBuilder();
		char[] chars = text.toCharArray();
		int previousBreakPoint = 0;

		for (int i = 0; i < chars.length; i++) {
			char currentChar = chars[i];
			line.append(currentChar);

			// 當行超過最大寬度時，處理斷行
			if (line.length() >= maxWidth) {
				int breakPoint = findBreakPoint(chars, i, previousBreakPoint);

				// 確保 breakPoint 是有效的，並在範圍內
				if (breakPoint > previousBreakPoint) {
					breakTexts.add(new String(chars, previousBreakPoint, breakPoint - previousBreakPoint + 1)); // 使用正确的索引
					previousBreakPoint = breakPoint + 1; // 更新到下一个开始位置
					i = breakPoint;
					line.setLength(0); // 清空当前行
				}
			}
		}

		// 添加最後一行
		if (line.length() > 0) {
			breakTexts.add(line.toString());
		}

		return breakTexts;
	}

	private static int findBreakPoint(char[] chars, int index, int previoudBreakPoint) {
		// 最大回溯長度
		double LIMITED_MAX_PERCETAGE = .8f;

		int theBreakPoint = -1;
		System.out.println("previoudBreakPoint " + previoudBreakPoint);

		System.out.println("index " + index);

		// # 查找適合的斷行點
		for (int i = index - 1; i > previoudBreakPoint; i--) {
			if (chars[i] == ' ') {
				// ## 空白符處可以斷行
				theBreakPoint = i;
			} else if (isChinese(chars[i])) {
				// ## 中文可斷行
				theBreakPoint = i;
			} else if (isSpecialSymbol(chars[i])) {
				// ## 特殊符號之前斷行，符號放在下一行
				theBreakPoint = i - 1;
			}

			if (theBreakPoint < 0) {
				continue;
			}
			// ## 計算分割前後寬度
			if ((theBreakPoint - previoudBreakPoint + 1) > (index - previoudBreakPoint + 1) * LIMITED_MAX_PERCETAGE) {
				// ### 如果前段能大於一定比例則斷行
				return theBreakPoint;
			} else {

				if (isFullEnglish(chars[i - 1])) {
					char temp = chars[i - 1];

					chars[i - 1] = '_';

					// i-1以後全部往後
				} else if (isHalfEnglish(chars[i - 1])) {
					chars[i - 1] = '-';
					// i-1以後全部往後

				}

				return i - 1;
			}

		}
		return theBreakPoint;

	}

	// 判斷是否為中文字符
	private static boolean isChinese(char ch) {
		return String.valueOf(ch).matches("[\\u4e00-\\u9fff]");
	}

	// 判斷是否為英文字母及數字(包含全形跟半形)
	private static boolean isFullEnglish(char ch) {
		return String.valueOf(ch).matches("[Ａ-Ｚａ-ｚ]");
	}

	private static boolean isHalfEnglish(char ch) {
		return String.valueOf(ch).matches("[A-Za-z]");
	}

	// 判斷是否為特殊符號(全形和半形)
	private static boolean isSpecialSymbol(char ch) {
		return String.valueOf(ch).matches("[–—,.!?;:'\"()\\[\\]{}，。！？；：「」『』（）【】｛｝]");
	}
}

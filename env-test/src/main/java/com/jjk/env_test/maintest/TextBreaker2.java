package com.jjk.env_test.maintest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class TextBreaker2 {

	static double LIMITED_MAX_PERCETAGE = .6f;
//	static int MAX_WIDTH = 60;
	static int MAX_WIDTH = 120;

	public static void main(String[] args) {
//		String mixedText = "這是測試文本 Hello world! 測試123，這段中英混合文本要做斷行。";
		String targetString = "鎝－９９ｍＴＲＯＤＡＴ－１腦部多巴神經元";
//		String targetString = "ABCDEFGHIJKLMNO";
//		String targetString = "ABCDE-FGHI-JKLMNO";
//		String targetString = "-FGHI-JK";

		// String targetStr = "012345678901234";

		CharacterEnum en = CharacterUtils.getInstance().getCharacterType('-');
		List<String> breakTexts = new ArrayList<>();

		List<CharacterSegment> segmentList = getCharacterSegments(targetString.toCharArray());
		int lineWidth = 0;
		int previousIndex = 0;

//		System.out.println(getCharacterSegments("-FGHI-JK".toCharArray()).stream().mapToInt(CharacterSegment::getWidth).sum());
//		System.out.println(getCharacterSegments("ABCDEFG".toCharArray()).stream().mapToInt(CharacterSegment::getWidth).sum());
//		System.out.println(getCharacterSegments("多巴神經".toCharArray()).stream().mapToInt(CharacterSegment::getWidth).sum());

		int index = 0;
		while (index < segmentList.size()) {
			CharacterSegment characterSegment = segmentList.get(index);
			lineWidth += characterSegment.getWidth();

			if (lineWidth > MAX_WIDTH) {
				Pair<Integer, String> result = findBreakPointIndexAndString(segmentList, index, previousIndex,
						lineWidth);
				int newIndex = result.getLeft();

				previousIndex = newIndex + 1;
				index = newIndex + 1; // Reset the index based on the new breakpoint

				breakTexts.add(result.getRight());

				lineWidth = 0;
				System.out.println(newIndex);
				System.out.println(result.getRight());
			} else {
				index++;
			}
		}

//		
//		for (int index = 0; index < segmentList.size(); index++) {
//			CharacterSegment characterSegment = segmentList.get(index);
//			lineWidth += characterSegment.getWidth();
//			if (lineWidth >= MAX_WIDTH) {
//				Pair<Integer, String> result = findBreakPointIndexAndString(segmentList, index, previousIndex,
//						lineWidth);
//				int newIndex = result.getLeft();
//				previousIndex = newIndex + 1;
//				index = newIndex + 1;
//				breakTexts.add(result.getRight());
//
//				lineWidth = 0;
//				System.out.println(newIndex);
//				System.out.println(result.getRight());
//			}
//		}

		if (breakTexts.size() >= 0) {
			StringBuilder sb = new StringBuilder();
			for (int j = previousIndex; j < segmentList.size(); j++) {
				sb.append(segmentList.get(j).getCharacter());
			}
			breakTexts.add(sb.toString());
		}

		for (String text : breakTexts) {
			System.out.println(text);
		}
	}

	private static Pair<Integer, String> findBreakPointIndexAndString(List<CharacterSegment> segmentList, int index,
			int previousIndex, int lineWidth) {
		int breakIndex = -1;
		int theLineWidth = lineWidth;
		StringBuilder sb = new StringBuilder();

		CharacterSegment ch = segmentList.get(index);
		theLineWidth -= ch.getWidth();

		for (int i = index - 1; i >= previousIndex; i--) {
			ch = segmentList.get(i);

			if (ch.getCharacterEnum() == CharacterEnum.CHINESE || ch.getCharacter() == ' ') {
				// ## 中文可斷行
				breakIndex = i;
				break;
			} else if (ch.getCharacterEnum() == CharacterEnum.SPECIAL_SYMBOL) {
				// ## 特殊符號之前斷行，符號放在下一行
				breakIndex = i - 1;
				theLineWidth -= ch.getWidth();
				break;
			}
			theLineWidth -= ch.getWidth();
		}

		if (theLineWidth > MAX_WIDTH * LIMITED_MAX_PERCETAGE) {
			for (int j = previousIndex; j <= breakIndex; j++) {
				sb.append(segmentList.get(j).getCharacter());
			}

			return Pair.of(breakIndex, sb.toString());
		} else {
			breakIndex = index - 1;
			int backIndex = breakIndex - 1;

			for (int j = previousIndex; j <= breakIndex; j++) {
				sb.append(segmentList.get(j).getCharacter());
			}

			System.out.println(segmentList.get(breakIndex) + "breakIndex  >> " + breakIndex);

			// char[] c = ZUXY，假如index=2代表在c[index-1]=c[1]=U斷行(ZU/XY)，backIndex為斷行前一字
			if (backIndex > 0 && segmentList.get(breakIndex).getCharacterEnum() == CharacterEnum.FULL_ENGLISH
					&& segmentList.get(backIndex).getCharacterEnum() == CharacterEnum.FULL_ENGLISH) {
				// 如果c[backindex]=c[0]=Z，中間加上-號，(Z-/UXY)
				sb.setLength(sb.length() - 1);
				sb.append('-');
				return Pair.of(backIndex, sb.toString());

			} else if (backIndex > 0 && segmentList.get(breakIndex).getCharacterEnum() == CharacterEnum.HALF_ENGLISH
					&& segmentList.get(backIndex).getCharacterEnum() == CharacterEnum.HALF_ENGLISH) {
				sb.setLength(sb.length() - 1);
				sb.append('-');
				return Pair.of(backIndex, sb.toString());
			} else {
				return Pair.of(breakIndex, sb.toString());
			}

		}

	}

	public static ArrayList<CharacterSegment> getCharacterSegments(char[] chars) {
		ArrayList<CharacterSegment> segments = new ArrayList<>();

		for (char ch : chars) {
			int width = CharacterUtils.getInstance().calculateWidth(ch);
			CharacterEnum category = CharacterUtils.getInstance().getCharacterType(ch);
			segments.add(new CharacterSegment(ch, width, category));
		}

		return segments;
	}

}

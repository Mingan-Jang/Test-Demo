package com.jjk.env_test.maintest;

public class CharacterSegment {
	private char character;
	private int width;
	private CharacterEnum CharacterEnum; 
	
	// Constructor
	public CharacterSegment(char character, int width, CharacterEnum CharacterEnum) {
		this.character = character;
		this.width = width;
		this.CharacterEnum = CharacterEnum;
	}

	// Getters
	public char getCharacter() {
		return character;
	}

	public int getWidth() {
		return width;
	}

	public CharacterEnum getCharacterEnum() {
		return CharacterEnum;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setCharacterEnum(CharacterEnum characterEnum) {
		CharacterEnum = characterEnum;
	}

	@Override
	public String toString() {
		return "CharacterSegment [character=" + character + ", width=" + width + ", CharacterEnum=" + CharacterEnum
				+ "]";
	}

}

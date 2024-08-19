package com.jjk.upload_test.mantest;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FontMetricsExample extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font = new Font("Arial", Font.PLAIN, 12);
		FontMetrics metrics = g.getFontMetrics(font);

		String[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
		for (String letter : letters) {
			int width = metrics.stringWidth(letter);
			System.out.println(letter + ": " + width + " pixels");
		}

		String[] numbers = "0123456789".split("");
		for (String number : numbers) {
			int width = metrics.stringWidth(number);
			System.out.println(number + ": " + width + " pixels");
		}
		String[] chineseCharacters = { "中", "字", "測", "試" };
		for (String character : chineseCharacters) {
			int width = metrics.stringWidth(character);
			System.out.println(character + ": " + width + " pixels");
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.add(new FontMetricsExample());
		frame.setVisible(true);
	}
}
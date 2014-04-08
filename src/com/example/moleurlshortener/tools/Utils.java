package com.example.moleurlshortener.tools;

import java.util.Random;

public class Utils {

	public static String generateRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0 : new Random()
				.nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
				+ (int) Math.pow(10, charLength - 1));
	}
}

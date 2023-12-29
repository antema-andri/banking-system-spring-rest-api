package com.backend.bankingsystem.utils;

public class UtilString {
    public static String getCapitalLetters(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isUpperCase(currentChar)) {
                result.append(currentChar);
            }
        }

        return result.toString();
    }
}

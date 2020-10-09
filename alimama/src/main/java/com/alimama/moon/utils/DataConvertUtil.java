package com.alimama.moon.utils;

public class DataConvertUtil {
    static final int CONVERT_STEP = 65248;
    static final char FULL_CHAR_END = '～';
    static final char FULL_CHAR_START = '！';
    static final char FULL_SPACE = '　';
    static final char HALF_CHAR_END = '~';
    static final char HALF_CHAR_START = '!';
    static final char HALF_SPACE = ' ';

    public static String FullToHalf(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == 12288) {
                charArray[i] = ' ';
            } else if (charArray[i] >= 65281 && charArray[i] <= 65374) {
                charArray[i] = (char) (charArray[i] - CONVERT_STEP);
            }
        }
        return new String(charArray);
    }

    public static String HalfToFull(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == ' ') {
                charArray[i] = FULL_SPACE;
            } else if (charArray[i] >= '!' && charArray[i] <= '~') {
                charArray[i] = (char) (charArray[i] + CONVERT_STEP);
            }
        }
        return new String(charArray);
    }
}

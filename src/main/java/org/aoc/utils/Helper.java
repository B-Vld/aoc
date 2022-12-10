package org.aoc.utils;

import java.util.regex.Pattern;

public class Helper {

    private static final Pattern NUMERIC = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMERIC.matcher(strNum).matches();
    }

}

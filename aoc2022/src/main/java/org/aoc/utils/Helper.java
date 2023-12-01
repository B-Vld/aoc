package org.aoc.utils;

import org.aoc.enums.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static final Pattern NUMERIC = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static final Pattern INTEGER = Pattern.compile("-?\\d+");
    public static final Set<Character> OPERATORS = Set.of('+', '-', '/', '*');

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMERIC.matcher(strNum).matches();
    }

    public static List<String> findIntegers(String stringToSearch) {
        Matcher matcher = NUMERIC.matcher(stringToSearch);

        List<String> integerList = new ArrayList<>();
        while (matcher.find()) {
            integerList.add(matcher.group());
        }
        return integerList;
    }

    public static Operator findFirstSig(String stringToSearch) {
        Operator result = null;
        for (char c : stringToSearch.toCharArray()) {
            if (OPERATORS.contains(c)) {
                switch (c) {
                    case '+' -> result = Operator.ADD;
                    case '-' -> result = Operator.SUBTRACT;
                    case '*' -> result = Operator.MULTIPLY;
                    case '/' -> result = Operator.DIVIDE;
                }
            }
        }
        return result;
    }

    public static int findFirstInt(String stringToSearch) {
        var s = stringToSearch.replaceAll("\\D+", "");
        return Integer.parseInt(s);
    }

    public static int gcd(int a, int b) {
        if (b == 0)
            return a;
        else
            return gcd(b, Math.abs(a - b));
    }

    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        } else {
            int gcd = gcd(a, b);
            return Math.abs(a * b) / gcd;
        }
    }

}

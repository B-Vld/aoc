package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

@Slf4j
@Day(day = 25)
public class Day25 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var result = Input.allLinesDefaultDelimiter(fileName)
                .map(this::toDecimal)
                .reduce(0L, Long::sum);

        log.info("Day 25 first challenge : {}", toSnafu(result));
    }

    @Override
    public void secondChallenge(String fileName) {

    }

    private long toDecimal(String str) {
        long decimal = 0;
        for (int i = 0; i < str.length(); i++) {
            char snafuDigit = str.charAt(i);
            decimal = decimal * 5 + snafuToDecimal(snafuDigit);
        }
        return decimal;
    }

    private String toSnafu(long num) {
        StringBuilder result = new StringBuilder();
        do {
            long fives = (num + 2) / 5;
            int digit = (int) (num - 5 * fives);
            result.insert(0, decimalToSnafu(digit));
            num = fives;
        } while (num != 0);

        return result.toString();
    }

    private int snafuToDecimal(char ch) {
        int result = 0;
        switch (ch) {
            case '1' -> result = 1;
            case '2' -> result = 2;
            case '-' -> result = -1;
            case '=' -> result = -2;
        }
        return result;
    }

    private char decimalToSnafu(int x) {
        char result = '0';
        switch (x) {
            case 1 -> result = '1';
            case 2 -> result = '2';
            case -1 -> result = '-';
            case -2 -> result = '=';
        }
        return result;
    }

}

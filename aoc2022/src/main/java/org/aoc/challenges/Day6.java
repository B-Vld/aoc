package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.HashSet;

@Day(day = 6)
@Slf4j
public class Day6 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        char[] arr = Input.readAsString(fileName).toCharArray();
        log.info("Day 6 first challenge result : {}", numStartOfPacket(arr));
    }

    @Override
    public void secondChallenge(String fileName) {
        char[] arr = Input.readAsString(fileName).toCharArray();
        log.info("Day 6 second challenge result : {}", numStartOfPacket2(arr));
    }

    private int numStartOfPacket(char[] arr) {
        for (int i = 0; i < arr.length - 4; i++) {
            if (different(arr[i], arr[i + 1], arr[i + 2], arr[i + 3]))
                return i + 4;
        }
        return 0;
    }

    private boolean different(char a, char b, char c, char d) {
        var result = new HashSet<>();
        result.add(a);
        result.add(b);
        result.add(c);
        result.add(d);
        return result.size() == 4;
    }

    private int numStartOfPacket2(char[] arr) {
        for (int i = 0; i < arr.length - 14; i++) {
            if (different(arr[i], arr[i + 1], arr[i + 2], arr[i + 3]))
                if (message(arr, i))
                    return i + 14;
        }
        return 0;
    }

    private boolean message(char[] arr, int pos) {
        var s = new HashSet<Character>();
        for (int i = 0; i < 14; i++) {
            s.add(arr[pos + i]);
        }
        return s.size() == 14;
    }


}

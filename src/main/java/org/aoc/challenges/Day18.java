package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;
import org.aoc.utils.Pos3;

@Slf4j
@Day(day = 18)
public class Day18 implements Challenge {
    @Override
    public void firstChallenge(String fileName) {
        var input = Input.allLinesDefaultDelimiter(fileName)
                .map(line -> line.split(","))
                .map(str -> Pos3.of(str[0], str[1], str[2]))
                .toList();
        var result = 0;
        for (var pos : input) {
            for (var n : Pos3.neighbors(pos)) {
                if (!input.contains(n))
                    result++;
            }
        }

        log.info("Day 18 first challenge {}", result);
    }

    @Override
    public void secondChallenge(String fileName) {

    }
}

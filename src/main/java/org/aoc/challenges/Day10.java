package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.enums.Command;
import org.aoc.utils.Helper;
import org.aoc.utils.Input;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Day(day = 10)
public class Day10 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        int result = 0;
        var allCmdCycles = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> str.split(" "))
                .map(str -> Command.of(str[0]))
                .map(this::cycles)
                .toList();
        for (int i = 20; i <= 220; i += 40) {
            var fileLineLimiter = result(allCmdCycles, i);
            var r = (Input.readFirstXLines(fileName, fileLineLimiter)
                    .stream()
                    .flatMap(str -> Stream.of(str.split(" ")))
                    .filter(Helper::isNumeric)
                    .map(Integer::parseInt)
                    .reduce(0, Integer::sum) + 1) * i;
            result += r;
        }
        log.info("Day 10 first challenge {}", result);
    }

    @Override
    public void secondChallenge(String fileName) {

    }

    private int cycles(Command cmd) {
        int cycles = 0;
        switch (cmd) {
            case NOOP -> cycles = 1;
            case ADDX -> cycles = 2;
        }
        return cycles;
    }

    private int result(List<Integer> input, int cycleNumber) {
        int currentCycle = 0;
        for (int i = 0; i < input.size(); i++) {
            var j = input.get(i);
            currentCycle += j;
            if (currentCycle >= cycleNumber)
                return i;
        }
        return 0;
    }


}

package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Day(day = 1)
@Slf4j
public class Day1 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        Optional<Long> largest = totals(fileName).reduce(Math::max);
        largest.ifPresent(val -> log.info("Largest stack: {}", val));
    }

    @Override
    public void secondChallenge(String fileName) {
        Optional<Long> largestThree = totals(fileName)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(Math::addExact);
        largestThree.ifPresent(val -> log.info("Total of largest three stacks: {}", val));
    }

    private Stream<Long> totals(String fileName) {
        return Input.readString(fileName, "\\R\\R")
                .map(group -> Arrays.stream(group.split("\\R")).mapToLong(Long::parseLong).sum());
    }

}

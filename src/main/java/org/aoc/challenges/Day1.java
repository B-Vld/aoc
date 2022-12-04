package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

@Day(day = 1)
@Slf4j
public class Day1 {

    public void part1() {
        Optional<Long> largest = totals().reduce(Math::max);
        largest.ifPresent(val -> log.info("Largest stack: {}", val));
    }

    public void part2() {
        Optional<Long> largestThree = totals()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(Math::addExact);
        largestThree.ifPresent(val -> log.info("Total of largest three stacks: {}", val));
    }

    private Stream<Long> totals() {
        return Input.readString("day1.txt", "\r\n\r\n")
                .map(group -> Arrays.stream(group.split("\r\n")).mapToLong(Long::parseLong).sum());
    }

}

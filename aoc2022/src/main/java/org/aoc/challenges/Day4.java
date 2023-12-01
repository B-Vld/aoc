package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Day(day = 4)
@Slf4j
public class Day4 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var result = (int) Input.readFilePartitionedByDelimiter(fileName, ",")
                .stream()
                .filter(this::isPairInRange)
                .count();
        log.info("Day 4 first challenge : {}", result);
    }

    @Override
    public void secondChallenge(String fileName) {
        var result = (int) Input.readFilePartitionedByDelimiter(fileName, ",")
                .stream()
                .filter(this::isPairOverlapping)
                .count();
        log.info("Day 4 second challenge : {}", result);
    }

    private boolean isPairInRange(Pair<String, String> pair) {
        var pair1 = Pair.of(Integer.parseInt(pair.getLeft().split("-")[0]), Integer.parseInt(pair.getLeft().split("-")[1]));
        var pair2 = Pair.of(Integer.parseInt(pair.getRight().split("-")[0]), Integer.parseInt(pair.getRight().split("-")[1]));
        var set1 = IntStream.rangeClosed(pair1.getLeft(), pair1.getRight())
                .boxed()
                .collect(Collectors.toSet());
        var set2 = IntStream.rangeClosed(pair2.getLeft(), pair2.getRight())
                .boxed()
                .collect(Collectors.toSet());
        return set1.size() > set2.size() ? set1.containsAll(set2) : set2.containsAll(set1);
    }

    private boolean isPairOverlapping(Pair<String, String> pair) {
        var pair1 = Pair.of(Integer.parseInt(pair.getLeft().split("-")[0]), Integer.parseInt(pair.getLeft().split("-")[1]));
        var pair2 = Pair.of(Integer.parseInt(pair.getRight().split("-")[0]), Integer.parseInt(pair.getRight().split("-")[1]));
        var set1 = IntStream.rangeClosed(pair1.getLeft(), pair1.getRight())
                .boxed()
                .collect(Collectors.toSet());
        var set2 = IntStream.rangeClosed(pair2.getLeft(), pair2.getRight())
                .boxed()
                .collect(Collectors.toSet());
        return set1.stream().anyMatch(set2::contains);
    }

}

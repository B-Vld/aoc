package org.aoc.challenges;

import org.aoc.Challenge;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 implements Challenge<Integer> {

    @Override
    public Integer firstChallenge(String fileName) {
        return (int) Input.readFilePartitionedByDelimiter(fileName, ",")
                .stream()
                .filter(this::isPairInRange)
                .count();
    }

    @Override
    public Integer secondChallenge(String fileName) {
        return (int) Input.readFilePartitionedByDelimiter(fileName, ",")
                .stream()
                .filter(this::isPairOverlapping)
                .count();
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

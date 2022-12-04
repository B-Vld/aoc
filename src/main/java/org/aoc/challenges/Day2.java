package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.stream.Stream;

@Day(day = 2)
@Slf4j
public class Day2 {

    public void part1() {
        Optional<Integer> score = games()
                .map(this::score)
                .reduce(Math::addExact);
        score.ifPresent(val -> log.info("Total score: {}", val));
    }

    public void part2() {
        Optional<Integer> score = games()
                .map(pair -> Pair.of(pair.getLeft(), (pair.getLeft() + pair.getRight() + 2) % 3))
                .map(this::score)
                .reduce(Math::addExact);
        score.ifPresent(val -> log.info("Total score: {}", val));
    }

    private Stream<Pair<Integer, Integer>> games() {
        return Input.readString("day2.txt", "\r\n").map(val -> {
            String[] in = val.split(" ");
            return Pair.of(moveValue(in[0]), moveValue(in[1]));
        });
    }

    private Integer moveValue(String input) {
        return switch (input) {
            case "A", "X" -> 0;
            case "B", "Y" -> 1;
            case "C", "Z" -> 2;
            default -> null;
        };
    }

    private Integer score(Pair<Integer, Integer> pair) {
        int score = pair.getRight();
        if (pair.getRight().equals((pair.getRight() + 1) % 3)) {
            score += 6;
        } else if (pair.getLeft().equals(pair.getRight())) {
            score += 3;
        }

        return score + 1;
    }

}

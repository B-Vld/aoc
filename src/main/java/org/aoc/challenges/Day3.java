package org.aoc.challenges;

import org.aoc.Challenge;
import org.aoc.utils.Input;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Day3 implements Challenge<Integer> {

    @Override
    public Integer firstChallenge(String fileName) {
        var lines = Input.allLinesDefaultDelimiter(fileName).toList();
        int total = 0;
        for (var line : lines) {
            var comp1 = toIntegerSet(line.substring(0, line.length() / 2).toCharArray());
            var comp2 = toIntegerSet(line.substring(line.length() / 2).toCharArray());
            total += comp1.stream()
                    .filter(comp2::contains)
                    .mapToInt(i -> i)
                    .sum();
        }
        return total;
    }

    @Override
    public Integer secondChallenge(String fileName) {
        return Input.readFilePartitionedBySize(fileName, 3)
                .stream()
                .map(this::getCommonChars)
                .flatMap(Collection::stream)
                .mapToInt(this::getPriority)
                .sum();
    }

    private Set<Integer> toIntegerSet(char[] arr) {
        var result = new HashSet<Integer>();
        for (var c : arr) {
            if (Character.isLowerCase(c)) {
                result.add((int) c - 96);
            } else {
                result.add((int) c - 38);
            }
        }
        return result;
    }

    private int getPriority(char c) {
        return Character.isLowerCase(c) ? (int) c - 96 : (int) c - 38;
    }

    private Set<Character> getCommonChars(List<String> lines) {
        var linesAsCharSet = lines
                .stream()
                .map(e -> e.chars().mapToObj(p -> (char) p).collect(toSet()))
                .collect(toSet());
        var characters = new HashSet<Character>();
        linesAsCharSet.forEach(line -> {
            if (characters.isEmpty()) {
                characters.addAll(line);
            } else {
                characters.retainAll(line);
            }
        });
        return characters;
    }

}

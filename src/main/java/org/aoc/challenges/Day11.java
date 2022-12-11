package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.Mon;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Map.entry;

@Slf4j
@Day(day = 11)
public class Day11 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var map = initMap();
        IntStream.range(0, 20)
                .forEach(i -> IntStream.range(0, map.size())
                        .forEach(j -> map.get(j).round(map, j, 3L)));
        var result = map
                .values()
                .stream()
                .map(Mon::getInspected)
                .sorted(Comparator.reverseOrder())
                .toArray(Long[]::new);

        log.info("Day 11 first challenge : {}", result[0] * result[1]);
    }

    @Override
    public void secondChallenge(String fileName) {
        var map = initMap();
        IntStream.range(0, 10_000)
                .forEach(i -> IntStream.range(0, map.size())
                        .forEach(j -> map.get(j).round(map, j, 1L)));
        var result = map
                .values()
                .stream()
                .map(Mon::getInspected)
                .sorted(Comparator.reverseOrder())
                .toArray(Long[]::new);

        log.info("Day 11 first challenge : {}", result[0] * result[1]);
    }

    private Map<Integer, Mon> initMap() {
        return Map.ofEntries(
                entry(0, new Mon(new LinkedList<>(List.of(83L, 88L, 96L, 79L, 86L, 88L, 70L)), old -> old * 5L, test -> test % 11L == 0L, 0L)),
                entry(1, new Mon(new LinkedList<>(List.of(59L, 63L, 98L, 85L, 68L, 72L)), old -> old * 11L, test -> test % 5L == 0L, 0L)),
                entry(2, new Mon(new LinkedList<>(List.of(90L, 79L, 97L, 52L, 90L, 94L, 71L, 70L)), old -> old + 2L, test -> test % 19L == 0L, 0L)),
                entry(3, new Mon(new LinkedList<>(List.of(97L, 55L, 62L)), old -> old + 5L, test -> test % 13L == 0L, 0L)),
                entry(4, new Mon(new LinkedList<>(List.of(74L, 54L, 94L, 76L)), old -> old * old, test -> test % 7L == 0L, 0L)),
                entry(5, new Mon(new LinkedList<>(List.of(58L)), old -> old + 4L, test -> test % 17L == 0L, 0L)),
                entry(6, new Mon(new LinkedList<>(List.of(66L, 63L)), old -> old + 6L, test -> test % 2L == 0L, 0L)),
                entry(7, new Mon(new LinkedList<>(List.of(56L, 56L, 90L, 96L, 68L)), old -> old + 7L, test -> test % 3L == 0L, 0L))
        );
    }


}

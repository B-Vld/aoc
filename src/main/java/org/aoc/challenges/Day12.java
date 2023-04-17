package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.GridBFS;
import org.aoc.structures.dao.Position;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.System.arraycopy;

@Slf4j
@Day(day = 12)
public class Day12 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        char[][] input = inputToMatrix(Input.allLinesDefaultDelimiter(fileName).toList());
        var sAndE = startAndFinish(input);

        log.info("Day 12 first challenge : {}", GridBFS.getSteps(input, sAndE.getLeft(), sAndE.getRight()));
    }

    @Override
    public void secondChallenge(String fileName) {
        char[][] input = inputToMatrix(Input.allLinesDefaultDelimiter(fileName).toList());
        var posSet = new HashSet<Integer>();
        var sAndE = startAndFinish(input);
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (input[i][j] == 'a') {
                    int result = GridBFS.getSteps(input, Position.of(i, j), sAndE.getRight());
                    if (result >= 0)
                        posSet.add(result);
                }
            }
        }

        log.info("Day 12 first challenge : {}", posSet.stream().sorted().toArray()[0]);
    }

    private char[][] inputToMatrix(List<String> input) {
        char[][] result = new char[input.size()][input.get(0).length()];
        IntStream.range(0, input.size())
                .forEach(i -> {
                    var line = input.get(i).toCharArray();
                    arraycopy(line, 0, result[i], 0, line.length);
                });
        return result;
    }

    private Pair<Position, Position> startAndFinish(char[][] input) {
        Position start = null, finish = null;
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                if (input[i][j] == 'S')
                    start = new Position(i, j);
                if (input[i][j] == 'E')
                    finish = new Position(i, j);
            }
        }
        return Pair.of(start, finish);
    }

}

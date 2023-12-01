package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.enums.Blizzard;
import org.aoc.structures.BlizzardAStar;
import org.aoc.structures.dao.Position;
import org.aoc.utils.Helper;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Day(day = 24)
public class Day24 implements Challenge {
    @Override
    public void firstChallenge(String fileName) {
        var input = Input.allLinesDefaultDelimiter(fileName)
                .map(String::toCharArray)
                .toArray(char[][]::new);
        var sAndF = Pair.of(new Position(0, findPos(input[0])), new Position(input.length - 1, findPos(input[input.length - 1])));

        log.info("Day 24 first challenge : {}", new BlizzardAStar(sAndF, cacheBlizzards(input)).solve(1));
    }

    @Override
    public void secondChallenge(String fileName) {
        var input = Input.allLinesDefaultDelimiter(fileName)
                .map(String::toCharArray)
                .toArray(char[][]::new);
        var start = new Position(input.length - 1, findPos(input[input.length - 1]));
        var finish = new Position(0, findPos(input[0]));
        var sol = new BlizzardAStar(Pair.of(start, finish), cacheBlizzards(input));
        var result = 1;
        for (int i = 0; i < 3; i++) {
            sol.setStart(finish);
            sol.setFinish(start);
            result = sol.solve(result);
        }

        log.info("Day 24 first challenge : {}", result);
    }

    private Map<Integer, char[][]> cacheBlizzards(char[][] input) {
        Map<Integer, char[][]> result = new HashMap<>();
        Map<Blizzard, List<Position>> initialPositions = initialPosOfBlizzards(input);
        Map<Blizzard, List<Position>> nextPositions = new HashMap<>();
        int i = 0;
        int nrOfUniqueGrids = Helper.lcm(input.length - 2, input[0].length - 2);
        result.put(i, input);
        i++;
        while (i != nrOfUniqueGrids) {
            if (i == 1) {
                nextPositions = moveBlizzards(input, initialPositions);
            } else {
                nextPositions = moveBlizzards(input, nextPositions);
            }
            char[][] grid = createGrid(input, nextPositions);
            result.put(i++, grid);
        }
        return result;
    }

    private Map<Blizzard, List<Position>> initialPosOfBlizzards(char[][] input) {
        Map<Blizzard, List<Position>> result = new HashMap<>();
        Set<Blizzard> blizzardValues = Arrays.stream(Blizzard.values()).collect(toSet());
        blizzardValues.forEach(value -> result.put(value, new ArrayList<>()));
        for (int i = 1; i < input.length - 1; i++) {
            for (int j = 1; j < input[0].length - 1; j++) {
                var blizzardElem = Blizzard.of(input[i][j]);
                if (blizzardValues.contains(blizzardElem)) {
                    result.get(blizzardElem).add(Position.of(i, j));
                }
            }
        }
        return result;
    }

    private Map<Blizzard, List<Position>> moveBlizzards(char[][] input, Map<Blizzard, List<Position>> previousPositions) {
        Map<Blizzard, List<Position>> result = new HashMap<>();
        Set<Blizzard> blizzardValues = Arrays.stream(Blizzard.values()).collect(toSet());
        blizzardValues.forEach(value -> result.put(value, new ArrayList<>()));
        previousPositions.forEach(((blizzard, positions) -> {
            var prevPosList = previousPositions.get(blizzard);
            var newPosList = result.get(blizzard);
            switch (blizzard) {
                case UP -> {
                    for (Position pos : prevPosList) {
                        int row = pos.getX();
                        int col = pos.getY();
                        Position nPos;
                        if (input[row - 1][col] == '#') {
                            nPos = Position.of(input.length - 2, col);
                        } else {
                            nPos = Position.of(row - 1, col);
                        }
                        newPosList.add(nPos);
                    }
                }
                case DOWN -> {
                    for (Position pos : prevPosList) {
                        int row = pos.getX();
                        int col = pos.getY();
                        Position nPos;
                        if (input[row + 1][col] == '#') {
                            nPos = Position.of(1, col);
                        } else {
                            nPos = Position.of(row + 1, col);
                        }
                        newPosList.add(nPos);
                    }
                }
                case LEFT -> {
                    for (Position pos : prevPosList) {
                        int row = pos.getX();
                        int col = pos.getY();
                        Position nPos;
                        if (input[row][col - 1] == '#') {
                            nPos = Position.of(row, input[row].length - 2);
                        } else {
                            nPos = Position.of(row, col - 1);
                        }
                        newPosList.add(nPos);
                    }
                }
                case RIGHT -> {
                    for (Position pos : prevPosList) {
                        int row = pos.getX();
                        int col = pos.getY();
                        Position nPos;
                        if (input[row][col + 1] == '#') {
                            nPos = Position.of(row, 1);
                        } else {
                            nPos = Position.of(row, col + 1);
                        }
                        newPosList.add(nPos);
                    }
                }
            }
        }));
        return result;
    }


    private int findPos(char[] arr) {
        return IntStream.range(0, arr.length)
                .filter(i -> arr[i] == '.')
                .findFirst()
                .orElse(-1);
    }

    private char[][] createGrid(char[][] input, Map<Blizzard, List<Position>> pos) {
        char[][] result = Arrays.stream(input)
                .map(char[]::clone)
                .toArray(char[][]::new);
        Map<Position, List<Blizzard>> positionListMap = new HashMap<>();
        pos.forEach((blizzard, positions) ->
                positions.forEach(position -> {
                    if (positionListMap.containsKey(position)) {
                        positionListMap.get(position).add(blizzard);
                    } else {
                        List<Blizzard> nList = new ArrayList<>();
                        nList.add(blizzard);
                        positionListMap.put(position, nList);
                    }
                }));
        for (int i = 1; i < input.length - 1; i++) {
            for (int j = 1; j < input[0].length - 1; j++) {
                var p = Position.of(i, j);
                if (positionListMap.containsKey(p)) {
                    List<Blizzard> blizzards = positionListMap.get(p);
                    if (blizzards.size() > 1) {
                        result[i][j] = (char) (blizzards.size() + '0');
                    } else {
                        result[i][j] = blizzards.get(0).getValue();
                    }
                } else {
                    result[i][j] = '.';
                }
            }
        }
        return result;
    }


}

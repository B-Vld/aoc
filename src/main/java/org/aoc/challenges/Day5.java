package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.*;
import java.util.stream.Stream;

@Day(day = 5)
@Slf4j
public class Day5 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var crateLines = Input.readFirstXLines(fileName, 8)
                .stream()
                .map(this::toFixedCharArray)
                .toList();
        var map = getInputCrates(crateLines);
        Input.readSkipXLines(fileName, 10)
                .stream()
                .map(str -> str.replaceAll("[^0-9]+", " ").trim().split(" "))
                .map(this::toIntArray)
                .forEach(movement -> moveCratesFirst(map, movement[0], movement[1], movement[2]));
        log.info("Day 5 first challenge : {}", resultOne(map));
    }

    @Override
    public void secondChallenge(String fileName) {
        var crateLines = Input.readFirstXLines(fileName, 8)
                .stream()
                .map(this::toFixedCharArray)
                .toList();
        var map = getInputCrates(crateLines);
        Input.readSkipXLines(fileName, 10)
                .stream()
                .map(str -> str.replaceAll("[^0-9]+", " ").trim().split(" "))
                .map(this::toIntArray)
                .forEach(movement -> moveCratesSecond(map, movement[0], movement[1], movement[2]));
        log.info("Day 5 first challenge : {}", resultOne(map));
    }

    private Map<Integer, LinkedList<Character>> getInputCrates(List<Character[]> crateLines) {
        var result = new HashMap<Integer, LinkedList<Character>>();
        var idx = 1;
        for (var i = 0; i < 9; i++) {
            var lst = new LinkedList<Character>();
            for (var crate : crateLines) {
                if (crate[i] != ' ') {
                    lst.addFirst(crate[i]);
                }
            }
            result.put(idx, lst);
            idx++;
        }
        return result;
    }

    private Character[] toFixedCharArray(String str) {
        var result = new Character[9];
        var idx = 0;
        for (var i = 1; i < 34; i += 4) {
            try {
                result[idx] = str.charAt(i);
            } catch (StringIndexOutOfBoundsException e) {
                result[idx] = ' ';
            }
            idx++;
        }
        return result;
    }

    private void moveCratesFirst(Map<Integer, LinkedList<Character>> map, int move, int from, int to) {
        var lstFrom = map.get(from);
        var lstTo = map.get(to);
        for (int i = 0; i < move; i++) {
            if (lstFrom != null && lstFrom.peek() != null) {
                lstTo.addLast(lstFrom.removeLast());
            }
        }
    }

    private void moveCratesSecond(Map<Integer, LinkedList<Character>> map, int move, int from, int to) {
        var lstFrom = map.get(from);
        var lstTo = map.get(to);
        if(move == 1) {
            if (lstFrom != null && lstFrom.peek() != null) {
                lstTo.addLast(lstFrom.removeLast());
            }
        } else {
            var lstTmp = new LinkedList<Character>();
            for (int i = 0; i < move; i++) {
                lstTmp.addFirst(lstFrom.removeLast());
            }
            for (int i = 0; i < move; i++) {
                lstTo.addLast(lstTmp.removeFirst());
            }
        }
    }

    private String resultOne(Map<Integer, LinkedList<Character>> map) {
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> {
            var lst = map.get(k);
            if (lst.size() != 0) {
                builder.append(map.get(k).getLast());
            } else {
                builder.append(" ");
            }
        });
        return builder.toString();
    }

    private int[] toIntArray(String[] arr) {
        return Stream.of(arr)
                .mapToInt(Integer::parseInt)
                .toArray();
    }


}

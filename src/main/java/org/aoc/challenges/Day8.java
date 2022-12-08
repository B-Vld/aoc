package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Day(day = 8)
@Slf4j
public class Day8 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var grid = Input.allLinesDefaultDelimiter(fileName)
                .map(String::toCharArray)
                .map(this::toIntStream)
                .toList();

        var result = (97 * 4) + 4;

        for (int i = 1; i < grid.size() - 1; i++) {
            for (int j = 1; j < grid.get(i).length - 1; j++) {
                if (isVisible(grid, i, j))
                    result++;
            }
        }

        log.info("Day 8 first challenge : {}", result);

    }

    @Override
    public void secondChallenge(String fileName) {
        var grid = Input.allLinesDefaultDelimiter(fileName)
                .map(String::toCharArray)
                .map(this::toIntStream)
                .toList();
        var result = new ArrayList<Integer>();
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.get(i).length; j++) {
                result.add(scenicScore(grid, i, j));
            }
        }

        log.info("Day 8 second challenge : {}", result.stream().sorted(Comparator.reverseOrder()).toList().get(0));
    }

    private Integer[] toIntStream(char[] arr) {
        return IntStream.range(0, arr.length)
                .mapToObj(i -> arr[i] - '0')
                .toArray(Integer[]::new);
    }

    private int maxUp(List<Integer[]> forest, int xPos, int yPos) {
        int max = Integer.MIN_VALUE;
        for (int i = xPos - 1; i >= 0; i--) {
            var pos = forest.get(i)[yPos];
            if (pos > max) {
                max = pos;
            }
        }
        return max;
    }

    private int fovUp(List<Integer[]> forest, int xPos, int yPos) {
        var result = 0;
        var tree = forest.get(xPos)[yPos];
        for (int i = xPos - 1; i >= 0; i--) {
            var pos = forest.get(i)[yPos];
            if (pos < tree)
                result++;
            if (pos.equals(tree)) {
                result++;
                return result;
            }
            if (pos > tree) return result;
        }
        return result;
    }

    private int maxDown(List<Integer[]> forest, int xPos, int yPos) {
        int max = Integer.MIN_VALUE;
        for (int i = xPos + 1; i < forest.size(); i++) {
            var pos = forest.get(i)[yPos];
            if (pos > max) {
                max = pos;
            }
        }
        return max;
    }

    private int fovDown(List<Integer[]> forest, int xPos, int yPos) {
        var result = 0;
        var tree = forest.get(xPos)[yPos];
        for (int i = xPos + 1; i < forest.size(); i++) {
            var pos = forest.get(i)[yPos];
            if (pos < tree)
                result++;
            if (pos.equals(tree)) {
                result++;
                return result;
            }
            if (pos > tree) return result;
        }
        return result;
    }

    private int maxLeft(List<Integer[]> forest, int xPos, int yPos) {
        int max = Integer.MIN_VALUE;
        for (int i = yPos - 1; i >= 0; i--) {
            var pos = forest.get(xPos)[i];
            if (pos > max) {
                max = pos;
            }
        }
        return max;
    }

    private int fovLeft(List<Integer[]> forest, int xPos, int yPos) {
        var result = 0;
        var tree = forest.get(xPos)[yPos];
        for (int i = yPos - 1; i >= 0; i--) {
            var pos = forest.get(xPos)[i];
            if (pos < tree)
                result++;
            if (pos.equals(tree)) {
                result++;
                return result;
            }
            if (pos > tree) return result;
        }
        return result;
    }


    private int maxRight(List<Integer[]> forest, int xPos, int yPos) {
        int max = Integer.MIN_VALUE;
        for (int i = yPos + 1; i < forest.size(); i++) {
            var pos = forest.get(xPos)[i];
            if (pos > max) {
                max = pos;
            }
        }
        return max;
    }

    private int fovRight(List<Integer[]> forest, int xPos, int yPos) {
        var result = 0;
        var tree = forest.get(xPos)[yPos];
        for (int i = yPos + 1; i < forest.size(); i++) {
            var pos = forest.get(xPos)[i];
            if (pos < tree)
                result++;
            if (pos.equals(tree)) {
                result++;
                return result;
            }
            if (pos > tree) return result;
        }
        return result;
    }

    private boolean isVisible(List<Integer[]> forest, int xPos, int yPos) {
        var tree = forest.get(xPos)[yPos];
        var up = maxUp(forest, xPos, yPos);
        var down = maxDown(forest, xPos, yPos);
        var left = maxLeft(forest, xPos, yPos);
        var right = maxRight(forest, xPos, yPos);
        return tree > up || tree > down || tree > left || tree > right;
    }

    private int scenicScore(List<Integer[]> forest, int xPos, int yPos) {
        var fovUp = fovUp(forest, xPos, yPos) == 0 ? 1 : fovUp(forest, xPos, yPos);
        var fovDown = fovDown(forest, xPos, yPos) == 0 ? 1 : fovDown(forest, xPos, yPos);
        var fovLeft = fovLeft(forest, xPos, yPos) == 0 ? 1 : fovLeft(forest, xPos, yPos);
        var fovRight = fovRight(forest, xPos, yPos) == 0 ? 1 : fovRight(forest, xPos, yPos);
        return fovUp * fovDown * fovLeft * fovRight;
    }


}

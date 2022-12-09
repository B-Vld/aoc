package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.enums.Direction;
import org.aoc.utils.Input;
import org.aoc.utils.Position;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Day(day = 9)
public class Day9 implements Challenge {

    @Override
    public void firstChallenge(String fileName) {
        var head = new Position(0, 0);
        var tail = new Position(0, 0);

        var commands = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> str.split(" "))
                .map(str -> Pair.of(Direction.valueOf(str[0]), Integer.parseInt(str[1])))
                .toList();

        log.info("Day 9 first challenge : {}", visitedByTail(commands, head, tail));
    }

    @Override
    public void secondChallenge(String fileName) {
        var commands = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> str.split(" "))
                .map(str -> Pair.of(Direction.valueOf(str[0]), Integer.parseInt(str[1])))
                .toList();

        log.info("Day 9 second challenge : {}", countVisited(commands));
    }

    private int visitedByTail(List<Pair<Direction, Integer>> commands, Position head, Position tail) {
        var visitedSet = new HashSet<Position>();

        for (var cmd : commands) {
            var direction = cmd.getLeft();
            var steps = cmd.getRight();
            for (var i = 0; i < steps; i++) {
                switch (direction) {
                    case U -> {
                        head.setX(head.getX() + 1);
                        if (isTailNotInRange(head, tail)) {
                            tail.setX(head.getX() - 1);
                            tail.setY(head.getY());
                        }
                        visitedSet.add(new Position(tail.getX(), tail.getY()));
                    }
                    case D -> {
                        head.setX(head.getX() - 1);
                        if (isTailNotInRange(head, tail)) {
                            tail.setX(head.getX() + 1);
                            tail.setY(head.getY());
                        }
                        visitedSet.add(new Position(tail.getX(), tail.getY()));
                    }
                    case L -> {
                        head.setY(head.getY() - 1);
                        if (isTailNotInRange(head, tail)) {
                            tail.setY(head.getY() + 1);
                            tail.setX(head.getX());
                        }
                        visitedSet.add(new Position(tail.getX(), tail.getY()));
                    }
                    case R -> {
                        head.setY(head.getY() + 1);
                        if (isTailNotInRange(head, tail)) {
                            tail.setY(head.getY() - 1);
                            tail.setX(head.getX());
                        }
                        visitedSet.add(new Position(tail.getX(), tail.getY()));
                    }
                }
            }
        }
        return visitedSet.size();
    }

    private int countVisited(List<Pair<Direction, Integer>> directions) {
        var result = new HashSet<Position>();
        Position head = new Position(0, 0);
        Position[] tail = new Position[9];
        for (int i = 0; i < 9; i++) {
            tail[i] = new Position(0, 0);
        }
        for (var direction : directions) {
            for (int i = 0; i < direction.getRight(); i++) {
                head = head.applyMove(direction.getLeft());
                Position neighbor = head;
                for (int j = 0; j < 9; j++) {
                    tail[j] = tail[j].followHead(neighbor);
                    neighbor = tail[j];
                }
                result.add(tail[9 - 1]);
            }
        }
        return result.size();
    }

    boolean isTailNotInRange(Position head, Position tail) {
        return (Math.abs(head.getX() - tail.getX()) != 0 && Math.abs(head.getX() - tail.getX()) != 1) ||
                (Math.abs(head.getY() - tail.getY()) != 0 && Math.abs(head.getY() - tail.getY()) != 1);
    }

}

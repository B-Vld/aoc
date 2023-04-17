package org.aoc.structures;

import org.aoc.structures.dao.BlizzardPosition;
import org.aoc.structures.dao.Position;
import org.aoc.utils.BlizzardPositionComparator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class BlizzardAStar {

    private static final int[][] positions = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {0, 0}};
    private Position start;
    private Position finish;
    private final Map<Integer, char[][]> cachedBlizzards;

    public BlizzardAStar(Pair<Position, Position> sAndF, Map<Integer, char[][]> cachedBlizzards) {
        this.start = sAndF.getLeft();
        this.finish = sAndF.getRight();
        this.cachedBlizzards = cachedBlizzards;
    }

    public BlizzardAStar(Map<Integer, char[][]> cachedBlizzards) {
        this.cachedBlizzards = cachedBlizzards;
    }

    private static boolean isValid(char[][] nextMap, BlizzardPosition nextPos) {
        int row = nextPos.x(), col = nextPos.y();
        return row >= 0 &&
                row < nextMap.length &&
                col >= 0 &&
                col < nextMap[0].length;
    }


    public int solve(int minute) {
        int maxIdxCachedBlizzard = cachedBlizzards.size();

        BlizzardPosition bStart = BlizzardPosition.of(start.getX(), start.getY(), minute);
        BlizzardPosition bFinish = BlizzardPosition.of(finish.getX(), finish.getY(), Integer.MIN_VALUE);

        BlizzardPositionComparator blizzardPositionComparator = new BlizzardPositionComparator(bFinish);
        Queue<BlizzardPosition> positionQueue = new PriorityQueue<>(blizzardPositionComparator);
        Set<BlizzardPosition> visitedSet = new HashSet<>();

        positionQueue.add(bStart);

        while (!positionQueue.isEmpty()) {

            BlizzardPosition currPosition = positionQueue.poll();
            if (visitedSet.contains(currPosition))
                continue;
            else visitedSet.add(currPosition);

            if (currPosition.equalsCord(bFinish)) {
                return currPosition.minute() - 1;
            }

            char[][] nextMap = cachedBlizzards.get((currPosition.minute() % maxIdxCachedBlizzard));

            List<BlizzardPosition> nextValidPositions = fetchNextValidPositions(nextMap, currPosition, currPosition.minute() + 1, visitedSet);
            positionQueue.addAll(nextValidPositions);

        }

        return -1;
    }

    private List<BlizzardPosition> fetchNextValidPositions(char[][] nextMap, BlizzardPosition currPosition, int minutes, Set<BlizzardPosition> visitedSet) {
        List<BlizzardPosition> nextValidPositions = new ArrayList<>();

        for (int[] directions : positions) {
            BlizzardPosition nextPosition = BlizzardPosition.of(directions[0] + currPosition.x(),
                    directions[1] + currPosition.y(),
                    minutes);
            if (isValid(nextMap, nextPosition) &&
                    nextMap[nextPosition.x()][nextPosition.y()] == '.' &&
                    !visitedSet.contains(nextPosition)) {
                nextValidPositions.add(nextPosition);
            }
        }

        return nextValidPositions;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public void setFinish(Position finish) {
        this.finish = finish;
    }
}

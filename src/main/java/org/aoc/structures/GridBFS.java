package org.aoc.structures;

import lombok.Data;
import org.aoc.utils.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
public class GridBFS {

    public static final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static int getSteps(char[][] input, Position start, Position finish) {
        var qStart = new QItem(start.getX(), start.getY(), 0, new ArrayList<>());
        boolean[][] visited = new boolean[input.length][input[0].length];
        visited[qStart.row][qStart.col] = true;
        var q = new LinkedList<QItem>();
        q.add(qStart);
        while (!q.isEmpty()) {
            var curr = q.poll();
            if (input[curr.row][curr.col] == input[finish.getX()][finish.getY()]) {
                //printPath(input, curr.path);
                return curr.dist;
            }
            for (int[] direction : directions) {
                int nextRow = curr.row + direction[0];
                int nextCol = curr.col + direction[1];
                if (isValid(nextRow, nextCol, input, visited)) {
                    if (checkCharacterNextPosition(curr.row, curr.col, nextRow, nextCol, input)) {
                        var item = new QItem(nextRow, nextCol, curr.dist + 1, new ArrayList<>());
                        item.getPath().addAll(curr.getPath());
                        item.getPath().add(new Position(curr.row, curr.col));
                        q.addLast(item);
                        visited[nextRow][nextCol] = true;
                    }
                }
            }
        }
        //printVisited(visited);
        return -1;
    }

    private static boolean checkCharacterNextPosition(int curRow, int curCol, int nextRow, int nextCol, char[][] input) {
        char currentChar = input[curRow][curCol];
        char nextChar = input[nextRow][nextCol];
        if (currentChar == 'S' || (currentChar == 'z' && nextChar == 'E')) {
            return true;
        }
        return nextChar <= currentChar || nextChar == currentChar + 1;
    }

    private static boolean isValid(int row, int col, char[][] grid, boolean[][] visited) {
        return row >= 0 &&
                row < grid.length &&
                col >= 0 &&
                col < grid[0].length &&
                !visited[row][col];
    }

    private static void printVisited(boolean[][] visited) {
        Arrays.stream(visited)
                .forEach(l -> {
                    for (var c : l) {
                        if (c) System.out.print("* ");
                        else System.out.print("x ");
                    }
                    System.out.println();
                });
        System.out.println();
    }

    private static void printPath(char[][] input, List<Position> path) {
        char[][] shortestPath = input.clone();
        path.forEach(pos -> shortestPath[pos.getX()][pos.getY()] = '#');
        Arrays.stream(shortestPath).forEach(i -> {
            for (char c : i) System.out.print(c + " ");
            System.out.println();
        });
    }

}

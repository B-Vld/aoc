package org.aoc.utils;

import org.aoc.enums.Direction;

import java.util.Objects;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Position applyMove(Direction direction) {
        return switch (direction) {
            case U -> new Position(x, y + 1);
            case D -> new Position(x, y - 1);
            case L -> new Position(x - 1, y);
            case R -> new Position(x + 1, y);
        };
    }

    public Position followHead(Position head) {
        boolean shouldMove = Math.abs(x - head.x) > 1 || Math.abs(y - head.y) > 1;

        if (!shouldMove) {
            return this;
        }

        Position coordinates = new Position(x, y);

        if (x > head.x) {
            coordinates.x--;
        } else if (x < head.x) {
            coordinates.x++;
        }

        if (y > head.y) {
            coordinates.y--;
        } else if (y < head.y) {
            coordinates.y++;
        }

        return coordinates;
    }

}

package org.aoc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aoc.enums.Direction;

@Data
@AllArgsConstructor
public class Position {
    private int x;
    private int y;

    public static Position of(int x, int y) {
        return new Position(x, y);
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

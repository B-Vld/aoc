package org.aoc.structures.dao;

import java.util.Objects;

public record BlizzardPosition(int x, int y, int minute) {

    public static BlizzardPosition of(int x, int y, int minute) {
        return new BlizzardPosition(x, y, minute);
    }

    public boolean equalsCord(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlizzardPosition that = (BlizzardPosition) o;
        // exclude the minute
        return x == that.x && y == that.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlizzardPosition that = (BlizzardPosition) o;
        return x == that.x && y == that.y && minute == that.minute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, minute);
    }
}

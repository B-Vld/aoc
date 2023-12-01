package org.aoc.utils;

import org.aoc.structures.dao.BlizzardPosition;

import java.util.Comparator;

public class BlizzardPositionComparator implements Comparator<BlizzardPosition> {
    private final BlizzardPosition finish;

    public BlizzardPositionComparator(BlizzardPosition finish) {
        this.finish = finish;
    }

    @Override
    public int compare(BlizzardPosition o1, BlizzardPosition o2) {
        // compute heuristic
        int p1 = o1.minute() + Math.abs(finish.x() - o1.x()) + Math.abs(finish.y() - o1.y());
        int p2 = o2.minute() + Math.abs(finish.x() - o2.x()) + Math.abs(finish.y() - o2.y());
        return Integer.compare(p1, p2);
    }

}

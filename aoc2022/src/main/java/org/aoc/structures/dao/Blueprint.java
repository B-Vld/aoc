package org.aoc.structures.dao;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;

public record Blueprint(int bIdx, int cOre, int cClay, Pair<Integer, Integer> cObsidian,
                        Pair<Integer, Integer> cGeode) {
    public static Blueprint toBlueprint(int[] arr) {
        return new Blueprint(
                arr[0],
                arr[1],
                arr[2],
                Pair.of(arr[3], arr[4]),
                Pair.of(arr[5], arr[6])
        );
    }

    public int maxOre() {
        return Arrays.stream(new int[]{cOre, cClay, cObsidian().getLeft(), cGeode().getLeft()})
                .max()
                .orElse(0);
    }

    public int maxClay() {
        return cObsidian.getRight();
    }

    public int maxObsidian() {
        return cGeode.getRight();
    }
}

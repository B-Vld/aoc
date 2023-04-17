package org.aoc.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Blizzard {

    LEFT('<'),
    RIGHT('>'),
    UP('^'),
    DOWN('v');

    private final char value;

    Blizzard(char value) {
        this.value = value;
    }

    public static Blizzard of(char c) {
        return Arrays.stream(Blizzard.values())
                .filter(val -> c == val.getValue())
                .findFirst()
                .orElse(null);
    }

}

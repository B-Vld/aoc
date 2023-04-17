package org.aoc.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Command {

    ADDX("addx"),
    NOOP("noop");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public static Command of(String value) {
        return Arrays.stream(Command.values())
                .filter(v -> value.equalsIgnoreCase(v.getValue()))
                .findFirst()
                .orElse(null);
    }

}

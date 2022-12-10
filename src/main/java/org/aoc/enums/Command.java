package org.aoc.enums;

import java.util.Arrays;

public enum Command {

    ADDX("addx"),
    NOOP("noop");

    private String value;

    Command(String value) {
        this.value = value;
    }

    public static Command of(String value) {
        return Arrays.stream(Command.values())
                .filter(v -> value.equalsIgnoreCase(v.getValue()))
                .findFirst()
                .orElse(null);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

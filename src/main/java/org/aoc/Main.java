package org.aoc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static java.lang.String.format;

@Slf4j
public class Main {

    private static final int DAY_OF_MONTH = LocalDate.now().getDayOfMonth();
    private static final String CHALLENGES = "org.aoc.challenges.";

    public static void main(String[] args) {
        if (DAY_OF_MONTH <= 25) {
            Challenge challenge = getChallenge();
            assert challenge != null;
            challenge.firstChallenge(getFileName());
            challenge.secondChallenge(getFileName());
        } else {
            log.info("AoC22 finished");
        }
    }

    static String getClassName() {
        return format("Day%d", Main.DAY_OF_MONTH);
    }

    static Challenge getChallenge() {
        try {
            return (Challenge) Class.forName(format("%s%s", CHALLENGES, getClassName())).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            log.error(format("Challenge not found for day %d", DAY_OF_MONTH));
        }
        return null;
    }

    static String getFileName() {
        return format("day%d.txt", DAY_OF_MONTH);
    }

}
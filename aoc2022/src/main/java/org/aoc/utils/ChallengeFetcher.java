package org.aoc.utils;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;

import static java.lang.String.format;

@Slf4j
public class ChallengeFetcher {

    private static final String CHALLENGES_DAY = "org.aoc.challenges.Day";

    public static Pair<Challenge, String> challengeInputPair(int day) {
        return Pair.of(getChallenge(day), getInput(day));
    }

    private static Challenge getChallenge(int day) {
        try {
            return (Challenge) Class.forName(format("%s%d", CHALLENGES_DAY, day)).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            log.error(format("Challenge not found for day %d. Maybe in js-solutions.", day));
        }
        return null;
    }

    private static String getInput(int day) {
        return format("day%d.txt", day);
    }


}

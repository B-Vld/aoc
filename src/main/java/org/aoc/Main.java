package org.aoc;

import lombok.extern.slf4j.Slf4j;
import org.aoc.utils.ChallengeFetcher;

import java.time.LocalDate;

@Slf4j
public class Main {

    public static void main(String[] args) {
        final var DAY_OF_MONTH = LocalDate.now().getDayOfMonth();
        if (DAY_OF_MONTH <= 25) {
            var challengePair = ChallengeFetcher.challengeInputPair(DAY_OF_MONTH);
            var challenge = challengePair.getLeft();
            var inputFile = challengePair.getRight();
            challenge.firstChallenge(inputFile);
            challenge.secondChallenge(inputFile);
        } else {
            log.info("AoC22 finished");
        }
    }

}
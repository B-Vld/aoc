package org.aoc;

import lombok.extern.slf4j.Slf4j;
import org.aoc.utils.ChallengeFetcher;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class Main {

    public static void main(String[] args) {
//        final var DAY_OF_MONTH = LocalDate.now().getDayOfMonth();
        final var DAY_OF_MONTH = 24;
        if (DAY_OF_MONTH <= 25) {
            var challengePair = ChallengeFetcher.challengeInputPair(DAY_OF_MONTH);
            var challenge = challengePair.getLeft();
            var inputFile = challengePair.getRight();
            runChallenges(challenge, inputFile);
        } else {
            log.info("AoC22 finished");
        }
    }

    private static void runChallenges(Challenge challenge, String inputFile) {
        Instant start1 = Instant.now();
        challenge.firstChallenge(inputFile);
        Instant end1 = Instant.now();
        log.info("Runtime first challenge : {} ms", Duration.between(start1, end1).toMillis());

        Instant start2 = Instant.now();
        challenge.secondChallenge(inputFile);
        Instant end2 = Instant.now();
        log.info("Runtime second challenge : {} ms", Duration.between(start2, end2).toMillis());
    }

}
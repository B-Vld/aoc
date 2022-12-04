package challenges;

import org.aoc.challenges.Day3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day3Test implements ChallengeTest {

    private final Day3 sut = new Day3();
    private static final String DAY3 = "day3.txt";

    @Test
    public void firstChallenge() {
        assertEquals(7997, sut.firstChallenge(DAY3).intValue());
    }

    @Test
    public void secondChallenge() {
        assertEquals(2545, sut.secondChallenge(DAY3).intValue());
    }

}

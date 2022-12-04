package challenges;

import org.aoc.challenges.Day4;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Day4Test implements ChallengeTest {

    private final Day4 sut = new Day4();
    private static final String DAY4 = "day4.txt";

    @Test
    public void firstChallenge() {
        assertEquals(7997, sut.firstChallenge(DAY4).intValue());
    }

    @Test
    public void secondChallenge() {
        assertEquals(2545, sut.secondChallenge(DAY4).intValue());
    }

}

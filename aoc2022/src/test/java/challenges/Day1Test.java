package challenges;

import org.junit.jupiter.api.Test;

import org.aoc.challenges.Day1;

public class Day1Test extends AbstractChallengeTest {

    public Day1Test() {
        super(Day1.class, new Day1());
    }

    @Test
    public void p1Example() {
        assertFirstChallenge("day1-example.txt", "Largest stack: 24000");
    }

    @Test
    public void p2Example() {
        assertSecondChallenge("day1-example.txt", "Total of largest three stacks: 45000");
    }
}

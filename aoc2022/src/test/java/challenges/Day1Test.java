package challenges;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

import org.aoc.challenges.Day1;

public class Day1Test {

    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Day1.class);
    private ListAppender<ILoggingEvent> listAppender;
    private final Day1 day1 = new Day1();

    @BeforeEach
    public void setUp() {
        listAppender = new ListAppender<>();
        listAppender.start();

        LOGGER.addAppender(listAppender);
        LOGGER.setLevel(ch.qos.logback.classic.Level.INFO);
    }

    @AfterEach
    public void tearDown() {
        LOGGER.detachAndStopAllAppenders();
    }

    @Test
    public void p1Example() {
        day1.firstChallenge("day1-example.txt");

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .contains("Largest stack: 24000");
    }
}

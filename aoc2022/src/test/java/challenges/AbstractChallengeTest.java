package challenges;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.aoc.Challenge;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractChallengeTest {

    private final Class<? extends Challenge> challengeClass;
    private final Challenge challenge;
    private Logger logger;
    private ListAppender<ILoggingEvent> listAppender;

    protected AbstractChallengeTest(Class<? extends Challenge> challengeClass, Challenge challenge) {
        this.challengeClass = challengeClass;
        this.challenge = challenge;
    }

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(challengeClass);
        listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);
        logger.setLevel(Level.INFO);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(listAppender);
        listAppender.stop();
    }

    protected void assertFirstChallenge(String inputFile, String expectedLogMessage) {
        challenge.firstChallenge(inputFile);

        assertThat(loggedMessages()).contains(expectedLogMessage);
    }

    protected void assertSecondChallenge(String inputFile, String expectedLogMessage) {
        challenge.secondChallenge(inputFile);

        assertThat(loggedMessages()).contains(expectedLogMessage);
    }

    protected List<String> loggedMessages() {
        return listAppender.list
                .stream()
                .map(ILoggingEvent::getFormattedMessage)
                .toList();
    }
}

package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

public class MatchingWindowTest {

    @Test
    public void shouldReturnTrueIfScoreIsAboveThreshold() {
        MatchingWindow window = new MatchingWindow();
        window.setScore(0.7f);
        assertThat(window.isScoreAboveThreshold(0.5), equalTo(true));
    }

    @Test
    public void shouldReturnTrueIfScoreIsEqualToThreshold() {
        MatchingWindow window = new MatchingWindow();
        window.setScore(0.5f);
        assertThat(window.isScoreAboveThreshold(0.5), equalTo(true));
    }

    @Test
    public void shouldReturnFalseIfScoreIsBelowThreshold() {
        MatchingWindow window = new MatchingWindow();
        window.setScore(0.49f);
        assertThat(window.isScoreAboveThreshold(0.5), equalTo(false));
    }

    @Test
    public void test() {
        System.out.println("(hello world".replace("(", "\\(").replace(")", "\\)"));
    }

}
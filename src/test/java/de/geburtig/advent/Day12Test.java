package de.geburtig.advent;

import de.geburtig.advent.y2022.Day12;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * TODO: No programmatic solution yet
 */
@Disabled
class Day12Test {
    private final Day12 subject = new Day12();

    @Test
    void resolveExample1() throws Exception {
        assertEquals("31", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("361", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("354", subject.resolvePuzzle2());
    }

    @Test
    void resolveExample2() throws Exception {
        assertEquals("2713310158", subject.resolveExample2());
    }
}
package de.geburtig.advent;

import de.geburtig.advent.y2022.Day19;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <a href="https://adventofcode.com/2022/day/19" />
 */
class Day19Test {
    private final Day19 subject = new Day19();

    @Test
    void resolveExample1() {
        assertEquals("33", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("?", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("?", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("?", subject.resolvePuzzle2());
    }
}
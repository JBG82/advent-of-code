package de.geburtig.advent;

import de.geburtig.advent.y2022.Day18;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <a href="https://adventofcode.com/2022/day/18" />
 */
class Day18Test {
    private final Day18 subject = new Day18();

    @Test
    void resolveExample1() {
        assertEquals("64", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("4242", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("58", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("2428", subject.resolvePuzzle2());
    }
}
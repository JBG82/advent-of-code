package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day5Test {
    private final Day5 subject = new Day5();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("FWSHSPJWM", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("PWPWHGFZS", subject.resolvePuzzle2());
    }
}
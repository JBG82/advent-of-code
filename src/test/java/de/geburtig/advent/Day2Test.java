package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day2Test {
    private final Day2 subject = new Day2();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(14163, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(12091, subject.resolvePuzzle2());
    }
}
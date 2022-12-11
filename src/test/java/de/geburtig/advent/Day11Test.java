package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
    private final Day11 subject = new Day11();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("99840", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("99840", subject.resolvePuzzle2());
    }
}
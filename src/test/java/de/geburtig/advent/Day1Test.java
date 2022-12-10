package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day1Test {
    private final Day1 subject = new Day1();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(66616, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(199172, subject.resolvePuzzle2());
    }
}
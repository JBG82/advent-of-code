package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {
    private final Day4 subject = new Day4();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(567, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(907, subject.resolvePuzzle2());
    }
}
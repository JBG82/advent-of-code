package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {
    private final Day7 subject = new Day7();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("1443806", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        assertEquals("942298", subject.resolvePuzzle2());
    }
}
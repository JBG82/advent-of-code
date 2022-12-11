package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
    private final Day10 subject = new Day10();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("14220", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        subject.resolvePuzzle2();
        // Result is "ZRARLFZU", watch the console output
    }
}
package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {
    private final Day9 subject = new Day9();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("5907", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        assertEquals("2303", subject.resolvePuzzle2());
    }
}
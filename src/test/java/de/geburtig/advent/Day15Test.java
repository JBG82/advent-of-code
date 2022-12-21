package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {
    private final Day15 subject = new Day15();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("5144286", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample1() {
        assertEquals("26", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("10229191267339", subject.resolvePuzzle2());
    }
}
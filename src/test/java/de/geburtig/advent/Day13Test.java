package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {
    private final Day13 subject = new Day13();

    @Test
    void resolveExample1() throws Exception {
        assertEquals("13", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("6046", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("21423", subject.resolvePuzzle2());
    }

    @Test
    void resolveExample2() throws Exception {
        assertEquals("140", subject.resolveExample2());
    }
}
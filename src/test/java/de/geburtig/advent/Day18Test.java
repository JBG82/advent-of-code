package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {
    private final Day18 subject = new Day18();

    @Test
    void resolveExample1() {
        assertEquals("64", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("?", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("?", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("?", subject.resolvePuzzle2());
    }
}
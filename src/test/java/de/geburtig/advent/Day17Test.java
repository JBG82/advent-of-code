package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {
    private final Day17 subject = new Day17();

    @Test
    void resolveExample1() {
        assertEquals("3068", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("3106", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("1514285714288", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("1537175792495", subject.resolvePuzzle2());
    }
}
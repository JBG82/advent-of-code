package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {
    private final Day16 subject = new Day16();

    @Test
    void resolveExample1() {
        assertEquals("1651", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("2330", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("10229191267339", subject.resolvePuzzle2());
    }
}
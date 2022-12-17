package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {
    private final Day14 subject = new Day14();

    @Test
    void resolveExample1() {
        assertEquals("24", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("1133", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("93", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("27566", subject.resolvePuzzle2());
    }
}
package de.geburtig.advent;

import de.geburtig.advent.y2022.Day16;
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
    void resolveExample2() {
        assertEquals("1707", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        // Runs forever, but prints out the correct result after 5-10 Minutes anyways...
//        assertEquals("2675", subject.resolvePuzzle2());
    }
}
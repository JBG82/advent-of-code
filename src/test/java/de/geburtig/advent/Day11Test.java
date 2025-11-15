package de.geburtig.advent;

import de.geburtig.advent.y2022.Day11;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class Day11Test {
    private final Day11 subject = new Day11();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("99840", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("20683044837", subject.resolvePuzzle2());
    }

    @Test
    void resolveExample1() throws Exception {
        assertEquals("10605", subject.resolveExample1());
    }

    @Test
    void resolveExample2() throws Exception {
        assertEquals("2713310158", subject.resolveExample2());
    }
}
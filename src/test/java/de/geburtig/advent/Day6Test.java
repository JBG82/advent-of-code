package de.geburtig.advent;

import de.geburtig.advent.y2022.Day6;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test {
    private final Day6 subject = new Day6();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(1140, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(3495, subject.resolvePuzzle2());
    }
}
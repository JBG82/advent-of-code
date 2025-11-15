package de.geburtig.advent;

import de.geburtig.advent.y2022.Day1;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class Day1Test {
    private final Day1 subject = new Day1();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(66616, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(199172, subject.resolvePuzzle2());
    }
}
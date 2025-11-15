package de.geburtig.advent;

import de.geburtig.advent.y2022.Day4;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class Day4Test {
    private final Day4 subject = new Day4();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(567, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(907, subject.resolvePuzzle2());
    }
}
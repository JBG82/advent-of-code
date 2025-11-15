package de.geburtig.advent;

import de.geburtig.advent.y2022.Day9;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class Day9Test {
    private final Day9 subject = new Day9();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("5907", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        assertEquals("2303", subject.resolvePuzzle2());
    }
}
package de.geburtig.advent;

import de.geburtig.advent.y2022.Day8;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class Day8Test {
    private final Day8 subject = new Day8();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("1827", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        assertEquals("335580", subject.resolvePuzzle2());
    }
}
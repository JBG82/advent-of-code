package de.geburtig.advent;

import de.geburtig.advent.y2022.Day10;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
class Day10Test {
    private final Day10 subject = new Day10();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("14220", subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() {
        subject.resolvePuzzle2();
        // Result is "ZRARLFZU", watch the console output
    }
}
package de.geburtig.advent;

import de.geburtig.advent.y2022.Day3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day3Test {
    private final Day3 subject = new Day3();

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals(7785, subject.resolvePuzzle1());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals(2633, subject.resolvePuzzle2());
    }

    @Test
    void testChars() {
        System.out.println("a " + (int)'a');
        System.out.println("z " + (int)'z');
        System.out.println("A " + (int)'A');
        System.out.println("Z " + (int)'Z');
    }
}
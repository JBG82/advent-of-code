package de.geburtig.advent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {
    private final Day17 subject = new Day17();

    @Test
    void resolveExample1() {
        assertEquals("3068", subject.resolveExample1());
    }

    @Test
    void resolvePuzzle1() throws Exception {
        assertEquals("3106", subject.resolvePuzzle1());
    }

    @Test
    void resolveExample2() {
        assertEquals("1514285714288", subject.resolveExample2());
    }

    @Test
    void resolvePuzzle2() throws Exception {
        assertEquals("?", subject.resolvePuzzle2());
    }

    @Test
    void test() {
        int[] test = {1,2,3,4,5,6,7,8,9,10};
        int[] target = new int[20];
        System.arraycopy(test, 5, target, 0, test.length - 5);

        System.out.print("[");
        for (int i = 0; i < target.length; ++i) {
            System.out.print(target[i] + ",");
        }
        System.out.println("]");


    }
}
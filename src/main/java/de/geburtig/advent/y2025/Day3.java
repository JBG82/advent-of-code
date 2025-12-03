/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * https://adventofcode.com/2025/day/3
 */
public class Day3 {
    public static void main(String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day3.txt", Day3.class);

        long result = 0;
        for (String line : input) result += fetchJoltageFromLinePart2(line);

        // Example: 357 (Part 1)
        // Example: 3121910778619 (Part 2)
        // Part1:   17095
        // Part2:   168794698570517
        System.out.println(result);
    }

    private static int fetchJoltageFromLinePart1(String line) {
        int firstValue = 0, secondValue = 0;
        for (int i = 0; i < line.length(); i++) {
            int v = line.charAt(i) - 48;
            if (v > firstValue && i < line.length() - 1) {
                firstValue = v;
                secondValue = -1;
            } else if (v > secondValue) {
                secondValue = v;
            }
        }
        return firstValue * 10 + secondValue;
    }

    private static long fetchJoltageFromLinePart2(String line) {
        int[] highestValue = new int[12];
        for (int i = 0; i < line.length(); i++) {
            int v = line.charAt(i) - 48;
            boolean overrun = false;
            for (int n = 0; n < 12; ++n) {
                if (overrun) {
                    highestValue[n] = 0;
                } else if (v > highestValue[n] && i <= line.length() + n - 12) {
                    highestValue[n] = v;
                    overrun = true;
                }
            }
        }
        return Long.parseLong(Arrays.stream(highestValue).mapToObj(String::valueOf).collect(joining()));
    }
}

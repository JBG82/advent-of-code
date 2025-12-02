/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

/**
 * https://adventofcode.com/2025/day/2
 */
public class Day2 {

    public static void main(String[] args) {
        String input = InputResolver.fetchLinesFromInputFile("input_day2.txt", Day2.class).getFirst();
        String[] ranges = input.split(",");

        // Example:  1227775554 (Part 1)
        // Example:  4174379265 (Part 2)
        // Result 1: 21139440284
        // Result 2: 38731915928
        System.out.println(solvePart2(ranges));
    }

    private static long solvePart1(final String[] ranges) {
        long result = 0;
        for (String range : ranges) {
            String[] split = range.split("-");
            long start = Long.parseLong(split[0]);
            long end = Long.parseLong(split[1]);
            long i = start - 1;
            while (++i <= end) {
                String c = String.valueOf(i);
                int len = c.length();
                if (len % 2 == 0) {
                    if (c.substring(0, len/2).equals(c.substring(len/2))) {
                        result += i;
                    }
                }
            }
        }
        return result;
    }

    private static long solvePart2(final String[] ranges) {
        long result = 0;
        for (String range : ranges) {
            String[] split = range.split("-");
            long start = Long.parseLong(split[0]);
            long end = Long.parseLong(split[1]);
            long i = start - 1;
            while (++i <= end) {
                String c = String.valueOf(i);
                int len = c.length();
                int patternLen = len / 2;
                if (patternLen == 0) continue;
                do {
                    if (len % patternLen == 0) {
                        int pos = 0;
                        String pattern = c.substring(0, patternLen);
                        boolean allTheSame = true;
                        while ((pos += patternLen) < len) {
                            if (!pattern.equals(c.substring(pos, pos + patternLen))) {
                                allTheSame = false;
                            }
                        }
                        if (allTheSame) {
                            result += i;
                            break;
                        }
                    }
                } while (--patternLen > 0);
            }
        }
        return result;
    }
}

/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * https://adventofcode.com/2025/day/5
 */
public class Day5 {
    public static void main(String[] args) {
        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day5.txt");

        // Part 1
        List<Range> ranges = new ArrayList<>();
        long result = 0;
        for (String line : lines) {
            if (line.contains("-")) {
                String[] split = line.split("-");
                ranges.add(new Range(Long.parseLong(split[0]), Long.parseLong(split[1])));
            } else {
                if (line.isEmpty()) continue;

                long value = Long.parseLong(line);
                for (Range range : ranges) {
                    if (range.contains(value)) {
                        ++result;
                        break;
                    }
                }
            }
        }
        System.out.println("Result Part 1: " + result);

        // Part 2
        ranges.sort(Comparator.comparingLong(Range::begin));
        result = 0;
        long current = 0;
        for (Range range : ranges) {
            if (current < range.begin) {
                // IDs aus der Range komplett übernehmen
                result += range.size();
                current = range.end;
            } else if (current <= range.end) {
                // IDs teilweise übernehmen
                result += range.size() - current + range.begin - 1;
                current = range.end;
            }
        }

        // Part 1: 511
        // Part 2: 350939902751909
        System.out.println("Result Part 2: " + result);
    }

    record Range(long begin, long end) {
        boolean contains(long value) {
            return value >= begin && value <= end;
        }

        long size() {
            return end - begin + 1;
        }
    }
}

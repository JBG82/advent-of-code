/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2023/day/11
 */
public class Day11 {

    // Puzzle 1
//    private static final int MULTIPLY_EMPTY_STRAIGHTS_WITH = 2;

    // Puzzle 2
    private static final int MULTIPLY_EMPTY_STRAIGHTS_WITH = 1_000_000;

    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day11.txt", Day11.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day11_example.txt", Day11.class);

        List<Pos> galaxies = new ArrayList<>();
        BitSet emptyLineSet = new BitSet(140);
        BitSet emptyColumnSet = new BitSet(140);
        emptyColumnSet.set(0, input.getFirst().length(), true);
        for (int y = 0; y < input.size(); ++y) {
            String line = input.get(y);
            char[] charArray = line.toCharArray();
            boolean emptyLine = true;
            for (int x = 0; x < line.length(); ++x) {
                if (charArray[x] == '#') {
                    emptyColumnSet.set(x, false);
                    galaxies.add(new Pos(x, y));
                    emptyLine = false;
                }
            }
            emptyLineSet.set(y, emptyLine);
        }

        System.out.println("Got " + galaxies.size() + " galaxies");

        List<Integer> emptyLines = new ArrayList<>();
        for (int i = 0; i < emptyLineSet.length(); ++i) {
            if (emptyLineSet.get(i)) {
                emptyLines.add(i);
            }
        }
        System.out.println("Got " + emptyLineSet.cardinality() + " empty lines: " + emptyLines.stream().map(String::valueOf).collect(Collectors.joining(", ")));

        List<Integer> emptyColumns = new ArrayList<>();
        for (int i = 0; i < emptyColumnSet.length(); ++i) {
            if (emptyColumnSet.get(i)) {
                emptyColumns.add(i);
            }
        }
        System.out.println("Got " + emptyColumnSet.cardinality() + " empty columns: " + emptyColumns.stream().map(String::valueOf).collect(Collectors.joining(", ")));


//        List<Pos> list = galaxies.stream().sorted(Comparator.comparingInt(Pos::getY)).toList();
        for (Integer emptyLine : emptyLines.reversed()) {
            galaxies.stream().filter(g -> g.y >= emptyLine).forEach(Pos::expandLine);
        }
//        list = galaxies.stream().sorted(Comparator.comparingInt(Pos::getY)).toList();
        for (Integer emptyCol : emptyColumns.reversed()) {
            galaxies.stream().filter(g -> g.x >= emptyCol).forEach(Pos::expandColumn);
        }

        List<Distance> distances = new ArrayList<>();
        for (int i = 0; i < galaxies.size(); ++i) {
            for (int j = i + 1; j < galaxies.size(); ++j) {
                Pos p1 = galaxies.get(i);
                Pos p2 = galaxies.get(j);
                distances.add(Distance.between(p1, p2));
            }
        }
        System.out.println("Got " + distances.size() + " distances");

        // Result 1: 9647174
        // Result 2: 377318892554
        long result1 = distances.stream().mapToLong(Distance::value).sum();
        System.out.println("Result: " + result1);

    }

    record Distance(Pos p1, Pos p2, long value) {
        static Distance between(final Pos p1, final Pos p2) {
            int dist = Math.max(p1.x, p2.x) - Math.min(p1.x, p2.x) + Math.max(p1.y, p2.y) - Math.min(p1.y, p2.y);
            return new Distance(p1, p2, dist);
        }
    }

    @Data
    @AllArgsConstructor
    static class Pos {
        private int x, y;

        void expandLine() {
            y += MULTIPLY_EMPTY_STRAIGHTS_WITH - 1;
        }

        void expandColumn() {
            x += MULTIPLY_EMPTY_STRAIGHTS_WITH - 1;
        }
    }

}

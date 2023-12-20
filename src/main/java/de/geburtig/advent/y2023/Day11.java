/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/11
 */
public class Day11 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day11.txt", Day11.class);

        List<Pos> galaxies = new ArrayList<>();
        BitSet emptyLines = new BitSet(140);
        BitSet emptyColumns = new BitSet(140);
        emptyColumns.set(0, input.getFirst().length(), true);
        for (int y = 0; y < input.size(); ++y) {
            String line = input.get(y);
            char[] charArray = line.toCharArray();
            boolean emptyLine = true;
            for (int x = 0; x < line.length(); ++x) {
                if (charArray[x] == '#') {
                    emptyColumns.set(x, false);
                    galaxies.add(new Pos(x, y));
                    emptyLine = false;
                }
            }
            emptyLines.set(y, emptyLine);
        }

        System.out.println("Got " + galaxies.size() + " galaxies");
        System.out.println("Got " + emptyColumns.cardinality() + " empty columns");
        System.out.println("Got " + emptyLines.cardinality() + " empty lines");
    }

    record Pos(int x, int y) {}
}

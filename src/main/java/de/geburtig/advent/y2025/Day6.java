/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * https://adventofcode.com/2025/day/6
 */
public class Day6 {

    public static void main(String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day6.txt");

        // Part 1: 4693419406682
        System.out.println(solvePart1(input));

        // Part 2: 9029931401920
        System.out.println(solvePart2(input));
    }

    private static long solvePart1(List<String> input) {
        List<Character> operators = new ArrayList<>();
        for (char c : input.getLast().toCharArray()) {
            if (c != ' ') operators.add(c);
        }

        List<List<Integer>> values = new ArrayList<>();
        for (String line : input.subList(0, input.size() - 1)) {
            List<Integer> row = new ArrayList<>();
            StringBuilder b = new StringBuilder();
            for (char c : line.toCharArray()) {
                if (c == ' ') {
                    if (!b.isEmpty()) {
                        row.add(Integer.parseInt(b.toString()));
                        b.setLength(0);
                    }
                } else {
                    b.append(c);
                }
            }
            row.add(Integer.parseInt(b.toString()));
            values.add(row);
        }

        long result = 0;
        for (int i = 0; i < operators.size(); i++) {
            long partResult = values.getFirst().get(i);
            for (int c = 1; c < values.size(); ++c) {
                if (operators.get(i) == '+') {
                    partResult += values.get(c).get(i);
                } else {
                    partResult *= values.get(c).get(i);
                }
            }
            result += partResult;
        }
        return result;
    }

    private static long solvePart2(final List<String> input) {
        List<char[]> charInput = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            charInput.add(input.get(i).toCharArray());
        }

        long result = 0;
        List<Integer> numbers = new ArrayList<>();
        int idx = input.stream().mapToInt(String::length).max().orElseThrow();
        while (idx-- > 0) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < input.size(); i++) {
                if (charInput.get(i).length > idx) {
                    char c = charInput.get(i)[idx];
                    if (Character.isDigit(c)) {
                        b.append(c);
                    } else if (c != ' ') {
                        long partResult = Integer.parseInt(b.toString());
                        for (Integer x : numbers) {
                            if (c == '+') {
                                partResult += x;
                            } else {
                                partResult *= x;
                            }
                        }
                        result += partResult;
                        b.setLength(0);
                        numbers.clear();
                        idx--;
                    }
                }
            }
            if (!b.isEmpty()) {
                numbers.add(Integer.parseInt(b.toString()));
            }
        }
        return result;
    }
}

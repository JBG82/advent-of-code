/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.NumericPermutations;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://adventofcode.com/2023/day/12
 */
public class Day12 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day12_example.txt", Day12.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day12.txt", Day12.class);

        List<ConditionRecord> records = input.stream().map(ConditionRecord::new).toList();
        int result1 = records.stream().mapToInt(r -> r.possibleCombinations().size()).sum();
        // 7173
        System.out.println("Result 1: " + result1);

        List<ConditionRecord> records2 = input.stream().map(Day12::unfold).map(ConditionRecord::new).toList();
        System.out.println(NumericPermutations.PERMUTATIONS.size() + " " + String.join(";", NumericPermutations.PERMUTATIONS.keySet()));
        records2.get(0).possibleCombinations().forEach(System.out::println);
        System.out.println(NumericPermutations.PERMUTATIONS.size() + " " + String.join(";", NumericPermutations.PERMUTATIONS.keySet()));

//        int result2 = records2.stream().mapToInt(r -> r.possibleCombinations().size()).sum();
//        System.out.println("Result 2: " + result2);

        System.out.println(NumericPermutations.of(15,12).size());
    }

    static String unfold(final String line) {
        String[] split = line.split(" ");
        String part1 = IntStream.range(0, 5).mapToObj(v -> split[0]).collect(Collectors.joining("?"));
        String part2 = IntStream.range(0, 5).mapToObj(v -> split[1]).collect(Collectors.joining(","));
        return part1 + " " + part2;
    }

    @Data
    static class ConditionRecord {
        final String input;
        final List<Integer> groups;
        final int springs;

        final Pattern pattern;

        ConditionRecord(final String line) {
            String[] split = line.split(" ");
            this.input = split[0];
            this.groups = Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).boxed().toList();
            this.springs = groups.stream().mapToInt(v -> v).sum();
            pattern = Pattern.compile(input.replace(".", "\\.").replace("?", "."));
        }

        int size() {
            return groups.size();
        }

        int length() {
            return input.length();
        }

        int spaces() {
            return length() - springs;
        }

        int freeSpaces() {
            return spaces() - size() + 1;
        }

        @Override
        public String toString() {
            return input + " " + groups.stream().map(String::valueOf).collect(Collectors.joining(",")) + " length=" +
                    length() + " spaces=" + spaces() + " freeSpaces=" + freeSpaces();
        }

        List<String> possibleCombinations() {
            ArrayList<String> result = new ArrayList<>();
            List<List<Integer>> combinations = NumericPermutations.of(freeSpaces(), groups.size() + 1);
            for (List<Integer> spaces : combinations) {
                String combination = "";
                for (int i = 0; i < groups.size(); ++i) {
                    int space = spaces.get(i) + (i == 0 ? 0 : 1);
                    combination += ".".repeat(space) + "#".repeat(groups.get(i));
                }
                combination += ".".repeat(spaces.getLast());
                if (fits(combination)) {
                    result.add(combination);
                }
            }
            return result;
        }

        private boolean fits(final String combination) {
            return pattern.matcher(combination).matches();
        }
    }
}

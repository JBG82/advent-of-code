/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;

import java.util.Arrays;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/6
 */
public class Day6 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day6.txt", Day6.class);

        List<Integer> times = Arrays.stream(input.getFirst().split("[ ]+")).filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        times.forEach(System.out::println);
    }
}

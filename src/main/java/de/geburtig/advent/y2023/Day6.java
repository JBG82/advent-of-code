/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2023/day/6
 */
public class Day6 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day6.txt", Day6.class);

        // Puzzle 1:
        List<Integer> times = Arrays.stream(input.getFirst().split("[ ]+")).filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();
        List<Integer> distances = Arrays.stream(input.getLast().split("[ ]+")).filter(s -> s.matches("\\d+")).map(Integer::parseInt).toList();

        int result = 1;
        for (int race = 0; race < times.size(); ++race) {
            int time = times.get(race);
            int minDistance = distances.get(race);

            int choices = 0;
            for (int i = 1; i < time; ++i) {
                int distance = i * (time - i);
                if (distance > minDistance) {
                    ++choices;
                }
            }
            result *= choices;
        }
        System.out.println("Result 1: " + result);

        // Puzzle 2
        long time = Long.parseLong(Arrays.stream(input.getFirst().split("[ ]+")).filter(s -> s.matches("\\d+")).collect(Collectors.joining()));
        long minDistance = Long.parseLong(Arrays.stream(input.getLast().split("[ ]+")).filter(s -> s.matches("\\d+")).collect(Collectors.joining()));
        int choices = 0;
        for (long i = 1; i < time; ++i) {
            long distance = i * (time - i);
            if (distance > minDistance) {
                ++choices;
            } else if (choices > 0) {
                break;
            }
        }
        System.out.println("Result 2: " + choices);
    }
}

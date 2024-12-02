/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.groupingBy;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day1 {

    public static void main(final String[] args) {
//        long result = solve1("input_day1_example.txt");
//        long result = solve1("input_day1.txt");
//        long result = solve2("input_day1_example.txt");
        long result = solve2("input_day1.txt");
        System.out.println(result);
    }

    private static long solve1(final String inputFileName) {
        Input input = readInputFrom(inputFileName);
        input.list1().sort(String::compareTo);
        input.list2().sort(String::compareTo);

        long dist = 0;
        for (int i = 0; i < input.list1().size(); i++) {
            dist += Math.abs(parseLong(input.list1().get(i)) - parseLong(input.list2().get(i)));
        }
        return dist;
    }

    private static long solve2(final String inputFileName) {
        Input input = readInputFrom(inputFileName);

        Map<String, Long> occurenceMap = input.list2().stream().collect(groupingBy(i -> i, Collectors.counting()));

        long score = 0;
        for (int i = 0; i < input.list1().size(); i++) {
            String key = input.list1().get(i);
            score += occurenceMap.getOrDefault(key, 0L) * parseLong(key);
        }
        return score;
    }

    private static Input readInputFrom(final String fileName) {
        List<String> input = InputResolver.fetchLinesFromInputFile(fileName, Day1.class);

        ArrayList<String> list1 = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        input.forEach(line -> {
            String[] split = line.split(" ");
            list1.add(split[0]);
            list2.add(split[split.length - 1]);
        });
        return new Input(list1, list2);
    }

    record Input(List<String> list1, List<String> list2) {}
}

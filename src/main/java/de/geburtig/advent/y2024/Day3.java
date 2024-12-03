/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day3 {
    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day3_example.txt", Day3.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day3_example2.txt", Day3.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day3.txt", Day3.class);
        System.out.println(solve2(input));
    }

    private static long solve1(final List<String> input) {
        Pattern pattern = Pattern.compile("(mul\\((\\d+),(\\d+)\\))");
        long result = 0;
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                result += parseLong(matcher.group(2)) * parseLong(matcher.group(3));
            }
        }
        return result;
    }

    private static long solve2(final List<String> input) {
        Pattern pattern = Pattern.compile("(mul\\((\\d+),(\\d+)\\))|(do\\(\\))|(don't\\(\\))");
        long result = 0;
        boolean active = true;
        for (String line : input) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String command = matcher.group();
                if (command.equals("do()")) {
                    active = true;
                } else if (command.equals("don't()")) {
                    active = false;
                } else if (active) {
                    result += parseLong(matcher.group(2)) * parseLong(matcher.group(3));
                }
            }
        }
        return result;
    }
}

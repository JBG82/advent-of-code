/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.base.DayBase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day1 extends DayBase {
    @Override
    public String resolvePuzzle1() throws Exception {
        List<String> lines = getLinesFromInputFile("/de/geburtig/advent/y2023/input_day1.txt");

        Pattern pattern = Pattern.compile("\\d(.*\\d)?");
        int result = 0;
        for (String input : lines) {
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) throw new RuntimeException("Not matching: " + input);

            String group = matcher.group();
            int v1 = Integer.parseInt(group.substring(0, 1));
            int v2 = Integer.parseInt(group.substring(group.length() - 1));
            int res = v1 * 10 + v2;
            result += res;

//            System.out.println(input + " -> " + group + " -> " + res);
        }
        return result + "";
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        List<String> lines = getLinesFromInputFile("/de/geburtig/advent/y2023/input_day1.txt");
//        List<String> lines = getLinesFromInputFile("/y2023/input_day1_example2.txt");

        Pattern pattern = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");

        int result = 0;
        for (String input : lines) {
            Matcher matcher = pattern.matcher(input);
            matcher.find();
            String first = matcher.group();

            String last = first;
            int nextStart = 0;
            while (matcher.find(nextStart)) {
                last = matcher.group();
                nextStart = matcher.start() + 1;
            }

            int v1 = toInt(first);
            int v2 = toInt(last);
            int res = v1 * 10 + v2;
            result += res;

//            System.out.println(input + " -> " + first + ", " + last);
        }
        return String.valueOf(result);
    }

    private int toInt(final String value) {
        if (value.length() > 1) {
            String v = value
                    .replace("nine", "9")
                    .replace("eight", "8")
                    .replace("seven", "7")
                    .replace("six", "6")
                    .replace("five", "5")
                    .replace("four", "4")
                    .replace("three", "3")
                    .replace("two", "2")
                    .replace("one", "1");
            return Integer.parseInt(v);
        }
        return Integer.parseInt(value);
    }

    public static void main(final String[] args) throws Exception {
        System.out.println("Result of puzzle 1: " + new Day1().resolvePuzzle1());
        System.out.println("Result of puzzle 2: " + new Day1().resolvePuzzle2());
    }
}

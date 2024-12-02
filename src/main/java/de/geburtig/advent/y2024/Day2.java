/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day2 {

    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day2_example.txt", Day2.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day2.txt", Day2.class);
        System.out.println(solve2(input));
    }

    private static long solve1(final List<String> input) {
        long result = 0;
        for (String line : input) {
            List<Integer> levels = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            boolean increasing = levels.get(1) > levels.get(0);
            boolean safe = true;
            for (int i = 1; i < levels.size(); i++) {
                boolean rightDirection = levels.get(i) > levels.get(i - 1) == increasing;
                int diff = Math.abs(levels.get(i) - levels.get(i - 1));
                if (!rightDirection || diff < 1 || diff > 3) {
                    safe = false;
                    break;
                }
            }
            result += safe ? 1 : 0;
        }
        return result;
    }

    private static long solve2(final List<String> input) {
        long result = 0;
        for (String line : input) {
            List<Integer> levelsOrig = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();

            List<Integer> levels = levelsOrig;
            int possibiliesLeft = levels.size();
            boolean safe;
            do {
                boolean increasing = levels.get(1) > levels.get(0);
                safe = true;
                for (int i = 1; i < levels.size(); i++) {
                    boolean rightDirection = levels.get(i) > levels.get(i - 1) == increasing;
                    int diff = Math.abs(levels.get(i) - levels.get(i - 1));
                    if (!rightDirection || diff < 1 || diff > 3) {
                        safe = false;
                        break;
                    }
                }
                if (!safe && possibiliesLeft > 0) {
                    levels = new ArrayList<>(levelsOrig);
                    levels.remove(possibiliesLeft - 1);
                }
            } while (!safe && possibiliesLeft-- > 0);
            result += safe ? 1 : 0;
        }
        return result;
    }
}

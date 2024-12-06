/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day4 {

    // Hinweis: "?=" ist der "Lookahead"-Ausdruck, der nach Mustern sucht, ohne den Suchindex zu verschieben, was überlappende Matches ermöglicht
    private final static Pattern PATTERN = Pattern.compile("(?=XMAS)|(?=SAMX)");
    private final static Pattern PATTERN_2 = Pattern.compile("(?=MAS)|(?=SAM)");

    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day4.txt", Day4.class);

        // 2567
        System.out.println("Result 1: " + solve1(input));
        // 2029
        System.out.println("Result 2: " + solve2(input));
    }

    private static long solve1(final List<String> input) {
        long result = 0;
        for (String line : input) result += count(line);
        for (String line : toVerticalInput(input)) result += count(line);
        for (String line : toDiagonal1(input)) result += count(line);
        for (String line : toDiagonal2(input)) result += count(line);
        return result;
    }

    private static long solve2(final List<String> input) {
        int height = input.size();
        Set<Pos> candidates = new HashSet<>();

        List<String> list = toDiagonal1(input);
        for (int y = 0; y < list.size(); y++) {
            String line = list.get(y);
            Matcher matcher = PATTERN_2.matcher(line);
            while (matcher.find()) {
                int x = matcher.start() + 1;
                Pos cand = new Pos(x, y).fromDiagonal1(height);
                candidates.add(cand);
            }
        }
        Set<Pos> hits = new HashSet<>();
        list = toDiagonal2(input);
        for (int y = 0; y < list.size(); y++) {
            String line = list.get(y);
            Matcher matcher = PATTERN_2.matcher(line);
            while (matcher.find()) {
                int x = matcher.start() + 1;
                Pos cand = new Pos(x, y).fromDiagonal2(height);
                if (candidates.contains(cand)) {
                    hits.add(cand);
                }
            }
        }
        return hits.size();
    }

    private static long count(final String line) {
        int result = 0;
        Matcher matcher = PATTERN.matcher(line);
        while (matcher.find()) ++result;
        return result;
    }

    private static List<String> toVerticalInput(final List<String> input) {
        List<String> output = new ArrayList<>(input.getFirst().length());
        for (int i = 0; i < input.getFirst().length(); ++i) {
            output.add("");
        }

        for (int i = 0; i < input.getFirst().length(); ++i) {
            for (String line : input) {
                char c = line.charAt(i);
                output.set(i, output.get(i) + c);
            }
        }
        return output;
    }

    private static List<String> toDiagonal1(final List<String> input) {
        int height = input.size();
        int width = input.getFirst().length();
        List<String> output = new ArrayList<>(height + width - 1);
        for (int i = 0; i < height + width - 1; ++i) {
            output.add("");
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                char c = input.get(y).charAt(x);
                int y1 = height - y - 1 + x; // =Diagonale
                output.set(y1, output.get(y1) + c);
            }
        }
        return output;
    }

    private static List<String> toDiagonal2(final List<String> input) {
        int height = input.size();
        int width = input.getFirst().length();
        List<String> output = new ArrayList<>(height + width - 1);
        for (int i = 0; i < height + width - 1; ++i) {
            output.add("");
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                char c = input.get(y).charAt(x);
                int y1 = x + y; // =Diagonale
                output.set(y1, output.get(y1) + c);
            }
        }
        return output;
    }

    /**
     * Konvertiert die Koordinaten aus dem originären Input zum diagonalen Input oder umgekehrt.
     */
    record Pos(int x, int y) {
        Pos toDiagonal1(final int height) {
            int x1 = Math.min(x, y);
            int y1 = x + height - 1 - y;
            return new Pos(x1, y1);
        }

        Pos fromDiagonal1(final int height) {
            int x0 = x + Math.max(0, y - (height - 1));
            int y0 = x + Math.max(0, height - 1 - y);
            return new Pos(x0, y0);
        }

        Pos toDiagonal2(final int height) {
            int x1 = Math.min(x, height - 1 - y);
            int y1 = x + y;
            return new Pos(x1, y1);
        }

        Pos fromDiagonal2(final int height) {
            int x0 = x + Math.max(0, y - (height - 1));
            int y0 = y - x0;
            return new Pos(x0, y0);
        }
    }
}

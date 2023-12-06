package de.geburtig.advent.y2021;

import de.geburtig.advent.util.InputResolver;

import java.util.List;

import static java.lang.Integer.parseInt;

public class Day2 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day2.txt", Day2.class);

        // Puzzle 1
        long x = 0, y = 0;
        for (String line : input) {
            if (line.startsWith("forward ")) {
                x += parseInt(line.substring(8));
            } else if (line.startsWith("down ")) {
                y += parseInt(line.substring(5));
            } else if (line.startsWith("up ")) {
                y -= parseInt(line.substring(3));
            }
        }
        System.out.println("Result 1: " + (x * y));

        // Puzzle 2
        long aim = 0;
        x = 0;
        y = 0;
        for (String line : input) {
            if (line.startsWith("forward ")) {
                int value = parseInt(line.substring(8));
                x += value;
                y += aim * value;
            } else if (line.startsWith("down ")) {
                aim += parseInt(line.substring(5));
            } else if (line.startsWith("up ")) {
                aim -= parseInt(line.substring(3));
            }
        }
        System.out.println("Result 2: " + (x * y));
    }
}

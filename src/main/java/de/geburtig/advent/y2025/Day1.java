package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

import java.util.List;

public class Day1 {

    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day1.txt", Day1.class);

        int pointer = 50, counterPart1 = 0, counterPart2 = 0;

        for (String value : input) {
            int distance = Integer.parseInt(value.substring(1));

            if (value.charAt(0) == 'R') {
                pointer += distance;
                while (pointer > 99) {
                    pointer -= 100;
                    ++counterPart2;
                }
            } else {
                if (pointer == 0) --counterPart2;
                pointer -= distance;
                while (pointer < 0) {
                    pointer += 100;
                    ++counterPart2;
                }
                if (pointer == 0) ++counterPart2;
            }
            if (pointer == 0) ++counterPart1;
        }

        // Part 1: 1100
        // Part 2: 6358
        System.out.println(counterPart1);
        System.out.println(counterPart2);
    }
}

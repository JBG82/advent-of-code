package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class Day11 {

    public static void main(final String[] args) throws Exception {
        String input = InputResolver.fetchLinesFromInputFile("input_day11.txt").getFirst();
        List<Long> stones = new ArrayList<>(Arrays.stream(input.split(" ")).mapToLong(Long::parseLong).boxed().toList());

        // Part 1: 212655
        System.out.println("Part 1: " + performBlinks(stones, 25).size());
        // Part 2: 253582809724830
        long result = solvePart2(stones);
        System.out.println("Part 2: " + result);
    }

    static List<Long> performBlinks(List<Long> stones, int times) {
        List<Long> result = stones;
        for (int i = 0; i < times; i++) {
            result = performBlink(result);
        }
        return result;
    }

    static List<Long> performBlink(List<Long> stones) {
        List<Long> result = new ArrayList<>();
        for (Long stone : stones) {
            if (stone == 0) {
                result.add(1L);
            } else {
                String str = String.valueOf(stone);
                if (str.length() % 2 == 0) {
                    result.add(Long.parseLong(str.substring(0, str.length() / 2)));
                    result.add(Long.parseLong(str.substring(str.length() / 2)));
                } else {
                    result.add(stone * 2024);
                }
            }
        }
        return result;
    }

    private static long solvePart2(final List<Long> stones) throws URISyntaxException, IOException {
        // Perform 40 blinks from the start
        List<Long> stonesAfter40Blinks = performBlinks(stones, 40);

        // After that I cheated a little here: I apply the results from the helper file, in which I performed another
        // 35 blink iterations for each unique stone number. I apply these auxiliary numbers to each stone and get
        // the result...
        Path helperFilePath = Path.of(Day11.class.getResource("input_day11_helper.txt").toURI());
        Map<Long, Long> after35BlinksMap = Files.lines(helperFilePath)
                .skip(1)
                .map(line -> line.split(" -> "))
                .collect(Collectors.toMap(s -> Long.parseLong(s[0]), s -> Long.parseLong(s[1])));

        after35BlinksMap.forEach((key, value) -> System.out.println(key + " -> " + value));

        long result = 0;
        for (Long stone : stonesAfter40Blinks) {
            result += after35BlinksMap.get(stone);
        }
        return result;
    }
}

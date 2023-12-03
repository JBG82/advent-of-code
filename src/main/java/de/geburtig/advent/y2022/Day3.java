package de.geburtig.advent.y2022;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day3 {

    public int resolvePuzzle1() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day3.txt").toURI());

        AtomicInteger sum = new AtomicInteger(0);
        for (String line : Files.lines(resource).toList()) {
            String part1 = line.substring(0, line.length()/2);
            String part2 = line.substring(line.length()/2);
            part1.chars().distinct().forEach(i -> {
                if (part2.contains(String.valueOf((char)i))) {
                    sum.addAndGet(valueOf(i));
                }
            });
        }
        return sum.get();
    }

    public int resolvePuzzle2() throws Exception {
        Path resource = Paths.get(getClass().getResource("/y2022/input_day3.txt").toURI());

        List<String> lines = Files.readAllLines(resource);
        AtomicInteger sum = new AtomicInteger(0);
        int count = 0;
        while (count < lines.size()) {
            String first = lines.get(count++);
            String second = lines.get(count++);
            String third = lines.get(count++);
            first.chars().distinct().forEach(i -> {
                if (second.contains(toChar(i)) && third.contains(toChar(i))) {
                    sum.addAndGet(valueOf(i));
                }
            });
        }
        return sum.get();
    }

    private String toChar(int i) {
        return String.valueOf((char)i);
    }

    private int valueOf(int c) {
        return c > 90 ? c - 96 : c - 38;
    }
}

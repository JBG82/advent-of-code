package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;

import java.util.*;

/**
 * https://adventofcode.com/2025/day/7
 */
public class Day7 {

    public static void main(final String[] args) {
        char[][] input = InputResolver.fetchInputAsCharArray("input_day7.txt");

        Pos start = null;
        outer:
        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input[x].length; y++) {
                if (input[x][y] == 'S') {
                    start = new Pos(x, y);
                    break outer;
                }
            }
        }
        Objects.requireNonNull(start);

        // Part 1: 1562
        System.out.println("Part 1: " + solvePart1(input, start));
        // Part 2: 24292631346665
        System.out.println("Part 2: " + solvePart2(input, start));
    }

    private static int solvePart1(final char[][] input, final Pos start) {
        Set<Integer> rays = Set.of(start.x);
        int y = start.y;
        int splitCount = 0;
        while (++y < input.length) {
            Set<Integer> newRays = new HashSet<>();
            for (Integer x : rays) {
                if (input[x][y] == '^') {
                    ++splitCount;
                    newRays.add(x - 1);
                    newRays.add(x + 1);
                } else {
                    newRays.add(x);
                }
            }
            rays = newRays;
        }
        return splitCount;
    }

    private static long solvePart2(final char[][] input, final Pos start) {
        int y = start.y;
        Map<Integer, Long> rayMap = new HashMap<>();
        rayMap.put(start.x, 1L);
        while (++y < input.length) {
            Map<Integer, Long> newRayMap = new HashMap<>();
            for (Integer x : rayMap.keySet()) {
                long rayCount = rayMap.get(x);
                if (input[x][y] == '^') {
                    long current = newRayMap.computeIfAbsent(x - 1, i -> 0L);
                    newRayMap.put(x - 1, current + rayCount);
                    current = newRayMap.computeIfAbsent(x + 1, i -> 0L);
                    newRayMap.put(x + 1, current + rayCount);
                } else {
                    long current = newRayMap.computeIfAbsent(x, i -> 0L);
                    newRayMap.put(x, current + rayCount);
                }
            }
            rayMap = newRayMap;
        }
        return rayMap.values().stream().mapToLong(i -> i).sum();
    }

    record Pos(int x, int y) {}
}

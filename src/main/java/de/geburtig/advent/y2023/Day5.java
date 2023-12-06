/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * https://adventofcode.com/2023/day/5
 */
public class Day5 {
    public static void main(final String[] args) {
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day5_example.txt", Day5.class);
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day5.txt", Day5.class);

        // Puzzle 1:
        List<Long> seeds = null;
        List<Map> maps = new ArrayList<>();
        for (String line : input) {
            if (line.startsWith("seeds: ")) {
                seeds = Arrays.stream(line.substring(7).split(" ")).map(Long::parseLong).toList();
            } else if (line.endsWith(" map:")) {
                String mapName = line.substring(0, line.indexOf(" map:"));
                maps.add(new Map(mapName));
            } else if (!line.isEmpty()) {
                String[] split = line.split(" ");
                maps.getLast().sections.add(new Section(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2])));
            }
        }

        List<Long> resolvedLocations = new ArrayList<>();
        for (Long seed : seeds) {
            System.out.print(seed);
            Long current = seed;
            for (Map map : maps) {
                current = map.resolve(current);
                System.out.print(" -> " + current);
            }
            System.out.println();
            resolvedLocations.add(current);
        }

        long result = resolvedLocations.stream().mapToLong(l -> l).min().orElseThrow();
        System.out.println("Result 1: " + result);

        // Puzzle 2:
        HashMap<Long, Long> seedRanges = new HashMap<>();
        for (int i = 0; i < seeds.size(); i += 2) {
            seedRanges.put(seeds.get(i), seeds.get(i + 1));
        }

        // Min seed:    93.533.455
        // Max seed: 4.064.626.836
        seedRanges.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(e -> {
            System.out.println(e.getKey() + " to " + (e.getKey() + e.getValue() - 1));
        });

//          Ranges:
//          1.)         93.533.455 to   222.103.137 = 128.569.682 (lowest location: 1.081.323.768)
//          2.)        515.785.082 to   603.690.120 =  87.905.038 (lowest location:   429.431.694)
//          3.)        720.333.403 to 1.105.567.595 = 385.234.192 (lowest location:   186.829.220)
//          4.)      1.357.904.101 to 1.641.290.267 = 283.386.166 (not lower then range 3)
//          5.)      2.104.518.691 to 2.607.668.533 = 503.149.842 (lowest location:    41.222.968)
//          6.)      2.655.687.716 to 2.664.091.132 =   8.403.416 (not lower then range 5)
//          7.)      2.844.655.470 to 2.869.650.098 =  24.994.628 (not lower then range 5)
//          8.)      3.120.497.449 to 3.228.254.329 = 107.756.880 (not lower then range 5)
//          9.)      3.934.515.023 to 4.001.842.840 =  67.327.817 (not lower then range 5)
//         10.)      4.055.128.129 to 4.064.626.836 =   9.498.707 (not lower then range 5)

        AtomicLong lowestLocation = new AtomicLong(Long.MAX_VALUE);
        AtomicInteger range = new AtomicInteger();
        seedRanges.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(e -> {
            long start = e.getKey();
            long end = e.getKey() + e.getValue();
            System.out.println("Processing range " + range.incrementAndGet() + " from " + start + " to " + end + "...");

            for (long seed = start; seed < end; ++seed) {
                long current = seed;
                for (Map map : maps) {
                    current = map.resolve(current);
                }
                if (lowestLocation.get() > current) {
                    lowestLocation.set(current);
                }
            }
            System.out.println("Done with range " + range.get() + ". Lowest result is " + lowestLocation);
        });

        // Fazit: Funktioniert so, dauert aber recht lange. Das geht bestimmt effizienter!
    }

    @Data
    static class Map {
        private @NonNull String name;
        private List<Section> sections = new ArrayList<>();

        public Long resolve(final Long value) {
            Optional<Section> any = sections.stream().filter(s -> value >= s.sourceStart && value < s.sourceStart + s.range).findAny();
            return any.map(section -> section.destStart + value - section.sourceStart).orElse(value);
        }
    }

    record Section(long destStart, long sourceStart, long range) {

    }
}

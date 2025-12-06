/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.Map2D;
import de.geburtig.advent.util.Map2D.Direction;
import de.geburtig.advent.util.Map2D.Field;
import de.geburtig.advent.util.Map2D.Pos;
import lombok.Data;

import java.util.*;

import static de.geburtig.advent.util.Map2D.Direction.*;

/**
 * https://adventofcode.com/2024/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        Map2D<Integer> map = InputResolver.fetchInputAsIntMap("input_day10.txt");

        List<Pos> trailheads = new ArrayList<>();
        List<Pos> peaks = new ArrayList<>();
        map.forEach((pos, value) -> {
            if (value == 0) trailheads.add(pos);
                else if (value == 9) peaks.add(pos);
        });
        System.out.println("Got " + trailheads.size() + " trailheads and " + peaks.size() + " peaks");

        ScanResult result = solve(trailheads, map);
        // Part 1: 841
        System.out.println("Part 1: " + result.reachedPeaks());
        // Part 2: 1875
        System.out.println("Part 2: " + result.distinctPathways());
    }

    private static ScanResult solve(List<Pos> trailheads, Map2D<Integer> map) {
        int reachedPeaks = 0, distinctPaths = 0;
        for (Pos pos : trailheads) {
            Field<Integer> trailhead = map.getField(pos);
            Map2D<Boolean> beenThereMap = new Map2D<>(map.getWidth(), map.getHeight(), false);
            ScanResult scanResult = scanTrailways(trailhead, beenThereMap);
            reachedPeaks += scanResult.reachedPeaks();
            distinctPaths += scanResult.distinctPathways;
        }
        return new ScanResult(reachedPeaks, distinctPaths);
    }

    static ScanResult scanTrailways(final Field<Integer> field, Map2D<Boolean> beenThereMap) {
        Set<Pos> reachedPeaks = new HashSet<>();
        Set<Path> fullPaths = new HashSet<>();

        Path path = new Path(field.getPos());
        wander(field, beenThereMap, reachedPeaks, path, fullPaths);

        return new ScanResult(reachedPeaks.size(), fullPaths.size());
    }

    private static void wander(Field<Integer> field, Map2D<Boolean> beenThereMap, Set<Pos> reachedPeaks, Path path, Set<Path> distinctPaths) {
        beenThereMap.set(field.getPos(), true);
        if (field.getValue() == 9) {
            reachedPeaks.add(field.getPos());
            distinctPaths.add(path);
        } else {
            tryAndGo(north, field, beenThereMap, reachedPeaks, path, distinctPaths);
            tryAndGo(east, field, beenThereMap, reachedPeaks, path, distinctPaths);
            tryAndGo(south, field, beenThereMap, reachedPeaks, path, distinctPaths);
            tryAndGo(west, field, beenThereMap, reachedPeaks, path, distinctPaths);
        }
    }

    private static void tryAndGo(final Direction direction, final Field<Integer> field, Map2D<Boolean> beenThereMap, Set<Pos> reachedPeaks, Path path, Set<Path> distinctPaths) {
        Optional<Field<Integer>> adjacent = field.getAdjacent(direction);
        if (adjacent.isPresent() && adjacent.get().getValue() == field.getValue() + 1) {
            wander(adjacent.get(), beenThereMap, reachedPeaks, path.next(adjacent.get().getPos()), distinctPaths);
        }
    }

    record ScanResult(int reachedPeaks, int distinctPathways) {}

    @Data
    private static class Path {
        private final Pos[] steps;

        Path(Pos head) {
            steps = new Pos[]{head};
        }

        Path(Path base, Pos tail) {
            steps = Arrays.copyOf(base.steps, base.steps.length + 1);
            steps[base.steps.length] = tail;
        }

        Path next(Pos pos) {
            return new Path(this, pos);
        }
    }
}

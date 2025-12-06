/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.Map2D;
import de.geburtig.advent.util.Map2D.Direction;
import de.geburtig.advent.util.Map2D.Field;
import de.geburtig.advent.util.Map2D.Pos;

import java.util.*;

import static de.geburtig.advent.util.Map2D.Direction.*;

/**
 * https://adventofcode.com/2024/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        Map2D<Integer> map = InputResolver.fetchInputAsIntMap("input_day10.txt");
        map.print();

        List<Pos> trailheads = new ArrayList<>();
        List<Pos> peaks = new ArrayList<>();
        map.forEach((pos, value) -> {
            if (value == 0) trailheads.add(pos);
                else if (value == 9) peaks.add(pos);
        });
        System.out.println("Got " + trailheads.size() + " trailheads and " + peaks.size() + " peaks");

        int result = 0;
        for (Pos pos : trailheads) {
            Field<Integer> trailhead = map.getField(pos);
            Map2D<Boolean> beenThereMap = new Map2D<>(map.getWidth(), map.getHeight(), false);
            Set<Field<Integer>> reachablePeaks = scanTrailways(trailhead, beenThereMap);
//            System.out.println(reachablePeaks.size());
            result += reachablePeaks.size();
        }

        // Part 1: 841
        System.out.println(result);
    }

    static Set<Field<Integer>> scanTrailways(final Field<Integer> field, Map2D<Boolean> beenThereMap) {
        Set<Field<Integer>> reachedPeaks = new HashSet<>();
        wander(field, beenThereMap, reachedPeaks);
        return reachedPeaks;
    }

    private static void wander(Field<Integer> field, Map2D<Boolean> beenThereMap, Set<Field<Integer>> reachedPeaks) {
        beenThereMap.set(field.getPos(), true);
        if (field.getValue() == 9) {
            reachedPeaks.add(field);
        } else {
            tryAndGo(north, field, beenThereMap, reachedPeaks);
            tryAndGo(east, field, beenThereMap, reachedPeaks);
            tryAndGo(south, field, beenThereMap, reachedPeaks);
            tryAndGo(west, field, beenThereMap, reachedPeaks);
        }
    }

    private static void tryAndGo(final Direction direction, final Field<Integer> field, Map2D<Boolean> beenThereMap, Set<Field<Integer>> reachedPeaks) {
        Optional<Field<Integer>> adjacent = field.getAdjacent(direction);
        if (adjacent.isPresent() && !beenThereMap.get(adjacent.get().getPos()) && adjacent.get().getValue() == field.getValue() + 1) {
            wander(adjacent.get(), beenThereMap, reachedPeaks);
        }
    }
}

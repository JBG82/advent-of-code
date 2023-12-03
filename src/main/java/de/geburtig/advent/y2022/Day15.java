package de.geburtig.advent.y2022;

import de.geburtig.advent.base.DayBase;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 extends DayBase {

    static int[][] map;

    private List<Sensor> sensors;

    private static final int GAP = 5_000_000;

    public String resolveExample1() {
        initFromInputFile("/y2022/input_day15_example.txt");

        BitSet bitSet = new BitSet();
        for (Sensor sensor : sensors) {
            bitSet.or(sensor.forRow(10));
        }
        long beaconsOnRow = sensors.stream().map(s -> s.closestBeacon).distinct().filter(b -> b.pos.y == 10).count();
        return String.valueOf(bitSet.cardinality() - beaconsOnRow);
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        initFromInputFile("/y2022/input_day15.txt");

        BitSet bitSet = new BitSet();
        for (Sensor sensor : sensors) {
            bitSet.or(sensor.forRow(2000000));
        }
        long beaconsOnRow = sensors.stream().map(s -> s.closestBeacon).distinct().filter(b -> b.pos.y == 2000000).count();
        return String.valueOf(bitSet.cardinality() - beaconsOnRow);
    }

    private static int row = 0;

    @Override
    public String resolvePuzzle2() throws Exception {
        initFromInputFile("/y2022/input_day15.txt");

//        3267339
        BitSet bitSet = new BitSet();
        for (Sensor sensor : sensors) {
            bitSet.or(sensor.forRow(3267339));
        }
        int i = bitSet.nextClearBit(GAP) - GAP;
        System.out.println(i);
        // x = 2.557.297, y = 3.267.339
        // 2.557.297 * 4.000.000 + 3.267.339 = 10.229.191.267.339
        // 110.267.267.339 - too low

        // TODO: Runs for about an hour, surely suitable for refactoring
/*
        List<Integer> rowCandidates = new ArrayList<>();
        int period = 10;
        AtomicInteger secs = new AtomicInteger();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("At row " + row + "/4000000 after " + secs.addAndGet(period) + " seconds, got " + rowCandidates.size() + " candidates so far: " + rowCandidates.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }, period, period, TimeUnit.SECONDS);

        for (row = 0; row <= 4_000_000; ++row) {
            BitSet bitSet = new BitSet();
            for (Sensor sensor : sensors) {
                bitSet.or(sensor.forRow(row));
            }
            int i = bitSet.nextClearBit(GAP) - GAP;
            if (i >= 0 && i <= 4_000_000) {
                System.out.println("Got one on row " + row);
            }
        }
*/
        return String.valueOf(0);
    }

    void initFromInputFile(String inputFile) {
        Pattern p1 = Pattern.compile("[xy]=(-?\\d+)");
        sensors = new ArrayList<>();

        List<String> lines = getLinesFromInputFile(inputFile);
        for (String line : lines) {
//            System.out.println(line);
            Matcher matcher = p1.matcher(line);
            List<Integer> values = new ArrayList<>(4);
            while (matcher.find()) {
                values.add(Integer.parseInt(matcher.group(1)));
            }
//            System.out.println(values.size() + ": " + values.stream().map(String::valueOf).collect(Collectors.joining(",")));
//            System.out.println(matcher.matches() + "; " + matcher.find() + "; ");
            Beacon beacon = new Beacon(new Point(values.get(2), values.get(3)));
            sensors.add(new Sensor(new Point(values.get(0), values.get(1)), beacon));
        }

//        int minX = sensors.stream().mapToInt(s -> Math.min(s.pos.x, s.closestBeacon.pos.x)).min().orElseThrow();
//        int minY = sensors.stream().mapToInt(s -> Math.min(s.pos.y, s.closestBeacon.pos.y)).min().orElseThrow();
//        int maxX = sensors.stream().mapToInt(s -> Math.max(s.pos.x, s.closestBeacon.pos.x)).max().orElseThrow();
//        int maxY = sensors.stream().mapToInt(s -> Math.max(s.pos.y, s.closestBeacon.pos.y)).max().orElseThrow();
        int minX = sensors.stream().mapToInt(s -> s.pos.x - s.distanceToClosestBeacon).min().orElseThrow();
        int minY = sensors.stream().mapToInt(s -> s.pos.y - s.distanceToClosestBeacon).min().orElseThrow();
        int maxX = sensors.stream().mapToInt(s -> s.pos.x + s.distanceToClosestBeacon).max().orElseThrow();
        int maxY = sensors.stream().mapToInt(s -> s.pos.y + s.distanceToClosestBeacon).max().orElseThrow();
        System.out.println("Range: [" + minX + "," + minY + "] to [" + maxX + "," + maxY + "]");

//        sensors.forEach(System.out::println);
    }

    @Data
    static abstract class Element {
        final Point pos;

        String at() {
            return "[" + pos.x + "," + pos.y + "]";
        }
    }

    static class Sensor extends Day15.Element {

        private final Beacon closestBeacon;
        private final int distanceToClosestBeacon;

        Sensor(final Point pos, final Beacon closestBeacon) {
            super(pos);
            this.closestBeacon = closestBeacon;
            this.distanceToClosestBeacon = Math.abs(Math.max(pos.x, closestBeacon.pos.x) - Math.min(pos.x, closestBeacon.pos.x))
                    + Math.abs(Math.max(pos.y, closestBeacon.pos.y) - Math.min(pos.y, closestBeacon.pos.y));
        }

        BitSet forRow(final int row) {
            BitSet bitSet = new BitSet();
            int distY = Math.max(pos.y, row) - Math.min(pos.y, row);
//            if (row >= pos.y - distanceToClosestBeacon && row <= pos.y + distanceToClosestBeacon) {
            if (distY < distanceToClosestBeacon) {
                bitSet.set(pos.x - distanceToClosestBeacon + distY + GAP, pos.x + distanceToClosestBeacon - distY + 1 + GAP);
            }
            return bitSet;
        }

        @Override
        public String toString() {
            return "Sensor at " + at() + " with distance " + distanceToClosestBeacon + " to closest beacon at " + closestBeacon.at();
        }
    }

    static class Beacon extends Element {
        Beacon(final Point pos) {
            super(pos);
        }
    }
}

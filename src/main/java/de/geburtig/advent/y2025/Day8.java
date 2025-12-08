package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day8 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day8.txt");
        List<Box> boxes = input.stream().map(l -> {
            String[] s = l.split(",");
            return new Box(new Pos(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2])));
        }).toList();
        System.out.println("Got " + boxes.size() + " boxes");

        List<Connection> possibleConnections = new ArrayList<>();
        for (int i = 0; i < boxes.size() - 1; i++) {
            for (int j = i + 1; j < boxes.size(); ++j) {
                possibleConnections.add(new Connection(boxes.get(i), boxes.get(j)));
            }
        }
        possibleConnections.sort(Comparator.comparingDouble(Connection::getDistance));
        ArrayList<Connection> connections = new ArrayList<>(possibleConnections.subList(0, 1000));

        List<Circuit> circuits = new ArrayList<>();
        outer:
        for (Connection con : connections) {
            for (Circuit circuit : circuits) {
                if (circuit.contains(con)) {
                    circuit.add(con);
                    continue outer;
                }
            }
            circuits.add(new Circuit(con));
        }

        System.out.println(circuits.size());
        System.out.println("Got " + circuits.stream().flatMap(c -> c.getBoxes().stream()).distinct().count() + " boxes");

        boolean changed;
        do {
            changed = false;
            System.out.println("iteration");
            int i = 0;
            do {
                Circuit c1 = circuits.get(i);
                int j = i + 1;
                while (j < circuits.size()) {
                    Circuit c2 = circuits.get(j);
                    if (c1.overlaps(c2)) {
                        c1.merge(c2);
                        System.out.println(circuits.size());
                        circuits.remove(c2);
                        System.out.println(circuits.size());
                        changed = true;
                    } else {
                        ++j;
                    }
                }
            } while (++i < circuits.size());
        } while (changed);

        System.out.println(circuits.size());
        System.out.println("Got " + circuits.stream().flatMap(c -> c.getBoxes().stream()).distinct().count() + " boxes");

//        circuits.forEach(System.out::println);

        for (int x = 0; x < circuits.size() - 1; ++x) {
            for (int y = x + 1; y < circuits.size(); ++y) {
                if (circuits.get(x).overlaps(circuits.get(y))) {
                    System.out.println("Overlapping:");
//                    System.out.println(circuits.get(x));
//                    System.out.println(circuits.get(y));
//                    throw new RuntimeException("Got one!");
                }
            }
        }

        // Part 1: 84968
//        System.out.println();
        int[] array = circuits.stream().mapToInt(Circuit::size).sorted().toArray();
        Arrays.stream(array).forEach(x -> System.out.print(x + " "));
        System.out.println();
        long result = ((long) array[array.length - 1]) * array[array.length - 2] * array[array.length - 3];
        System.out.println(result);
    }

    @Data
    @ToString(of = "boxes")
    static class Circuit {
        Set<Connection> connections = new HashSet<>();
        Set<Box> boxes = new HashSet<>();

        Circuit(Connection connection) {
            add(connection);
        }

        boolean contains(Connection connection) {
            return boxes.contains(connection.b1) || boxes.contains(connection.b2);
        }

        void add(Connection connection) {
            connections.add(connection);
            boxes.add(connection.b1);
            boxes.add(connection.b2);
        }

        boolean overlaps(Circuit other) {
            for (Box box : boxes) {
                if (other.boxes.contains(box)) {
                    return true;
                }
            }
            return false;
        }

        void merge(Circuit other) {
            connections.addAll(other.connections);
            boxes.addAll(other.boxes);
        }

        int size() {
            return boxes.size();
        }
    }

    @Data
    static class Connection {
        private final Box b1;
        private final Box b2;
        private final double distance;

        public Connection(Box b1, Box b2) {
            this.b1 = b1;
            this.b2 = b2;
            this.distance = Math.sqrt(Math.pow(b1.pos.x - b2.pos.x, 2) + Math.pow(b1.pos.y - b2.pos.y, 2) + Math.pow(b1.pos.z - b2.pos.z, 2));
        }
    }

    record Box(Pos pos) {}

    record Pos(int x, int y, int z) {}
}

package de.geburtig.advent.y2025;

import de.geburtig.advent.util.InputResolver;
import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * https://adventofcode.com/2025/day/8
 */
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
        System.out.println("Got " + possibleConnections.size() + " possible connections");

        // Part 1: 84968
        System.out.println("Part 1: " + solvePart1(possibleConnections));
        // Part 2: 8663467782
        System.out.println("Part 2: " + solvePart2(possibleConnections));
    }

    static void mergeCircuits(List<Circuit> circuits) {
        boolean changed;
        do {
            changed = false;
            int i = 0;
            do {
                Circuit c1 = circuits.get(i);
                int j = i + 1;
                while (j < circuits.size()) {
                    Circuit c2 = circuits.get(j);
                    if (c1.overlaps(c2)) {
                        c1.merge(c2);
                        circuits.remove(c2);
                        changed = true;
                    } else {
                        ++j;
                    }
                }
            } while (++i < circuits.size());
        } while (changed);
    }

    static long solvePart1(List<Connection> possibleConnections) {
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

        mergeCircuits(circuits);
        int[] array = circuits.stream().mapToInt(Circuit::size).sorted().toArray();
        return ((long) array[array.length - 1]) * array[array.length - 2] * array[array.length - 3];
    }

    static long solvePart2(List<Connection> possibleConnections) {
        int amountOfConnections = 5720;
        long circuitCount, boxesCount, result;
        do {
            System.out.println("Trying " + amountOfConnections + " connections...");

            ArrayList<Connection> connections = new ArrayList<>(possibleConnections.subList(0, amountOfConnections));

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
            mergeCircuits(circuits);

            circuitCount = circuits.size();
            boxesCount = circuits.stream().flatMap(c -> c.getBoxes().stream()).distinct().count();
//            System.out.println("Got " + circuits.size() + " circuits with " + boxesCount + " contained boxes");

            Connection lastConnection = possibleConnections.get(amountOfConnections - 1);
            result = (long) lastConnection.b1.pos().x() * lastConnection.b2.pos().x();
//            System.out.println("Multiplied x of last connection is " + result);
            amountOfConnections++;
        } while (circuitCount != 1 || boxesCount != 1000);
        return result;
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

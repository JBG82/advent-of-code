package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Day12 extends DayBase {

    public String resolveExample1() throws Exception {
        HightMap map = new HightMap(getLinesFromInputFile("/input_day12_example.txt"));
        map.scanPaths();
        System.out.println("Got " + map.possiblePaths.size() + " possible paths to goal");
//        System.out.println(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).min());
//        System.out.println(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).max());
        return String.valueOf(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).min().orElse(0));
    }

    public String resolveExample2() throws Exception {
        return String.valueOf("0");
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        HightMap map = new HightMap(getLinesFromInputFile("/input_day12_fake.txt"));
        map.scanPaths();
        System.out.println("Got " + map.possiblePaths.size() + " possible paths to goal");
//        System.out.println(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).min());
//        System.out.println(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).max());
        return String.valueOf(map.possiblePaths.stream().mapToInt(p -> p.stack.size()).min().orElse(0));
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        return String.valueOf("0");
    }

    static class HightMap {

        private final Square[][] map;

        private Square start;
        private Square goal;

        public HightMap(final List<String> lines) {
            // Init height map
            map = new Square[lines.size()][lines.get(0).length()];
            for (int y = 0; y < lines.size(); ++y) {
                String line = lines.get(y);
                for (int x = 0; x < line.length(); ++x) {
                    char value = line.charAt(x);
                    boolean isStart = false, isGoal = false;
                    if (value == 'S') {
                        isStart = true;
                        value = 'a';
                    } else if (value == 'E') {
//                        isGoal = true;
                        value = 'z';
                    } else if (value == 'G') {
                        // TODO: Just for test, needs to be erased!
                        isGoal = true;
                        value = 'c';
                    }
                    Square square = new Square(x, y, value);
                    map[y][x] = square;
                    if (isStart) start = square;
                    if (isGoal) {
                        goal = square;
                    }
                }
            }

            // Set neighbours and check movement options
            for (int y = 0; y < map.length; ++y) {
                for (int x = 0; x < map[y].length; ++x) {
                    Square ref = map[y][x];
                    if (x < map[y].length - 1) {
                        Square east = map[y][x + 1];
                        ref.neighbours.put(Direction.EAST, east);
                        east.neighbours.put(Direction.WEST, ref);
                    }
                    if (y < map.length - 1) {
                        Square south = map[y + 1][x];
                        ref.neighbours.put(Direction.SOUTH, south);
                        south.neighbours.put(Direction.NORTH, ref);
                    }
                    ref.defineOptions();
                }
            }
        }

        public void scanPaths() {
            int period = 10;
            AtomicInteger secs = new AtomicInteger(0);
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                System.out.println("Got " + possiblePaths.size() + " possible paths after " + secs.addAndGet(period) + " seconds");
            }, period, period, TimeUnit.SECONDS);

            PathElement root = new PathElement(start, null);
            scan(root);
        }

        public List<Path> possiblePaths = new ArrayList<>();

        public void scan(final PathElement element) {
            Square squaere = element.position;
            if (squaere == goal) {
                possiblePaths.add(new Path(element));
            } else {
                for (Direction option : squaere.getPossibleOptionsOrdered()) {
                    Square neighbour = squaere.getNeighbour(option);
                    if (!element.alreadyVisited(neighbour)) {
                        PathElement next = new PathElement(neighbour, element);
                        scan(next);
                    }
                }
            }
        }
    }

    static class Path {
        private final Stack<PathElement> stack = new Stack<>();

        Path(final PathElement goalElement) {
            PathElement element = goalElement;
            do {
                stack.push(element);
                element = element.getPrevious();
            } while (element.getPrevious() != null);
        }
    }

    @Data
//    @EqualsAndHashCode(of = "position")
    @ToString(of = "position")
    static class PathElement {
        private final Square position;
        private final PathElement previous;
//        private final PathElement next;

        private int option;

        public boolean alreadyVisited(Square square) {
            PathElement element = this;
            while (element.previous != null) {
                if (element.previous.position == square) return true;
                element = element.previous;
            }
            return false;
        }
    }


    private static final Map<Direction, Movement> INIT_OPTIONS = new HashMap<>(4);
    static {
        INIT_OPTIONS.put(Direction.NORTH, Movement.NOT_POSSIBLE);
        INIT_OPTIONS.put(Direction.EAST, Movement.NOT_POSSIBLE);
        INIT_OPTIONS.put(Direction.SOUTH, Movement.NOT_POSSIBLE);
        INIT_OPTIONS.put(Direction.WEST, Movement.NOT_POSSIBLE);
    }

    @Data
    @EqualsAndHashCode(of = {"x", "y", "value"})
    @ToString(of = {"x", "y", "value"})
    static class Square {
        private final int x, y;
        private final char value;
//        private Map<Direction, Movement> options = new HashMap<>(INIT_OPTIONS);
        private Map<Direction, Movement> options = new HashMap<>();
        private Map<Direction, Square> neighbours = new HashMap<>(4);

        public int num() {
            return value - 'a';
        }

        public Square getNeighbour(final Direction direction) {
            return neighbours.get(direction);
        }

        public void defineOptions() {
            neighbours.forEach((directionToNeighbour, neighbour) -> {
                Direction oppositeDirection = directionToNeighbour.opposite();
                if (this.num() == neighbour.num()) {
                    this.options.put(directionToNeighbour, Movement.EVEN);
                    neighbour.options.put(oppositeDirection, Movement.EVEN);
                } else if (this.num() > neighbour.num()) {
                    this.options.put(directionToNeighbour, Movement.DOWN);
                    neighbour.options.put(oppositeDirection, (this.num() == neighbour.num() + 1) ? Movement.UP : Movement.NOT_POSSIBLE);
                } else if (this.num() < neighbour.num()) {
                    this.options.put(directionToNeighbour, (neighbour.num() == this.num() + 1) ? Movement.UP : Movement.NOT_POSSIBLE);
                    neighbour.options.put(oppositeDirection, Movement.DOWN);
                }
            });
        }

        public List<Direction> getPossibleOptionsOrdered() {
            return options.entrySet().stream()
                    .filter(e -> !Movement.NOT_POSSIBLE.equals(e.getValue()))
                    .sorted(Map.Entry.comparingByValue(Movement::compareTo))
                    .map(Map.Entry::getKey)
                    .toList();
        }
    }

    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case EAST -> WEST;
                case SOUTH -> NORTH;
                case WEST -> EAST;
            };
        }
    }

    enum Movement {
        // TODO: Do I need NOT_POSSIBLE at all?
        UP, EVEN, DOWN, NOT_POSSIBLE;
    }
}

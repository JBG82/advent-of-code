/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.utils.CollectionUtil;
import lombok.*;

import java.util.*;

/**
 * TODO
 *
 * @author jochen.geburtig
 */
public class Day6 {
    public static void main(final String[] args) throws InterruptedException {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day6.txt", Day6.class);

        State[][] map = new State[input.getFirst().length()][input.size()];
        Guard guardAtStart = null;
        Guard guard;
        int y = 0;
        for (String line : input) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                map[x][y] = State.of(c);
                if (c == '^') {
                    guardAtStart = new Guard(new Pos(x, y), Direction.NORTH);
                }
            }
            ++y;
        }
        State[][] mapAtStart = CollectionUtil.copyArray(map);
        guard = new Guard(guardAtStart.pos, Direction.NORTH);

        List<Pos> visitedList = Collections.emptyList();
        try {
            visitedList = runTheGuard(map, guard);
        } catch (LoopDetectedException e) {
            System.out.println("Loop detected at " + guard.pos);
        }

        // 5086
        System.out.println("Result 1: " + (visitedList.size() + 1));

        // Now trying to put an additional obstacle in every visited position and run it again...
        List<Pos> potentialObstacles = new ArrayList<>();
        for (Pos pos : visitedList) {
            State[][] editedMap = CollectionUtil.copyArray(mapAtStart);
            guard = new Guard(guardAtStart.pos, Direction.NORTH);
            editedMap[pos.x][pos.y] = State.OBSTACLE;
            try {
                runTheGuard(editedMap, guard);
            } catch (LoopDetectedException e) {
                potentialObstacles.add(pos);
            }
        }
        // 1770
        System.out.println("Result 2: " + potentialObstacles.size());
    }

    static List<Pos> runTheGuard(final State[][] map, final Guard guard) throws LoopDetectedException {
        List<Pos> visitedList = new ArrayList<>();
        do {
            if (guard.canMove(map)) {
                boolean isOutOfBounds = guard.move(map);
                if (isOutOfBounds) {
                    break;
                }
                if (map[guard.pos.x][guard.pos.y] == State.FREE) {
                    visitedList.add(guard.pos);
                }
            } else {
                guard.turn();
            }
        } while (true);
        return visitedList;
    }

    record Pos(int x, int y) {
        Pos next(final @NonNull Direction direction) {
            return switch (direction) {
                case NORTH -> new Pos(x, y - 1);
                case EAST -> new Pos(x + 1, y);
                case SOUTH -> new Pos(x, y + 1);
                case WEST -> new Pos(x - 1, y);
            };
        }
    }

    @AllArgsConstructor
    @Data static class Guard {
        private Pos pos;
        private Direction dir;
        private final Map<Pos, Integer> visitedRegister = new HashMap<>();

        boolean canMove(final State[][] map) {
            Pos next = pos.next(dir);
            if (next.x < 0 || next.y < 0 || next.x == map.length || next.y == map[0].length) {
                return true;
            } else {
                return map[next.x][next.y] != State.OBSTACLE;
            }
        }

        /**
         * Returns true, if guard moves out of map
         */
        boolean move(final State[][] map) throws LoopDetectedException {
            if (map[pos.x][pos.y] == State.VISITED) {
                Integer visited = visitedRegister.compute(pos, (k, v) -> v == null ? 2 : v + 1);
                if (visited > 4) {
                    throw new LoopDetectedException();
                }
            }
            map[pos.x][pos.y] = State.VISITED;
            pos = pos.next(dir);
            return pos.x < 0 || pos.y < 0 || pos.x == map.length || pos.y == map[0].length;
        }

        void turn() {
            dir = dir.turnRight();
        }
    }

    static class LoopDetectedException extends Exception {}

    enum Direction {
        NORTH, EAST, SOUTH, WEST;

        Direction turnRight() {
            return switch (this) {
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
            };
        }
    }

    @RequiredArgsConstructor
    @Getter
    enum State {
        GUARD('G'),
        FREE('.'),
        OBSTACLE('#'),
        VISITED('X');

        private final char c;

        public static State of(final char c) {
            return switch (c) {
                case 'G', '^' -> GUARD;
                case '.' -> FREE;
                case '#' -> OBSTACLE;
                case 'X' -> VISITED;
                default -> throw new IllegalArgumentException("Unexpected character: " + c);
            };
        }
    }
}

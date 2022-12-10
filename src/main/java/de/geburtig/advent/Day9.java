package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.IntStream.range;

public class Day9 extends DayBase {

    @Override
    public String resolvePuzzle1() throws Exception {
        Rope rope = new Rope(2);

        List<String> lines = getLinesFromInputFile("/input_day9.txt");
        lines.forEach(line -> {
            String[] part = line.split(" ");
            rope.move(part[0], Integer.parseInt(part[1]));
        });
        return String.valueOf(rope.tail.visited.size());
    }

    @Override
    public String resolvePuzzle2() {
        Rope rope = new Rope(10);

        List<String> lines = getLinesFromInputFile("/input_day9.txt");
        lines.forEach(line -> {
            String[] part = line.split(" ");
            rope.move(part[0], Integer.parseInt(part[1]));
        });
        return String.valueOf(rope.tail.visited.size());
    }

    static class Rope {
        private final Knot head;
        private final Knot tail;
        private final List<Knot> knots;

        public Rope(int knots) {
            if (knots < 2) throw new IllegalArgumentException("Minimum knots is 2");
            this.head = new Knot(null);
            this.knots = new ArrayList<>(knots);
            this.knots.add(head);

            Knot last = head;
            for (int i = 0; i < knots - 1; ++i) {
                last = new Knot(last);
                this.knots.add(last);
            }
            this.tail = last;
        }
        public void move(String direction, int steps) {
            range(0, steps).forEach(i -> move(direction));
        }

        private void move(String direction) {
            // Move head
            if ("U".equals(direction)) {
                head.up();
            } else if ("D".equals(direction)) {
                head.down();
            } else if ("L".equals(direction)) {
                head.left();
            } else if ("R".equals(direction)) {
                head.right();
            } else {
                throw new RuntimeException("Unexpected direction " + direction);
            }

            // Follow tails
            knots.stream().skip(1).forEachOrdered(Knot::followParent);
        }
    }

    @Data
    static class Knot {
        private Position pos;
        private final Knot parent;
        private Set<Position> visited = new HashSet<>();

        public Knot(final Knot parent) {
            this.parent = parent;
            this.pos = parent != null ? parent.getPos() : new Position(0, 0);
            this.visited.add(pos);
        }

        public void up() {
            pos = new Position(pos.x, pos.y + 1);
        }

        public void down() {
            pos = new Position(pos.x, pos.y - 1);
        }

        public void left() {
            pos = new Position(pos.x - 1, pos.y);
        }

        public void right() {
            pos = new Position(pos.x + 1, pos.y);
        }

        void followParent() {
            int distX = parent.pos.x - pos.x;
            int distY = parent.pos.y - pos.y;
            int dist = Math.abs(distX) + Math.abs(distY);
            if (distX > 1 || (distX == 1 && dist > 2)) {
                right();
            } else if (distX < -1 || (distX == -1 && dist > 2)) {
                left();
            }
            if (distY > 1 || (distY == 1 && dist > 2)) {
                up();
            } else if (distY < -1 || (distY == -1 && dist > 2)) {
                down();
            }
            visited.add(pos);
        }
    }

    @Data
    static class Position {
        private final int x;
        private final int y;

        @Override
        public String toString() {
            return "(" + x + "|" + y + ")";
        }
    }
}

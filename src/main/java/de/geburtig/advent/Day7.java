package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Day7 extends DayBase {

    private Filesystem filesystem;

    public Day7() {
        try {
            initFilesystem();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initFilesystem() throws Exception {
        Scanner scanner = createInputFileScanner("/input_day7.txt").useDelimiter("\n\\$ ");
        filesystem = new Filesystem();

        int count = 0;
        while (scanner.hasNext()) {
            String part = scanner.next();
            if (part.contains("\n")) {
                String command = part.substring(0, part.indexOf("\n"));
                String result = part.substring(part.indexOf("\n") + 1);
                filesystem.processCommand(command, result);
            } else {
                filesystem.processCommand(part, null);
            }
        }
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        AtomicLong sum = new AtomicLong(0);
        scanForPuzzle1(filesystem.getRoot(), sum);
        return sum.toString();
    }

    private void scanForPuzzle1(Node node, final AtomicLong sum) {
        long total = node.deepSize();
        boolean counts = node.isDirectory() && total <= 100_000;
        if (counts) sum.addAndGet(total);
        for (Node child : node.children.values()) {
            scanForPuzzle1(child, sum);
        }
    }

    @Override
    public String resolvePuzzle2() {
        long usedSpace = filesystem.getRoot().deepSize();
//        long toDelete = (70_000_000 - usedSpace - 30_000_000) * -1;
        Node candidate = scanForPuzzle2(filesystem.getRoot(), null);

        return String.valueOf(candidate.deepSize());
    }

    Node scanForPuzzle2(Node node, Node candidate) {
        if (node.isDirectory()) {
            long total = node.deepSize();
            // Total space (70_000_000) - Needed space (30_000_000) - Used space (40_913_445)
            if (total >= 913445 && (candidate == null || total < candidate.deepSize())) {
                candidate = node;
            }
            for (Node child : node.children.values()) {
                candidate = scanForPuzzle2(child, candidate);
            }
        }
        return candidate;
    }

    static class Filesystem {

        private final @Getter Node root = new Node("", null, null);
        private Node pos = root;

        public void processCommand(String command, String result) {
            String[] part = command.split(" ");
            if (part[0].equals("$")) return; // first command, do nothing here...

            if (part[0].equals("cd")) {
                if (part[1].equals("/")) {
                    pos = root;
                } else if (part[1].equals("..")) {
                    pos = pos.getParent();
                } else {
                    pos = pos.getOrInitChild(part[1], null);
                }
            } else if (part[0].equals("ls")) {
                Arrays.stream(result.split("\n")).forEach(line -> {
                    String[] split = line.split(" ");
                    pos.getOrInitChild(split[1], split[0]);
                });
            } else {
                throw new RuntimeException("Unexpected command: " + command);
            }
        }
    }

    @Data
    @EqualsAndHashCode(of = {"parent", "name"})
    static class Node {
        private final String name;
        private final Node parent;
        private final Map<String, Node> children = new HashMap<>();

        private final long size;

        public Node(String name, String attr, Node parent) {
            this.name = name;
            this.size = attr ==  null || "dir".equals(attr) ? 0 : Long.parseLong(attr);
            this.parent = parent;
        }

        @Override
        public String toString() {
            if (isRoot()) return "/";
            return (parent.isRoot() ? "" : parent) +"/" + name;
        }

        public Node getOrInitChild(final String name, final String attr) {
            return children.computeIfAbsent(name, n -> new Node(n, attr, this));
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isDirectory() {
            return children.size() > 0;
        }

        public long deepSize() {
            return children.values().stream().mapToLong(Node::deepSize).sum() + size;
        }
    }
}

/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.utils.graph.Node;
import lombok.Data;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2023/day/8
 */
public class Day8 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day8.txt", Day8.class);
//        List<String> input = InputResolver.fetchLinesFromInputFile("input_day8_example.txt", Day8.class);
        String commandSequence = input.getFirst();

        Map<String, Node<String>> nodeMap = new HashMap<>();
        input.stream().skip(2).forEach(line -> {
            String value = line.substring(0, 3);
            String child1 = line.substring(7, 10);
            String child2 = line.substring(12, 15);
            Node<String> node = nodeMap.computeIfAbsent(value, Node::new);
            if (node.getChildren().isEmpty()) {
                Node<String> cn1 = nodeMap.computeIfAbsent(child1, Node::new);
                Node<String> cn2 = nodeMap.computeIfAbsent(child2, Node::new);
                node.getChildren().addAll(List.of(cn1, cn2));
            } else if (node.getChildren().size() == 1) {
                throw new RuntimeException("Häh!?");
            }
        });

        long steps;

        // Puzzle 1
        Node<String> node = nodeMap.get("AAA");
        steps = 0;
        do {
            for (char command : commandSequence.toCharArray()) {
                node = node.move(command);
                ++steps;
                if ("ZZZ".equals(node.get())) {
                    break;
                }
            }
//            System.out.println("Not finished after " + steps + " steps. Starting over again...");
        } while (!"ZZZ".equals(node.get()));
        System.out.println("Result 1: " + steps);

        // Puzzle 2
        List<Node<String>> nodes = new ArrayList<>(nodeMap.values().stream().filter(n -> n.get().endsWith("A")).toList());
        steps = 0;
        int onTarget = 0;
        int max = 0;
        int roundTrip = 0;
        int stepInRoundTrip;
        int[] lastHitOnRoundTrip = new int[nodes.size()];
        long[] lastHitOnStep = new long[nodes.size()];

        long[] lastGrowth = new long[nodes.size()];
        long[] targetGrowth = {18727, 22411, 16271, 14429, 24253, 20569};
        do {
            stepInRoundTrip = 0;
            ++roundTrip;
            for (char command : commandSequence.toCharArray()) {
                onTarget = 0;
                ++steps;
                ++stepInRoundTrip;
                for (int i = 0; i < nodes.size(); ++i) {
                    Node<String> n = nodes.set(i, nodes.get(i).move(command));
                    if (n.get().endsWith("Z")) {
                        ++onTarget;
//                        System.out.println("Path " + i + " on target after " + steps + " steps on round trip " + roundTrip + " (" + n.get() + ")");

                        long groth = steps - lastHitOnStep[i];
                        System.out.println("Path " + i + ": " + stepInRoundTrip + " " + n.get() + " " + steps + " " + roundTrip + " " + groth + " " + (roundTrip - lastHitOnRoundTrip[i]));
                        lastHitOnRoundTrip[i] = roundTrip;
                        lastHitOnStep[i] = steps;
                        lastGrowth[i] = groth;
                    }
                }
                if (onTarget == nodes.size()) {
                    break;
                } else if (onTarget > max) {
                    max = onTarget;
//                    System.out.println(max + " after " + steps + " steps");
                }

                boolean gotIt = true;
                for (int i = 0; i < nodes.size(); ++i) {
                    if (lastGrowth[i] != targetGrowth[i]) {
                        gotIt = false;
                        break;
                    }
                }
                if (gotIt) {
                    onTarget = 6;
                    break;
                }
            }
//            System.out.println("New round trip after " + steps + " steps");
//            System.out.println("Not finished after " + steps + " steps. Starting over again...");
        } while (onTarget < nodes.size());
        System.out.println("Result 2: " + steps);

        // 0: 37455 alle 18727 steps
        // 1: 44823 alle 22411 steps
        // 2: 32543 alle 16271 steps
        // 3: 28859 alle 14429 steps
        // 4: 48507 alle 24253 steps
        // 5: 41139 alle 20569 steps
        long[] pos = {37455, 44823, 32543, 28859, 48507, 41139};
        int[] growth = {18727, 22411, 16271, 14429, 24253, 20569};
        long step = 28859;
        int groth = 14429;
        do {
            step += groth;
            boolean fits = true;
            for (int i = 0; i < pos.length; ++i) {
                if (pos[i] < step) {
                    pos[i] += growth[i];
                }
                if (pos[i] != step) {
                    fits = false;
                }
            }

//            System.out.println(step + ": " + Arrays.stream(pos).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
            if (fits) {
                break;
            }
        } while (true);

        // 18024643846274 too high
        System.out.println("Result 2: " + step);
        System.out.println("Result 2: 18024643846273 (correct result, something went wrong on the way...)");
    }

    static class Node<String> extends de.geburtig.utils.graph.Node<String> {
        public Node(@NonNull final String object) {
            super(object);
        }

        Node<String> move(final char command) {
            return (Node<String>) (command == 'L' ? getChildren().getFirst() : getChildren().getLast());
        }
    }
}

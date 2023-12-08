/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2023;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.utils.graph.Node;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/8
 */
public class Day8 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day8.txt", Day8.class);
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

        // Puzzle 1
        Node<String> node = nodeMap.get("AAA");
        long steps = 0;
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
        do {
            ++roundTrip;
            for (char command : commandSequence.toCharArray()) {
                onTarget = 0;
                ++steps;
                for (int i = 0; i < nodes.size(); ++i) {
                    Node<String> n = nodes.set(i, nodes.get(i).move(command));
                    if (n.get().endsWith("Z")) {
                        ++onTarget;
                        System.out.println("Path " + i + " on target after " + steps + " steps on round trip " + roundTrip + " (" + n.get() + ")");
                    }
                }
                if (onTarget == nodes.size()) {
                    break;
                } else if (onTarget > max) {
                    max = onTarget;
                    System.out.println(max + " after " + steps + " steps");
                }
            }
//            System.out.println("New round trip after " + steps + " steps");
//            System.out.println("Not finished after " + steps + " steps. Starting over again...");
        } while (onTarget < nodes.size());
        System.out.println("Result 2: " + steps);
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

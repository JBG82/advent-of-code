/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.y2025;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import de.geburtig.advent.util.InputResolver;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * https://adventofcode.com/2025/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day10.txt");
        List<Input> inputList = lines.stream().map(Input::new).toList();

        // Part 1: 432
        System.out.println("Part 1: " + solvePart1(inputList));
        // Part 2: 18011
        System.out.println("Solving Part 2, could take a few seconds...");
        System.out.println("Part 2: " + solvePart2(inputList));
    }

    private static long solvePart1(List<Input> inputList) {
        long result = 0;
        for (Input input : inputList) {
            List<State> states = Collections.singletonList(State.init(input));

            outer:
            while (true) {
                List<State> newStates = new ArrayList<>();
                for (State state : states) {
                    for (int b = 0; b < input.buttons.length; b++) {
                        State newState = state.push(input.buttons[b]);
                        if (newState.getCurrent().equals(input.getTarget())) {
                            result += newState.pushed.length;
                            break outer;
                        }
                        newStates.add(newState);
                    }
                }
                states = newStates;
            }
        }
        return result;
    }

    private static long solvePart2(List<Input> inputList) {
        Loader.loadNativeLibraries();
        long result = 0;
        for (Input input : inputList) {
            result += solveSinglePart2(input);
        }
        return result;
    }

    static long solveSinglePart2(Input input) {
        // Hier wird die OR-Tools-Bibliothek von Google genutzt, um die Gleichungen aufzulösen.
        MPSolver solver = MPSolver.createSolver("SCIP");

        // Die Variablen in den Gleichungen sind die Anzahl an Betätigungen der einzelnen Buttons
        List<MPVariable> variables = new ArrayList<>();
        for (int b = 0; b < input.buttons.length; b++) {
            variables.add(solver.makeIntVar(0, Double.POSITIVE_INFINITY, "B" + b));
        }

        BiPredicate<Button, Integer> buttonAffects = (button, joltageIdx) -> {
            for (int x = 0; x < button.changes.length; ++x) {
                if (button.changes[x] == joltageIdx) return true;
            }
            return false;
        };
        for (int i = 0; i < input.getJoltage().length; ++i) {
            MPConstraint equation = solver.makeConstraint(input.getJoltage()[i], input.getJoltage()[i]);
            for (int b = 0; b < input.getButtons().length; ++b) {
                if (buttonAffects.test(input.getButtons()[b], i)) {
                    equation.setCoefficient(variables.get(b), 1);
                }
            }
        }

        MPObjective objective = solver.objective();
        for (MPVariable variable : variables) {
            objective.setCoefficient(variable, 1);
        }
        objective.setMinimization();

        solver.solve();

        long result = 0;
        for (MPVariable variable : variables) {
            result += (long) variable.solutionValue();
        }
        return result;
    }

    @Data
    static class State {
        private final int lights;
        private final String current;
        private final int[] pushed;

        static State init(final Input input) {
            return new State(input.target.length(), ".".repeat(input.target.length()), new int[0]);
        }

        public State push(Button button) {
            char[] newCurrent = current.toCharArray();
            for (int change : button.changes) {
                newCurrent[change] = flip(newCurrent[change]);
            }
            int[] newPushed = new int[pushed.length + 1];
            for (int i = 0; i < pushed.length; i++) {
                newPushed[i] = pushed[i];
            }
            newPushed[pushed.length] = button.index;
            return new State(lights, new String(newCurrent), newPushed);
        }

        private char flip(char c) {
            return c == '.' ? '#' : '.';
        }
    }

    @Data
    static class Input {
        private final String target;
        private final Button[] buttons;
        private final int[] joltage;

        Input(final String line) {
            Matcher matcher = Pattern.compile("^\\[(.+)] (.+) \\{(.+)}$").matcher(line);
            if (matcher.find()) {
                target = matcher.group(1);
                String[] split = matcher.group(2).split(" ");
                buttons = new Button[split.length];
                for (int i = 0; i < split.length; i++) {
                    buttons[i] = new Button(i, split[i]);
                }
                joltage = Arrays.stream(matcher.group(3).split(",")).mapToInt(Integer::parseInt).toArray();
            } else {
                throw new RuntimeException("Not found: " + line);
            }
        }

        @Override
        public String toString() {
            return "[" + target + "] " + Arrays.stream(buttons).map(Button::toString).collect(Collectors.joining(" "))
                    + " " + Arrays.toString(joltage).replace("[", "{").replace("]", "}").replace(" ", "");
        }
    }

    @Data
    static class Button {
        private final int index;
        private final int[] changes;

        Button(final int index, final String str) {
            this.index = index;
            changes = Arrays.stream(str.substring(1, str.length() - 1).split(",")).mapToInt(Integer::parseInt).toArray();
        }

        @Override
        public String toString() {
            return Arrays.toString(changes).replace("[", "(").replace("]", ")").replace(" ", "");
        }
    }
}

package de.geburtig.advent;

import de.geburtig.advent.base.DayBase;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static de.geburtig.advent.Day13.Result.*;

public class Day13 extends DayBase {

    static char EOF = 'X';
    static int pos = 0;
    static char nextChar(final String line) {
        return pos >= line.length() ? EOF : line.charAt(pos++);
    }

    enum Result {
        RIGHT_ORDER, FALSE_ORDER, CONTINUE;
    }

    public String resolveExample1() throws Exception {
        Scanner scanner = createInputFileScanner("/input_day13_example.txt").useDelimiter("\n\n");
        return processForPuzzle1(scanner);
    }

    @Override
    public String resolvePuzzle1() throws Exception {
        Scanner scanner = createInputFileScanner("/input_day13.txt").useDelimiter("\n\n");
        return processForPuzzle1(scanner);
    }

    public String resolveExample2() throws Exception {
        List<String> lines = new ArrayList<>(getLinesFromInputFile("/input_day13_example.txt"));
        return processForPuzzle2(lines);
    }

    @Override
    public String resolvePuzzle2() throws Exception {
        List<String> lines = new ArrayList<>(getLinesFromInputFile("/input_day13.txt"));
        return processForPuzzle2(lines);
    }

    String processForPuzzle1(final Scanner scanner) {
        int count = 0, sum = 0;
        while (scanner.hasNext()) {
            ++count;
            String[] pair = scanner.next().split("\n");
            ListElement left = createList(pair[0]);
            ListElement right = createList(pair[1]);
            if (checkRightOrder(left, right)) {
                sum += count;
            }
        }
        return String.valueOf(sum);
    }

    String processForPuzzle2(final List<String> linesFromInput) {
        List<String> lines = new ArrayList<>(linesFromInput);
        lines.add("[[2]]");
        lines.add("[[6]]");
        lines = lines.stream()
                .filter(line -> line.length() > 0)
                .map(this::createList)
                .sorted()
                .map(Object::toString)
                .toList();
        int i1 = lines.indexOf("[[2]]") + 1;
        int i2 = lines.indexOf("[[6]]") + 1;
        return String.valueOf(i1 * i2);
    }

    boolean checkRightOrder(ListElement left, ListElement right) {
        Result result = Element.compare(left, right);
        return result != FALSE_ORDER;
    }

    ListElement createList(final String line) {
        ListElement listElement = new ListElement();
        pos = 0;
        String newLine = line.substring(1, line.length() - 1);
        create(newLine, listElement);
        return listElement;
    }

    void create(final String line, final @NonNull ListElement parent) {
        do {
            char c = nextChar(line);
            if (c == EOF || c == ']') {
                break;
            } else if (c == '[') {
                ListElement listElement = new ListElement();
                parent.add(listElement);
                create(line, listElement);
            } else if (Character.isDigit(c)) {
                String numberValue = c + "";
                c = nextChar(line);
                if (Character.isDigit(c)) {
                    numberValue += c;
                    c = nextChar(line);
                }
                parent.add(new IntElement(numberValue));
                if (c == ']') {
                    break;
                }
            }
        } while(true);
    }

    static abstract class Element {
        public ListElement toListElement() {
            return (ListElement) this;
        }

        public IntElement toIntElement() {
            return (IntElement) this;
        }

        public boolean isList() {
            return this instanceof ListElement;
        }

        static Result compare(Element left, Element right) {
            if (left instanceof IntElement && right instanceof IntElement) {
                int leftValue = ((IntElement) left).value;
                int rightValue = ((IntElement) right).value;
                if (leftValue < rightValue) {
                    return RIGHT_ORDER;
                } else if (leftValue > rightValue) {
                    return FALSE_ORDER;
                } else {
                    return CONTINUE;
                }
            } else if (left instanceof ListElement && right instanceof ListElement) {
                int i = 0;
                ListElement l = left.toListElement();
                ListElement r = right.toListElement();
                do {
                    if (l.got(i) && r.got(i)) {
                        Result result = compare(l.get(i), r.get(i));
                        if (result != CONTINUE) {
                            return result;
                        }
                    } else if (l.got(i)) {
                        return FALSE_ORDER;
                    } else if (r.got(i)) {
                        return RIGHT_ORDER;
                    } else {
                        return CONTINUE;
                    }
                    ++i;
                } while (true);
            } else {
                // Mixed type compare
                ListElement l = left.isList() ? left.toListElement() : left.toIntElement().toList();
                ListElement r = right.isList() ? right.toListElement() : right.toIntElement().toList();
                return compare(l, r);
            }
        }
    }

    static class ListElement extends Element implements Comparable<ListElement> {
        private final List<Element> list = new ArrayList<>();

        void add(final Element element) {
            list.add(element);
        }

        Element get(int index) {
            return list.get(index);
        }

        boolean got(int index) {
            return index < list.size();
        }

        @Override
        public String toString() {
            return "[" + list.stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
        }

        @Override
        public int compareTo(ListElement o) {
            return switch (compare(this, o)) {
                case RIGHT_ORDER -> -1;
                case CONTINUE -> 0;
                case FALSE_ORDER -> 1;
            };
        }
    }

    static class IntElement extends Element {
        private final int value;
        public IntElement(String value) {
            this.value = Integer.parseInt(value);
        }

        public ListElement toList() {
            ListElement result = new ListElement();
            result.add(this);
            return result;
        }
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}

package de.geburtig.advent.y2024;

import de.geburtig.advent.util.InputResolver;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day9 {

    public static void main(final String[] args) {
//        String input = InputResolver.fetchLinesFromInputFile("input_day9_example.txt", Day9.class).getFirst();
        String input = InputResolver.fetchLinesFromInputFile("input_day9.txt", Day9.class).getFirst();

        long result = solvePart1(input);
//        long result = solvePart2(input);

        // Result 1: 6201130364722
        // Result 2: 6221662795602
        System.out.println(result);
    }

    static long solvePart1(final String input) {
        int[] array = input.chars().map(i -> i - 48).toArray();
        int frontIdx = -1;
        int backIdx = array.length - 1;
        int resultIdx = 0;

        int leftOnFront, leftOnBack = array[backIdx];
        long result = 0;
        boolean doneMovingUpFront = false;
        do {
            leftOnFront = array[++frontIdx];
            if (frontIdx >= backIdx) {
                doneMovingUpFront = true;
            }
            if (isFile(frontIdx) && !doneMovingUpFront) {
                int fileIndex = frontIdx / 2;
                while (leftOnFront-- > 0) {
                    result += (long) resultIdx++ * fileIndex;
//                    System.out.print(fileIndex);
                }
            } else {
                int fileIndex = backIdx / 2;
                while (leftOnFront-- > 0) {
                    if (leftOnBack-- > 0) {
                        result += (long) resultIdx++ * fileIndex;
//                        System.out.print(fileIndex);
                    } else {
                        // Hinteren Pointer nach vorne verschieben
                        if (doneMovingUpFront) {
                            break;
                        }
                        do {
                            backIdx -= 2;
                            leftOnBack = array[backIdx];
                        } while (leftOnBack == 0);
                        if (backIdx <= frontIdx) {
                            break;
                        }
                        fileIndex = backIdx / 2;
                        leftOnBack--;
                        result += (long) resultIdx++ * fileIndex;
//                        System.out.print(fileIndex);
                    }
                }
            }
        } while (frontIdx < backIdx);
//        System.out.println("\n");

        return result;
    }

    static long solvePart2(final String input) {
        int[] array = input.chars().map(i -> i - 48).toArray();

        List<File> files = new ArrayList<>();
        List<Gap> gaps = new ArrayList<>();
        long fsIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (isFile(i)) {
                files.add(new File(i / 2, fsIndex, array[i]));
            } else {
                gaps.add(new Gap(fsIndex, array[i]));
            }
            fsIndex += array[i];
        }

        int fileId = files.size() - 1;
        do {
            final int x = fileId;
            File file = files.stream().filter(f -> f.getId() == x).findAny().orElseThrow();
            int i = 0;
            do {
                Gap gap = gaps.get(i++);
                if (gap.index > file.index) {
                    break;
                } else if (gap.size >= file.size) {
                    file.setIndex(gap.index);
                    gap.setIndex(gap.index + file.size);
                    gap.setSize(gap.size - file.size);
                    break;
                }
            } while (true);
        } while (--fileId >= 0);
        return calculate(files);
    }

    static long calculate(final List<File> files) {
        long result = 0;
        for (File file : files.stream().sorted(Comparator.comparingLong(File::getIndex)).toList()) {
            for (int i = 0; i < file.size; ++i) {
                result += (file.index + i) * file.getId();
            }
        }
        return result;
    }

    static boolean isFile(final int idx) {
        return idx % 2 == 0;
    }

    @Data
    @AllArgsConstructor
    static class File {
        final int id;
        long index;
        final int size;
    }

    @Data
    @AllArgsConstructor
    static class Gap {
        long index;
        int size;
    }
}

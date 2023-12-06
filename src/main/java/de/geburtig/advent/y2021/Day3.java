package de.geburtig.advent.y2021;

import de.geburtig.advent.util.InputResolver;

import java.util.List;

public class Day3 {
    public static void main(final String[] args) {
        List<String> input = InputResolver.fetchLinesFromInputFile("input_day3.txt", Day3.class);

        int bitLength = input.getFirst().length();
        int[] set = new int[bitLength];
        for (String line : input) {
            int[] split = line.chars().toArray();
            for (int i = 0; i < bitLength; ++i) {
                if (49 == split[i]) {
                    ++set[i];
                }
            }
        }

//        000010000011
//        000010008421
//           256 16
//          512 32
//             64
//            128
//        2048
        long gamma = 0;
        long epsilon = 0;

        for (int i = 0; i < bitLength; ++i) {
            if (set[i] > input.size() / 2) {
                gamma += (long) Math.pow(2, 11 - i);
            } else {
                epsilon += (long) Math.pow(2, 11 - i);
            }
        }
        System.out.println("Result 1: " + gamma * epsilon);
    }
}

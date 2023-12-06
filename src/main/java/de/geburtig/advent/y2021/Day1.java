package de.geburtig.advent.y2021;

import de.geburtig.advent.util.InputResolver;
import de.geburtig.advent.util.PeekableIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day1 {
    public static void main(final String[] args) {
        List<String> lines = InputResolver.fetchLinesFromInputFile("input_day1.txt", Day1.class);
        Integer last = null;
        int increaseCount = 0;
        for (String line : lines) {
            int current = Integer.parseInt(line);
            if (last != null && current > last) {
                ++increaseCount;
            }
            last = current;
        }
        System.out.println("Result 1: " + increaseCount);

        List<Data> dataList = new ArrayList<>();
        for (String line : lines) {
            int current = Integer.parseInt(line);
            if (dataList.size() > 0) {
                dataList.getLast().measurements.add(current);
            }
            if (dataList.size() > 1) {
                dataList.get(dataList.size() - 2).measurements.add(current);
            }
            dataList.add(new Data(current));
        }

        AtomicInteger count = new AtomicInteger(0);
        PeekableIterator<Data> iterator = new PeekableIterator<>(dataList);
        while (iterator.hasNext()) {
            Data current = iterator.next();
            iterator.peekPrev().ifPresent(prev -> {
                if (current.isComplete() && prev.isComplete() && current.isMoreThan(prev)) {
                    count.incrementAndGet();
                }
            });
        }
        System.out.println("Result 2: " + count);
    }

    @lombok.Data
    static class Data {
        List<Integer> measurements = new ArrayList<>(3);

        Data(final int firstMeasurement) {
            measurements.add(firstMeasurement);
        }

        public boolean isMoreThan(final Data prev) {
            return measurements.stream().mapToInt(i -> i).sum() > prev.measurements.stream().mapToInt(i -> i).sum();
        }

        boolean isComplete() {
            return measurements.size() == 3;
        }
    }
}

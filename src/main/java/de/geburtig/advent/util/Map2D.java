/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.util;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Die Klasse Map repräsentiert eine zweidimensionale Karte aus Einzelelementen vom generischen Typ T.
 * @author jochen.geburtig
 */
@Data
public class Map2D<T> {

    private final T[][] array;
    private final int width;
    private final int height;

    public Map2D(final T[][] array) {
        this.array = array;
        this.height = array.length;
        this.width = array[0].length;
        forEachRow((rowIdx, row) -> {
            if (row.length != width) {
                throw new IllegalArgumentException("Unexpected row size " + row.length + " in row index " + rowIdx);
            }
        });
    }

    public Map2D(final int width, final int height, final T initValue) {
        this.width = width;
        this.height = height;

        @SuppressWarnings("unchecked")
        T[][] tmp = (T[][]) java.lang.reflect.Array.newInstance(initValue.getClass(), width, height);
        this.array = tmp;
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                array[x][y] = initValue;
            }
        }
    }

    public void print() {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                System.out.print(array[x][y]);
            }
            System.out.println();
        }
    }

    public void forEach(final BiConsumer<Pos, T> consumer) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                consumer.accept(new Pos(x, y), array[x][y]);
            }
        }
    }

    public void forEachRow(final BiConsumer<Integer, T[]> consumer) {
        for (int y = 0; y < array.length; y++) {
            consumer.accept(y, array[y]);
        }
    }

    public T get(final int x, final int y) {
        return array[x][y];
    }

    public T get(final Pos pos) {
        return array[pos.x][pos.y];
    }

    public void set(final Pos pos, final T value) {
        array[pos.x][pos.y] = value;
    }

    public Field<T> getField(final Pos pos) {
        // TODO: Jedesmal neu instanziieren?
        return new Field<>(pos, array[pos.x][pos.y], this);
    }

    public Optional<Field<T>> getFieldIfInBounds(final @NonNull Pos pos) {
        return isInBounds(pos) ? Optional.of(getField(pos)) : Optional.empty();
    }

    public boolean isInBounds(final Pos pos) {
        return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
    }

    public record Pos(int x, int y) {
        public Pos go(final @NonNull Direction direction) {
            return switch (direction) {
                case north -> new Pos(x, y - 1);
                case east -> new Pos(x + 1, y);
                case south -> new Pos(x, y + 1);
                case west -> new Pos(x - 1, y);
            };
        }
    }

    @Data
    @ToString(of = {"pos", "value"})
    public static class Field<T> {
        private final Pos pos;
        private final T value;
        private final Map2D<T> map;

        public Optional<Field<T>> getAdjacent(final Direction direction) {
            return map.getFieldIfInBounds(pos.go(direction));
        }
    }

    public static enum Direction {
        north, east, south, west;
    }
}

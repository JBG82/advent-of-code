package de.geburtig.advent.util;

import java.util.*;

/**
 * Klasse für die Generierung von verschiedenen Verteilungsmöglichkeiten einer festen Zahl von Elementen auf eine
 * bestimmte Zahl von möglichen Positionen. Folgt der Fragestellung: Welche Möglichkeiten habe ich x Elemente auf
 * n Positionen zu verteilen?
 * ------------------------------------
 * Beispiel mit 2 Elementen und 3 Positionen (Ergebnis von NumericPermutations.of(2, 3):
 * (2,0,0)
 * (1,1,0)
 * (1,0,1)
 * (0,2,0)
 * (0,1,1)
 * (0,0,2)
 */
public abstract class NumericPermutations {

    public static final Map<String, List<List<Integer>>> PERMUTATIONS = new HashMap<>();

    public static List<List<Integer>> of(final int elements, final int positions) {
        String key = elements + "," + positions;
        List<List<Integer>> cached = PERMUTATIONS.get(key);
        if (cached != null) {
            return cached;
        }

        List<List<Integer>> result = new ArrayList<>();
        if (positions == 0) {
            throw new RuntimeException("length of 0 not possible");
        } else if (positions == 1) {
            for (int i = elements; i >= 0; --i) {
                result.add(Collections.singletonList(i));
            }
        } else if (positions == 2) {
            for (int i = elements; i >= 0; --i) {
                result.add(List.of(i, elements - i));
            }
        } else {
            for (int i = elements; i >= 0; --i) {
                for (List<Integer> c : NumericPermutations.of(elements - i, positions - 1)) {
                    ArrayList<Integer> l = new ArrayList<>(c);
                    l.addFirst(i);
                    result.add(l);
                }
            }
        }
        PERMUTATIONS.put(key, result);
        return result;
    }
}

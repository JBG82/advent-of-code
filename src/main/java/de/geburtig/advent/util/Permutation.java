package de.geburtig.advent.util;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Erzeugt für eine Liste mit (normalerweise) ungleichen Elementen alle möglichen Permutationen, also alle Listen, die
 * dieselben Elemente in unterschiedlichen Reihenfolgen beinhalten.
 * Auf diese Permutationen kann über folgende zwei Varianten zugegriffern werden:
 * 1.) Mit der Kombination aus hasNext() und next()
 * 2.) Mit Methode forEach(), in diesem Fall wird ein Consumer mitgeliefert, der die Permutationen verwendet.
 * ACHTUNG bei Variante 1: Listen mit 10 Elementen gehen gerade noch, ab 11 Elementen geht der Heap Space in die Knie!
 *
 * @param <T> Typ der übergebenen Liste
 */
public class Permutation<T> {

    private T type;
    private final List<T> current;
    private int requested = 0;
    @Getter
    private final long max;

    private boolean sequenceInitialized = false;
//    private final SeqElem[] switchSequence;
    private final List<Action> switchSequenceList = new ArrayList<>();

    public Permutation(final List<T> values) {
        current = new ArrayList<>(values);
        max = factorial(values.size());

/*
        Action lastAction = initSequence(current, new Action(), current.size());
        if (switchSequenceList.size() < max) {
            throw new RuntimeException("Something's wrong here, " + switchSequenceList.size() + " switch actions for " + max + " permutations!?");
        }
*/

//        System.out.println("Got " + switchSequenceList.size() + " actions");
//        switchSequenceList.forEach(System.out::println);
    }

    private Action initSequence(final List<T> values, Action action, int p) {
        sequenceInitialized = true;
        if (p == 1) {
            switchSequenceList.add(action);
            action = new Action();
        }
        for (int i = 0; i < p; ++i) {
            int a = p % 2 == 1 ? 0 : i;
            int b = p - 1;
            action = initSequence(values, action, b);
            if (a != b) {
                action.addSwitch(a, b);
            }
        }
        return action;
    }

    public long factorial(int x) {
        return x == 1 ? 1 : x * factorial(x - 1);
    }

    public boolean hasNext() {
        if (!sequenceInitialized) {
            initSequence(current, new Action(), current.size());
        }
        return requested < switchSequenceList.size();
    }

    public List<T> next() {
        if (!sequenceInitialized) {
            initSequence(current, new Action(), current.size());
        } else if (requested == switchSequenceList.size()) {
            throw new IllegalStateException("All permutations requested!");
        }
        Action action = switchSequenceList.get(requested++);
        return action.perform();
    }

    public void forEach(Consumer<List<T>> consumer) {
        performForPermutations(current, current.size(), consumer);
    }

    private void performForPermutations(final List<T> values, int p, Consumer<List<T>> consumer) {
        if (p == 1) {
            consumer.accept(new ArrayList<>(values));
        }
        T tmp;
        for (int i = 0; i < p; ++i) {
            int a = p % 2 == 1 ? 0 : i;
            int b = p - 1;
            performForPermutations(values, b, consumer);
            if (a != b) {
                tmp = values.get(a);
                values.set(a, values.get(b));
                values.set(b, tmp);
            }
        }
    }

    @ToString(of = "switches")
    class Action {
        private final List<Switch> switches = new ArrayList<>();

        void addSwitch(int index1, int index2) {
            switches.add(new Switch(index1, index2));
        }

        List<T> perform() {
            switches.forEach(s -> {
                T tmp = current.get(s.a);
                current.set(s.a, current.get(s.b));
                current.set(s.b, tmp);
            });
            return new ArrayList<>(current);
        }
    }

    record Switch(int a, int b) {}
}

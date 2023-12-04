/*
 * Copyright (C) CredaRate Solutions GmbH
 */
package de.geburtig.advent.util;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Iterator mit Zugriff auf nächste und vorhergegangene Elemente.
 *
 * @param <E> Element type
 * @author jochen.geburtig
 */
@RequiredArgsConstructor
public class PeekableIterator<E> implements Iterator<E> {

    private final List<E> list;
    private int index = -1;

    @Override
    public boolean hasNext() {
        return list.size() > index + 1;
    }

    @Override
    public E next() {
        return list.get(++index);
    }

    @Override
    public void remove() {
        list.remove(index--);
    }

    public void removeNext(final int amount) {
        IntStream.range(0, amount).forEach(i -> {
            next();
            remove();
        });
    }

    /**
     * Gibt das nächste kommende Element zurück, ohne den Positionszeiger des Iterators zu verändern.
     * @return Kommendes Element oder Optional.empty, wenn kein entsprechendes Element vorhanden ist
     */
    public Optional<E> peekNext() {
        if (index >= list.size() - 1) {
            return Optional.empty();
        }
        return Optional.of(list.get(index + 1));
    }

    /**
     * Gibt das entsprechend weit vorne liegende Element zurück, ohne den Positionszeiger des Iterators zu verändern.
     * @param offset Zu überspringende Elemente
     * @return Angesteuertes Element oder Optional.empty, wenn kein entsprechendes Element vorhanden ist
     */
    public Optional<E> peekNextPlus(final int offset) {
        if (index + offset >= list.size() - 1) {
            return Optional.empty();
        }
        return Optional.of(list.get(index + offset + 1));
    }

    public E prev() {
        return list.get(--index);
    }

    public E peekPrev() {
        return list.get(index - 1);
    }

    public E peekPrevPlus(final int offset) {
        return list.get(index - offset - 1);
    }

    public int elementsLeft() {
        return list.size() - (index + 1);
    }

    public void skip(final int offset) {
        index += offset;
    }

    public void replace(final E element) {
        list.set(index, element);
    }

    /**
     * Fügt ein Element an der aktuellen Stelle ein und rückt alle anderen Elemente einen Platz weiter nach hinten.
     * Der Iterator verhält sich so, als wäre das eingefügte Element mit dem letzten next() bezogen worden.
     * Der nächste Aufruf von next() liefert also das aktuelle, nach hinten geschobene Element, erneut.
     * @param element Element
     */
    public void insert(final E element) {
        list.add(index, element);
    }
}

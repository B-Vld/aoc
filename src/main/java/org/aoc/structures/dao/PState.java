package org.aoc.structures.dao;

public record PState(Quantity pouch, Quantity robots) {
    public PState(State s) {
        this(s.pouch(), s.robots());
    }
}

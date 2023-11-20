package org.aoc.structures.dao;

import org.aoc.enums.Mineral;
import org.jetbrains.annotations.NotNull;

public record State(Quantity pouch, Quantity robots, int minute) implements Comparable<State> {
    public State buyAndProduce(Blueprint bp, Mineral robot) {
        Quantity nPouch, nRobots;
        switch (robot) {
            case ORE -> {
                nPouch = new Quantity(
                        this.pouch().ore() + this.robots().ore() - bp.cOre(),
                        this.pouch().clay() + this.robots().clay(),
                        this.pouch().obsidian() + this.robots().obsidian(),
                        this.pouch().geode() + this.robots().geode());
                nRobots = new Quantity(
                        this.robots().ore() + 1,
                        this.robots().clay(),
                        this.robots().obsidian(),
                        this.robots().geode()
                );
            }
            case CLAY -> {
                nPouch = new Quantity(
                        this.pouch().ore() + this.robots().ore() - bp.cClay(),
                        this.pouch().clay() + this.robots().clay(),
                        this.pouch().obsidian() + this.robots().obsidian(),
                        this.pouch().geode() + this.robots().geode());
                nRobots = new Quantity(
                        this.robots().ore(),
                        this.robots().clay() + 1,
                        this.robots().obsidian(),
                        this.robots().geode()
                );
            }
            case OBSIDIAN -> {
                nPouch = new Quantity(
                        this.pouch().ore() + this.robots().ore() - bp.cObsidian().getLeft(),
                        this.pouch().clay() + this.robots().clay() - bp.cObsidian().getRight(),
                        this.pouch().obsidian() + this.robots().obsidian(),
                        this.pouch().geode() + this.robots().geode());
                nRobots = new Quantity(
                        this.robots().ore(),
                        this.robots().clay(),
                        this.robots().obsidian() + 1,
                        this.robots().geode()
                );
            }
            case GEODE -> {
                nPouch = new Quantity(
                        this.pouch().ore() + this.robots().ore() - bp.cGeode().getLeft(),
                        this.pouch().clay() + this.robots().clay(),
                        this.pouch().obsidian() + this.robots().obsidian() - bp.cGeode().getRight(),
                        this.pouch().geode() + this.robots().geode());
                nRobots = new Quantity(
                        this.robots().ore(),
                        this.robots().clay(),
                        this.robots().obsidian(),
                        this.robots().geode() + 1
                );
            }
            default -> {
                nPouch = new Quantity(
                        this.pouch().ore() + this.robots().ore(),
                        this.pouch().clay() + this.robots().clay(),
                        this.pouch().obsidian() + this.robots().obsidian(),
                        this.pouch().geode() + this.robots().geode());
                nRobots = new Quantity(
                        this.robots().ore(),
                        this.robots().clay(),
                        this.robots().obsidian(),
                        this.robots().geode()
                );
            }
        }
        return new State(nPouch, nRobots, this.minute() + 1);
    }

    @Override
    public int compareTo(@NotNull State o) {
        return -Integer.compare(this.robots.countAll(), o.robots.countAll());
//        return this.robots.countAll() > o.robots().countAll() ? 1 : -1;
////        return Comparator.comparingInt(State::minute)
////                .thenComparing(e -> e.robots.geode())
////                .thenComparing(e -> e.robots.obsidian())
////                .thenComparing(e -> e.robots.clay())
////                .thenComparing(e -> e.robots.ore())
////                .compare(this, o);
    }

}

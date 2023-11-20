package org.aoc.structures.dao;

import org.aoc.enums.Mineral;

public record Quantity(int ore, int clay, int obsidian, int geode) {
    public boolean canBuy(int timeLeft, Quantity robots, Blueprint bp, Mineral m) {
        switch (m) {
            case ORE -> {
                return (
                        robots.ore() * timeLeft + this.ore < timeLeft * bp.maxOre()) &&
                        this.ore >= bp.cOre();
            }
            case CLAY -> {
                return (
                        robots.clay() * timeLeft + this.clay < timeLeft * bp.maxClay()) &&
                        this.ore >= bp.cClay();
            }
            case OBSIDIAN -> {
                return (
                        robots.obsidian() * timeLeft + this.obsidian < timeLeft * bp.maxObsidian()) &&
                        this.ore >= bp.cObsidian().getLeft() && this.clay >= bp.cObsidian().getRight();
            }
            case GEODE -> {
                return this.ore >= bp.cGeode().getLeft() && this.obsidian >= bp.cGeode().getRight();
            }
            default -> throw new RuntimeException("Shouldn't reach this statement");
        }
    }
    public int countAll() {
        return this.ore + this.clay + this.obsidian + this.geode;
    }

}


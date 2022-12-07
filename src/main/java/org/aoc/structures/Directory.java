package org.aoc.structures;

public class Directory {

    private final String name;
    private int size;

    public Directory(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

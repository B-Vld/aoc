package org.aoc.structures.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryTree {

    private final List<DirectoryTree> children = new ArrayList<>();
    private final Directory data;
    private DirectoryTree parent = null;

    public DirectoryTree(Directory data) {
        this.data = data;
    }

    public static int sizeOfNode(DirectoryTree tree) {
        int result = tree.getData().getSize();
        for (var child : tree.getChildren()) {
            result += sizeOfNode(child);
        }
        return result;
    }

    public Directory getData() {
        return data;
    }

    public List<DirectoryTree> getChildren() {
        return children;
    }

    public void addChild(DirectoryTree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public DirectoryTree addChild(Directory data) {
        DirectoryTree child = new DirectoryTree(data);
        this.addChild(child);
        return child;
    }

    public Optional<DirectoryTree> getChildByName(String name) {
        for (var child : children) {
            if (child.getData().getName().equals(name))
                return Optional.of(child);
        }
        return Optional.empty();
    }

    public DirectoryTree getParent() {
        return parent;
    }

    public void setParent(DirectoryTree parent) {
        this.parent = parent;
    }

}

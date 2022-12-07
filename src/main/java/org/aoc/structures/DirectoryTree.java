package org.aoc.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DirectoryTree {

    private Directory data;
    private List<DirectoryTree> children = new ArrayList<>();
    private DirectoryTree parent = null;

    public DirectoryTree(Directory data) {
        this.data = data;
    }

    public Directory getData() {
        return data;
    }

    public int getSize() {
        return data.getSize();
    }

    public int getTotalSize() {
        int result = getSize();
        for(var child : children) {
            result+=child.getSize();
        }
        return result;
    }

    public void setData(Directory data) {
        this.data = data;
    }

    public List<DirectoryTree> getChildren() {
        return children;
    }

    public void addChild(DirectoryTree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Directory data) {
        DirectoryTree child = new DirectoryTree(data);
        this.addChild(child);
    }

    public void addChildren(List<DirectoryTree> children) {
        for(DirectoryTree t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public Optional<DirectoryTree> getChildByName(String name) {
        for(var child : children) {
            if(child.getData().getName().equals(name))
                return Optional.of(child);
        }
        return Optional.empty();
    }

    public static int sizeOfAllSubtrees(DirectoryTree tree) {
        int result = tree.getData().getSize();
        for(var child : tree.getChildren()) {
            result += sizeOfAllSubtrees(child);
        }
        return result;
    }

    public DirectoryTree getParent() {
        return parent;
    }

    public void setParent(DirectoryTree parent) {
        this.parent = parent;
    }

}

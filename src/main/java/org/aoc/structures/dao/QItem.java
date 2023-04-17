package org.aoc.structures.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QItem {

    public int row;
    public int col;
    public int dist;
    public List<Position> path;

}

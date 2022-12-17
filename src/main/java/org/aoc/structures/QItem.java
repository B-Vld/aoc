package org.aoc.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aoc.utils.Position;

import java.util.List;

@Data
@AllArgsConstructor
public class QItem {

    public int row;
    public int col;
    public int dist;
    public List<Position> path;

}

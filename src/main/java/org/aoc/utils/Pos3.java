package org.aoc.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pos3 {

    private int x;
    private int y;
    private int z;

    public static Pos3 of(String a, String b, String c) {
        return new Pos3(Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(c));
    }

    public static Pos3 of(int a, int b, int c) {
        return new Pos3(a, b, c);
    }


    public static List<Pos3> neighbors(Pos3 p) {
        return List.of(
                Pos3.of(p.x - 1, p.y, p.z),
                Pos3.of(p.x + 1, p.y, p.z),
                Pos3.of(p.x, p.y - 1, p.z),
                Pos3.of(p.x, p.y + 1, p.z),
                Pos3.of(p.x, p.y, p.z - 1),
                Pos3.of(p.x, p.y, p.z + 1)
        );
    }

}

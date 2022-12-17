package org.aoc.structures;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
public class Mon {
    private LinkedList<Long> items;
    private Function<Long, Long> operation;
    private Predicate<Long> predicate;
    private long inspected;

    public void round(Map<Integer, Mon> monMap, int monIdx, long dWlvl, long lcm) {
        var mon = monMap.get(monIdx);
        while (!mon.items.isEmpty()) {
            var itm = mon.items.removeFirst();
            var wLvl = lcm == 1L ? Long.divideUnsigned(this.operation.apply(itm), dWlvl) :          // part 1
                                         Long.divideUnsigned(this.operation.apply(itm), dWlvl) % lcm;     // part 2
            this.inspected++;
            if (this.predicate.test(wLvl)) {
                switch (monIdx) {
                    case 0, 3 -> monMap.get(2).items.addLast(wLvl);
                    case 1, 7 -> monMap.get(4).items.addLast(wLvl);
                    case 2 -> monMap.get(5).items.addLast(wLvl);
                    case 4 -> monMap.get(0).items.addLast(wLvl);
                    case 5, 6 -> monMap.get(7).items.addLast(wLvl);
                }
            } else {
                switch (monIdx) {
                    case 0, 4 -> monMap.get(3).items.addLast(wLvl);
                    case 1 -> monMap.get(0).items.addLast(wLvl);
                    case 2, 3 -> monMap.get(6).items.addLast(wLvl);
                    case 5, 7 -> monMap.get(1).items.addLast(wLvl);
                    case 6 -> monMap.get(5).items.addLast(wLvl);
                }
            }
        }
    }
}

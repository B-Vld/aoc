package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.dao.Blueprint;
import org.aoc.structures.dao.Quantity;
import org.aoc.structures.dao.State;
import org.aoc.utils.Input;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

import static org.aoc.enums.Mineral.*;

@Slf4j
@Day(day = 19)
public class Day19 implements Challenge {
    @Override
    public void firstChallenge(String fileName) {
        var result = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> Arrays.stream(str.split("\\D+")).filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray())
                .map(Blueprint::toBlueprint)
                .map(e -> solve(e, 24))
                .mapToInt(i -> i.getLeft() * i.getRight())
                .sum();

        log.info("Day 19 first challenge: {}", result);
    }

    @Override
    public void secondChallenge(String fileName) {
        var result = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> Arrays.stream(str.split("\\D+")).filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray())
                .map(Blueprint::toBlueprint)
                .map(e -> solve(e, 32))
                .mapToInt(i -> i.getLeft() * i.getRight())
                .sum();

        log.info("Day 19 second challenge: {}", result);
    }

    private Pair<Integer, Integer> solve(Blueprint bp, int time) {
        Queue<State> queue = new PriorityQueue<>(State::compareTo);
        Set<State> seen = new HashSet<>();
        Set<Integer> geodes = new HashSet<>();
        queue.add(new State(
                new Quantity(0, 0, 0, 0),
                new Quantity(1, 0, 0, 0),
                1
        ));
        while (!queue.isEmpty()) {
            var state = queue.poll();
            int timeLeft = time - state.minute();
            if (!seen.contains(state)) {
                seen.add(state);
            } else continue;
            if (timeLeft == 0) {
                geodes.add(state.pouch().geode() + state.robots().geode());
                continue;
            }

            if (state.pouch().canBuy(timeLeft, state.robots(), bp, GEODE)) {
                var nState = state.buyAndProduce(bp, GEODE);
                queue.add(nState);
                continue;
            }
            if (state.pouch().canBuy(timeLeft, state.robots(), bp, OBSIDIAN)) {
                var nState = state.buyAndProduce(bp, OBSIDIAN);
                queue.add(nState);
            }
            if (state.pouch().canBuy(timeLeft, state.robots(), bp, CLAY)) {
                var nState = state.buyAndProduce(bp, CLAY);
                queue.add(nState);
            }
            if (state.pouch().canBuy(timeLeft, state.robots(), bp, ORE)) {
                var nState = state.buyAndProduce(bp, ORE);
                queue.add(nState);
            }
            var nState = state.buyAndProduce(bp, NONE);
            queue.add(nState);
        }
        return Pair.of(bp.bIdx(), geodes.stream().mapToInt(i -> i).max().orElse(0));
    }

}

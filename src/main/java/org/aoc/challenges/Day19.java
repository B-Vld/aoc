package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.dao.Blueprint;
import org.aoc.structures.dao.PState;
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
                .limit(3)
                .map(str -> Arrays.stream(str.split("\\D+")).filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).toArray())
                .map(Blueprint::toBlueprint)
                .map(e -> solve(e, 32))
                .mapToInt(Pair::getRight)
                .reduce(1, (left, right) -> left * right);

        log.info("Day 19 second challenge: {}", result);
    }

    private Pair<Integer, Integer> solve(Blueprint bp, int time) {
        Queue<State> queue = new PriorityQueue<>(State::compareTo);
        Map<PState, Integer> seen = new HashMap<>();
        int sumGeodeAcquisitionMinute = 0, countFirstGeodeAcquisition = 0;
        Set<Integer> geodes = new HashSet<>();
        queue.add(new State(
                new Quantity(0, 0, 0, 0),
                new Quantity(1, 0, 0, 0),
                1
        ));
        while (!queue.isEmpty()) {
            var state = queue.poll();
            var pState = new PState(state);
            int timeLeft = time - state.minute();
            if (countFirstGeodeAcquisition != 0) {
                if (state.minute() > (sumGeodeAcquisitionMinute / countFirstGeodeAcquisition) && state.robots().geode() == 0) {
                    continue;
                }
            }
            if (!seen.containsKey(pState)) {
                seen.put(pState, timeLeft);
            } else {
                if (seen.get(pState) >= timeLeft) {
                    continue;
                } else {
                    seen.replace(pState, timeLeft);
                }
            }
            if (timeLeft == 0) {
                int finalGeodes = state.pouch().geode() + state.robots().geode();
                geodes.add(finalGeodes);
                continue;
            }

            if (state.pouch().canBuy(timeLeft, state.robots(), bp, GEODE)) {
                var nState = state.buyAndProduce(bp, GEODE);
                if (state.robots().geode() == 0) {
                    countFirstGeodeAcquisition++;
                    sumGeodeAcquisitionMinute += state.minute() + 1;
                }
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

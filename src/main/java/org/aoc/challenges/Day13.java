package org.aoc.challenges;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.utils.Input;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Day(day = 13)
public class Day13 implements Challenge {
    @Override
    public void firstChallenge(String fileName) {
        var result = 0;
        var input = Input.partitionList(Input.allLinesDefaultDelimiter(fileName)
                        .filter(str -> !str.isEmpty())
                        .map(this::parsePacket)
                        .toList(), 2)
                .stream()
                .toList();

        for(int i = 0; i<input.size(); i++) {
            var pair = input.get(i);
            if(compare(pair.get(0), pair.get(1)) <= 0) {
                result += i + 1;
            }
        }

        log.info("Day 13 first challenge : {}", result);
    }

    @Override
    public void secondChallenge(String fileName) {

    }

    private List<Object> parsePacketFromJsonArray(JsonArray elements) {
        var items = new ArrayList<>();

        elements.forEach(e -> {
            if (e.isJsonArray())
                items.add(parsePacketFromJsonArray(e.getAsJsonArray()));
            else
                items.add(e.getAsInt());
        });

        return items;
    }

    private List<Object> parsePacket(String inputLine) {
        var json = JsonParser.parseString(inputLine);
        return parsePacketFromJsonArray(json.getAsJsonArray());
    }

    private int compare(Object left, Object right) {
        if (left instanceof Integer leftInt && right instanceof Integer rightInt)
            return leftInt - rightInt;

        // If left list runs out of items first, it is in the right order.
        if (left instanceof List<?> leftList && right instanceof List<?> rightList) {
            if (leftList.isEmpty())
                return rightList.isEmpty() ? 0 : -1;

            var minSize = Math.min(leftList.size(), rightList.size());

            for (int i = 0; i < minSize; i++) {
                int check = compare(leftList.get(i), rightList.get(i));
                if (check != 0) return check;
            }

            // all elements matched.
            return leftList.size() - rightList.size();
        }

        // otherwise one side is an int and the other is a list.
        if (left instanceof Integer leftInt)
            return compare(List.of(leftInt), right);

        if (right instanceof Integer rightInt)
            return compare(left, List.of(rightInt));

        throw new RuntimeException("Should not happen");
    }

}

package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.Directory;
import org.aoc.structures.DirectoryTree;
import org.aoc.utils.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Day(day = 7)
@Slf4j
public class Day7 implements Challenge {

    private static final List<DirectoryTree> nodesHolder = new ArrayList<>();
    private static DirectoryTree ROOT;
    private final Pattern NUMERIC = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public void firstChallenge(String fileName) {

        var input = Input.allLinesDefaultDelimiter(fileName)
                .map(str -> str.split(" "))
                .toList();
        var rootAsInput = input.get(0);


        ROOT = new DirectoryTree(new Directory(rootAsInput[2], 0));
        var currentDirectory = ROOT;
        nodesHolder.add(ROOT);

        for (var i = 1; i < input.size(); i++) {
            var line = input.get(i);
            if (line[0].equals("$")) {
                if (line[1].equals("ls")) {
                    continue;
                }
                if (line[1].equals("cd")) {
                    if (line[2].equals("..")) {
                        currentDirectory = currentDirectory.getParent();
                    } else {
                        var exists = currentDirectory.getChildByName(line[2]);
                        if (exists.isPresent()) {
                            currentDirectory = exists.get();
                        }
                    }
                }
            }
            if (line[0].equals("dir")) {
                var exists = currentDirectory.getChildByName(line[1]);
                if (exists.isPresent()) {
                    currentDirectory = exists.get();
                } else {
                    nodesHolder.add(currentDirectory.addChild(new Directory(line[1], 0)));
                }
            }
            if (isNumeric(line[0])) {
                var newSize = currentDirectory.getData().getSize() + Integer.parseInt(line[0]);
                currentDirectory.getData().setSize(newSize);
            }
        }

        var result = nodesHolder.stream()
                .map(DirectoryTree::sizeOfNode)
                .filter(size -> size < 100_000)
                .mapToInt(i -> i)
                .sum();

        log.info("Day 7 first challenge : {}", result);

    }

    @Override
    public void secondChallenge(String fileName) {
        var spaceNeeded = DirectoryTree.sizeOfNode(ROOT) - 40_000_000;
        var result = nodesHolder.stream()
                .map(DirectoryTree::sizeOfNode)
                .filter(i -> i >= spaceNeeded)
                .mapToInt(i -> i)
                .sorted()
                .limit(1)
                .findFirst()
                .orElse(-1);

        log.info("Day 7 second challenge {}", result);
    }


    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMERIC.matcher(strNum).matches();
    }

}

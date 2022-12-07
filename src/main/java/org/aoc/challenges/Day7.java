package org.aoc.challenges;

import lombok.extern.slf4j.Slf4j;
import org.aoc.Challenge;
import org.aoc.annotations.Day;
import org.aoc.structures.Directory;
import org.aoc.structures.DirectoryTree;
import org.aoc.utils.Input;

import java.util.regex.Pattern;

@Day(day = 7)
@Slf4j
public class Day7 implements Challenge {

    private static DirectoryTree ROOT;
    private final Pattern NUMERIC = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public void firstChallenge(String fileName) {

        var input = Input.allLinesDefaultDelimiter("day7.txt")
                .map(str -> str.split(" "))
                .toList();
        var rootAsInput = input.get(0);


        ROOT = new DirectoryTree(new Directory(rootAsInput[2], 0));
        var currentDirectory = ROOT;

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
                        } else {
                            currentDirectory.addChild(new Directory(line[3], 0));
                        }
                    }
                }
            }
            if (line[0].equals("dir")) {
                var exists = currentDirectory.getChildByName(line[1]);
                if (exists.isPresent()) {
                    currentDirectory = exists.get();
                } else {
                    currentDirectory.addChild(new Directory(line[1], 0));
                }
            }
            if (isNumeric(line[0])) {
                var newSize = currentDirectory.getData().getSize() + Integer.parseInt(line[0]);
                currentDirectory.getData().setSize(newSize);
            }
        }


    }

    @Override
    public void secondChallenge(String fileName) {

    }


    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return NUMERIC.matcher(strNum).matches();
    }

}

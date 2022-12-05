package org.aoc.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Input {

    private static final String DEFAULT_LINE_DELIMITER = "\r\n";
    private static final String CONTENT_ROOT = "src/main/resources";

    public static Stream<String> allLinesDefaultDelimiter(String fileName) {
        return Arrays.stream(readAsString(fileName).split(DEFAULT_LINE_DELIMITER));
    }

    public static String readAsString(String name) {
        try {
            return Files.readString(Path.of(CONTENT_ROOT, name), Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Stream<String> readString(String name, String lineDelimiter) {
        return Arrays.stream(readAsString(name).split(lineDelimiter));
    }

    public static Stream<String> readAsString(String name, String lineDelimiter) {
        return Arrays.stream(readAsString(name).split(lineDelimiter));
    }

    public static Collection<List<String>> readFilePartitionedBySize(String name, int size) {
        var input = readAsString(name, DEFAULT_LINE_DELIMITER).toList();
        return IntStream.range(0, input.size())
                .boxed()
                .collect(Collectors.groupingBy(partition -> (partition / size), Collectors.mapping(input::get, Collectors.toList())))
                .values();
    }

    public static List<String> readFirstXLines(String name, int x) {
        return readAsString(name, "\n")
                .toList()
                .stream()
                .limit(x)
                .toList();
    }

    public static List<String> readSkipXLines(String name, int x) {
        return readAsString(name, "\n")
                .toList()
                .stream()
                .skip(x)
                .toList();
    }


    public static Collection<Pair<String, String>> readFilePartitionedByDelimiter(String name, String delimiter) {
        return readAsString(name, DEFAULT_LINE_DELIMITER)
                .toList()
                .stream()
                .map(str -> str.split(delimiter))
                .map(strings -> Pair.of(strings[0], strings[1]))
                .toList();
    }


}

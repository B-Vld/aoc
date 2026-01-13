import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
PART2 implementation is 100% based on this idea: 
https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
*/
record Input(IndicatorLightDiagram indicatorLightDiagram, List<int[]> buttonWiringSchematics,
                JoltageRequirement joltageRequirement) {
}

record ButtonWiringSchematicBitMask(int mask, int presses) {
}

record IndicatorLightDiagram(int mask, int size) {
}

record JoltageRequirement(int mask, int[] joltages) {
}

final static int INF = 1_000_000;

List<Input> input(String inputPath) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(inputPath))) {
                return lines.map(l -> l.split(" "))
                                .map(l -> {
                                        var ild = IntStream.range(0, l[0].length())
                                                        .filter(i -> l[0].charAt(i) == '#')
                                                        .map(i -> 1 << (i - 1))
                                                        .reduce(0, (a, b) -> a | b);
                                        var bws = Arrays.asList(l).subList(1, l.length - 1).stream()
                                                        .map(u -> u.replace("(", "").replace(")", ""))
                                                        .map(u -> u.split(","))
                                                        .map(u -> Arrays.asList(u).stream()
                                                                        .mapToInt(Integer::parseInt)
                                                                        .toArray())
                                                        .toList();
                                        var jr = Arrays.asList(l[l.length - 1]).stream()
                                                        .map(u -> u.replace("{", "").replace("}", ""))
                                                        .map(u -> u.split(","))
                                                        .flatMap(u -> Arrays.stream(u))
                                                        .mapToInt(Integer::parseInt)
                                                        .toArray();
                                        return new Input(new IndicatorLightDiagram(ild, l[0].length() - 2), bws,
                                                        new JoltageRequirement(toJoltageMask(jr), jr));
                                })
                                .toList();
        }
}

int minButtonPressToJoltageCounter(Map<List<Integer>, Integer> memo, List<int[]> bws, JoltageRequirement jr, int min) {
        List<Integer> memoKey = Arrays.stream(jr.joltages()).boxed().toList();
        if (memo.containsKey(memoKey)) {
                return memo.get(memoKey);
        }
        if (Arrays.stream(jr.joltages()).allMatch(j -> j == 0)) {
                return 0;
        }
        List<ButtonWiringSchematicBitMask> bMasks = bitMaskMatchingJoltageRequirementPattern(bws, jr);
        if (bMasks.size() == 0) {
                return INF;
        }
        for (ButtonWiringSchematicBitMask bm : bMasks) {
                List<Integer> rem = normalizeJoltages(bws, bm, jr.joltages());
                if (rem.stream().anyMatch(x -> x < 0)) {
                        continue;
                }
                if (rem.stream().allMatch(x -> x == 0)) {
                        min = Math.min(min, bm.presses());
                        continue;
                }
                if (!rem.stream().allMatch(x -> (x % 2) == 0)) {
                        continue;
                }
                int[] half = rem.stream().mapToInt(x -> x / 2).toArray();
                JoltageRequirement next = new JoltageRequirement(toJoltageMask(half), half);
                int sub = minButtonPressToJoltageCounter(memo, bws, next, INF);
                if (sub >= INF) {
                        continue;
                }
                int candidate = bm.presses() + 2 * sub;
                min = Math.min(min, candidate);
        }
        memo.put(memoKey, min);
        return min;
}

List<Integer> normalizeJoltages(List<int[]> bws, ButtonWiringSchematicBitMask bm, int[] joltages) {
        int[] res = Arrays.copyOf(joltages, joltages.length);
        int mask = bm.mask();
        while (mask != 0) {
                int pos = Integer.numberOfTrailingZeros(mask);
                int[] buttons = bws.get(pos);
                for (int button : buttons) {
                        res[button]--;
                }
                mask &= (mask - 1);
        }
        return Arrays.stream(res).boxed().toList();
}

List<ButtonWiringSchematicBitMask> bitMaskMatchingJoltageRequirementPattern(List<int[]> bws, JoltageRequirement jr) {
        int total = 1 << bws.size();
        int[] bitMaskBWS = bitMaskWiringSchematics(bws);
        List<ButtonWiringSchematicBitMask> bitMasks = new ArrayList<>();
        for (int mask = 0; mask < total; mask++) {
                int nmask = mask, target = 0;
                while (nmask != 0) {
                        int pos = Integer.numberOfTrailingZeros(nmask);
                        target ^= bitMaskBWS[pos];
                        nmask &= (nmask - 1);
                }
                if (target == jr.mask()) {
                        bitMasks.add(new ButtonWiringSchematicBitMask(mask, Integer.bitCount(mask)));
                }
        }
        return bitMasks;
}

int toJoltageMask(int[] joltages) {
        return IntStream.range(0, joltages.length)
                        .filter(i -> joltages[i] % 2 == 1)
                        .map(i -> 1 << i)
                        .reduce(0, (a, b) -> a | b);
}

int[] bitMaskWiringSchematics(List<int[]> buttonWiringSchematics) {
        int bwsSize = buttonWiringSchematics.size();
        int[] buttonMasks = new int[bwsSize];
        for (int j = 0; j < bwsSize; j++) {
                int m = 0;
                for (int ildPos : buttonWiringSchematics.get(j)) {
                        m |= (1 << ildPos);
                }
                buttonMasks[j] = m;
        }
        return buttonMasks;
}

int findFewestButtonPresses(Input line) {
        int[] buttonMasks = bitMaskWiringSchematics(line.buttonWiringSchematics());
        int target = line.indicatorLightDiagram().mask();
        Deque<ButtonWiringSchematicBitMask> q = new ArrayDeque<>();
        q.add(new ButtonWiringSchematicBitMask(0, 0));
        boolean[] visited = new boolean[1 << line.indicatorLightDiagram().size()];
        visited[0] = true;
        while (!q.isEmpty()) {
                ButtonWiringSchematicBitMask current = q.pollFirst();
                if (current.mask() == target) {
                        return current.presses();
                }
                for (int bm : buttonMasks) {
                        int next = current.mask() ^ bm;
                        if (!visited[next]) {
                                visited[next] = true;
                                q.addLast(new ButtonWiringSchematicBitMask(next, current.presses() + 1));
                        }
                }
        }
        return 0;
}

void main() throws IOException {
        Instant start = Instant.now();
        List<Input> in = input("10/input.txt");
        int part1 = in.stream().mapToInt(l -> findFewestButtonPresses(l)).sum();
        IO.println(part1);
        int part2 = in.parallelStream()
                        .mapToInt(l -> minButtonPressToJoltageCounter(new ConcurrentHashMap<>(),
                                        l.buttonWiringSchematics(),
                                        l.joltageRequirement(), Integer.MAX_VALUE))
                        .sum();
        IO.println(part2);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        IO.println("Duration: " + duration.toMillis() + " ms");
}
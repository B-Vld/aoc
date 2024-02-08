import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2022/src/main/resources/16-example.txt';
const FP_ACTUAL = './aoc2022/src/main/resources/16.txt';

class State {
    constructor(node, minute, visited, path, rate, flowRate) {
        this.node = node;
        this.minute = minute;
        this.visited = visited;
        this.path = path;
        this.rate = rate;
        this.flowRate = flowRate;
    }
}

function getInput(path) {
    const resMap = new Map();
    const map = new Map(readFileSync(path).toString().split('\r\n')
        .map(l => l.split(" "))
        .map(l => ({
            key: l[1],
            rate: parseInt(l[4].match(/-?\d+/g)[0]),
            valves: l.slice(9, l.length).map(e => e.replace(',', ''))
        }))
        .map(e => {
            return [e.key, { rate: e.rate, valves: e.valves }];
        }));
    const relevantNodes = new Set([...map].filter(e => e[1].rate > 0).map(e => e[0]));
    relevantNodes.add('AA');
    relevantNodes.forEach(rv => {
        resMap.set(rv, ({
            rate: map.get(rv).rate,
            paths: bfsDistances(rv, map, relevantNodes).filter(d => d.node !== 'AA' && d.distance > 0)
        }));
    });
    return resMap;
}

function bfsDistances(cNode, iMap, relevantNodes) {
    const distances = [], states = [], visited = new Set(), cState = { node: cNode, distance: 0 };
    states.push(cState);
    while (distances.length !== relevantNodes.size) {
        let c = states.shift();
        if (visited.has(c.node)) {
            continue;
        } else {
            visited.add(c.node);
            if (relevantNodes.has(c.node)) {
                distances.push(c);
            }
        }
        iMap.get(c.node).valves.forEach(nV => {
            states.push({
                node: nV,
                distance: c.distance + 1,
                flowRate: iMap.get(nV).rate
            });
        });
    }
    return distances;
}

function solve(map, maxMinute) {
    const state = new State('AA', 1, new Set(), [], 0, 0), states = [], rz = [], cache = new Set();
    const results = new Set();
    const max_relieved_states = new Map();
    states.push(state);
    while (states.length > 0) {
        const curr = states.pop();
        const currStr = JSON.stringify({
            node: curr.node,
            minute: curr.minute,
            visited: [...curr.visited]
        });
        cache.add(currStr);
        let nextFlowRate = curr.rate * (maxMinute - curr.minute + 1);
        if ((curr.visited.size >= map.size - 1) ||
            (curr.minute + map.get(curr.node).paths[0].distance > maxMinute)
        ) {
            results.add(curr.flowRate + nextFlowRate);
            rz.push({
                flowRate: curr.flowRate + nextFlowRate,
                visited: [...curr.visited],
                path: curr.path
            });
        } else {
            const allPaths = map.get(curr.node).paths.filter(p => !curr.visited.has(p.node));
            for (let path of allPaths) {
                nextFlowRate = curr.rate * (path.distance + 1);
                const next = new State(
                    path.node,
                    curr.minute + path.distance + 1,
                    new Set(curr.visited),
                    [...curr.path],
                    curr.rate + path.flowRate,
                    curr.flowRate + nextFlowRate);

                if (next.minute <= maxMinute) {
                    next.visited.add(path.node);
                    next.path.push({
                        node: path.node,
                        minute: curr.minute + path.distance + 1,
                        flowRate: path.flowRate
                    });
                    if (!cache.has(JSON.stringify({
                        node: next.node,
                        minute: next.minute,
                        visited: [...next.visited]
                    }))) {
                        states.push(next);
                    }
                } else {
                    nextFlowRate = curr.rate * (maxMinute - curr.minute + 1);
                    results.add(curr.flowRate + nextFlowRate);
                }
            }
        }
    }
    return [Math.max(...[...results]), rz];
}

function part1(input) {
    console.log(solve(getInput(input), 30)[0]);
}

function part2(input) {

}

part1(FP_ACTUAL);
part1(FP_EXAMPLE);
// part2(FP_ACTUAL);
// part2(FP_EXAMPLE);
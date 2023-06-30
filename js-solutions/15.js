import { readFileSync } from "fs";

const FP_EXAMPLE = './src/input/15-example.txt';
const FP_ACTUAL = './src/input/15.txt';

function getInput(path) {
    return readFileSync(path).toString().split('\r\n')
        .map(line => line.match(/-?\d+/g))
        .map(line => ({
            sensor: {
                x: parseInt(line[0]),
                y: parseInt(line[1])
            },
            beacon: {
                x: parseInt(line[2]),
                y: parseInt(line[3])
            }
        }))
        .map(inp => ({
            ...inp,
            distance: manhattan(inp.sensor, inp.beacon)
        }))
        .map(inp => ({
            ...inp,
            down: { x: inp.sensor.x, y: inp.sensor.y + inp.distance },
            up: { x: inp.sensor.x, y: inp.sensor.y - inp.distance }
        }));
}

function checkRowFill(nRow, sensor, maxRow, isPart1) {
    const start = sensor.up.y, dest = sensor.down.y;
    const sX = sensor.sensor.x, sY = sensor.sensor.y;

    if (nRow >= start && nRow <= dest) {
        let xLeft = 0, xRight = 0, by = sensor.distance;

        if (nRow < sY) {
            by = nRow - start;
        }
        if (nRow > sY) {
            by = dest - nRow;
        }

        xLeft = sX - by;
        xRight = sX + by;

        return isPart1 ?
            [xLeft, xRight] :
            [xLeft < 0 ? 0 : xLeft, xRight < maxRow ? xRight : maxRow];
    }

    return [];
}

function gatherSegments(sensors, nRow) {
    let intervals = [];
    for (let i = 0; i <= nRow; i++) {
        sensors.forEach(sensor =>
            intervals.push(checkRowFill(i, sensor, nRow, false))
        );
        const x = consultIntervals(intervals.filter((arr) => arr.length === 2).sort((a, b) => a[0] - b[0]));
        if (x !== 0)
            return (x * 4_000_000) + i;
        intervals = [];
    }
    return 0;
}

function manhattan(sensor, beacon) {
    return Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
}

function part1(path, nRow) {
    const input = getInput(path);
    const intervals = [];
    input.forEach((sensor) => intervals.push(checkRowFill(nRow, sensor, 0, true)));
    const result = mergeIntervals(intervals.filter((i) => i.length > 0).sort((a, b) => a[0] - b[0]));
    console.log(result.map(arr => arr[1] - arr[0]).reduce((acc, cv) => acc + cv, 0));
}

function part2(path, nRow) {
    const input = getInput(path);
    let res = 0;
    res = gatherSegments(input, nRow);
    console.log(res);
}

function consultIntervals(sortedPoints) {
    const arr = [];
    for (let i = 0; i < sortedPoints.length; i++) {
        const elem = sortedPoints[i];
        if (arr.length === 0) {
            arr.push(elem);
        } else {
            if (elem[0] <= arr[0][1]) {
                if (elem[1] > arr[0][1]) {
                    arr[0][1] = elem[1];
                }
            } else {
                if (elem[0] === arr[0][1] + 1) {
                    arr[0][1] = elem[1]
                } else {
                    return arr[0][1] + 1
                }
            }
        }
    }
    return 0;
}

function mergeIntervals(intervals) {
    const arr = [];
    for (let interval of intervals) {
        if (arr.length === 0) {
            arr.push(interval);
        } else {
            const fE = interval[0], sE = interval[1], iArr = arr[arr.length - 1];
            const iSe = iArr[1];
            if (fE <= (iSe + 1)) {
                iArr[1] = sE > iSe ? sE : iSe;
            } else {
                arr.push(interval);
            }
        }
    }
    return arr;
}

// part1(FP_EXAMPLE, 20);
// part2(FP_EXAMPLE, 20);
part1(FP_ACTUAL, 2000000);
part2(FP_ACTUAL, 4_000_000);
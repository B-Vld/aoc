import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2023/inputs/3/3-example.txt';
const FP_ACTUAL = './aoc2023/inputs/3/3.txt';

class Point {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
    isOob(len) {
        return this.x < 0 ||
            this.x >= len ||
            this.y < 0 ||
            this.y >= len;
    }
    neighbors() {
        return [
            new Point(this.x - 1, this.y),
            new Point(this.x + 1, this.y),
            new Point(this.x, this.y - 1),
            new Point(this.x, this.y + 1),
            new Point(this.x + 1, this.y + 1),
            new Point(this.x + 1, this.y - 1),
            new Point(this.x - 1, this.y - 1),
            new Point(this.x - 1, this.y + 1),
        ];
    }
    equals(p) {
        return this.x === p.x && this.y === p.y;
    }
}

function isDigit(c) {
    return /^\d+$/.test(c);
}

function toDigitIsPartNumber(mat, line, lineIdx, part2) {
    let num = [], nums = [], numNeighbors = [];
    line.forEach((ch, colIdx) => {
        if (isDigit(ch)) {
            num.push(ch);
            const point = new Point(lineIdx, colIdx);
            point.neighbors().filter(n => !n.isOob(line.length)).forEach(n => numNeighbors.push(n));
            if (colIdx === line.length - 1) {
                part2 ? nums.push([parseInt(num.join('')), numNeighbors]) : nums.push([parseInt(num.join('')), numNeighbors.filter(n => !/[0-9.]/g.test(mat[n.x][n.y])).length === 0 ? false : true]);
            }
        } else {
            if (num.length > 0) {
                part2 ? nums.push([parseInt(num.join('')), numNeighbors]) : nums.push([parseInt(num.join('')), numNeighbors.filter(n => !/[0-9.]/g.test(mat[n.x][n.y])).length === 0 ? false : true]);
                num = [], numNeighbors = [];
            }
        }
    });
    return nums;
}

function getInput(path) {
    return readFileSync(path).toString().split("\r\n");
}

function has2NeighborNumbers(nums, asterixPos) {
    const neighbors = new Set();
    nums.forEach(n => n[1].forEach(p => {
        if (p.equals(asterixPos)) {
            neighbors.add(n[0]);
        }
    }));
    return neighbors.size < 2 ? 0 : [...neighbors].reduce((a, b) => a * b, 1);
}

function part1(input) {
    const mat = getInput(input).map(line => line.split(''));
    return mat.map((line, lineIdx) => toDigitIsPartNumber(mat, line, lineIdx, false))
        .flat()
        .filter(e => e[1])
        .reduce((a, b) => a + b[0], 0);
}

function part2(input) {
    const mat = getInput(input).map(line => line.split(''));
    const nums = mat.map((line, lineIdx) => toDigitIsPartNumber(mat, line, lineIdx, true)).flat();
    return mat.map((l, i) => l.map((_, j) => new Point(i, j)))
        .map(line => line.filter(p => mat[p.x][p.y] === '*'))
        .flat()
        .map(p => has2NeighborNumbers(nums, p))
        .reduce((a, b) => a + b, 0);
}

console.log(part1(FP_EXAMPLE));
console.log(part1(FP_ACTUAL));
console.log(part2(FP_EXAMPLE));
console.log(part2(FP_ACTUAL));
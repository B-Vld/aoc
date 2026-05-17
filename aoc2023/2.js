import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2023/inputs/2/2-example.txt';
const FP_ACTUAL = './aoc2023/inputs/2/2.txt';

class Subset {
    constructor(r, g, b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    isPossible(subset) {
        return this.r <= subset.r &&
            this.g <= subset.g &&
            this.b <= subset.b;
    }
}

function parseSubsets(arr) {
    const rReg = new RegExp('red', 'g'), gReg = new RegExp('green', 'g'), bReg = new RegExp('blue', 'g'), subsets = [];
    let r = 0, g = 0, b = 0;
    arr[1].map(e => e.split(",")).forEach(x => {
        x.forEach(e => {
            if (rReg.test(e)) {
                r = parseInt(e.replace(/[a-z, A-Z]/g, "").trim());
            }
            if (gReg.test(e)) {
                g = parseInt(e.replace(/[a-z, A-Z]/g, "").trim());
            }
            if (bReg.test(e)) {
                b = parseInt(e.replace(/[a-z, A-Z]/g, "").trim());
            }
        })
        subsets.push(new Subset(r, g, b));
        r = 0; g = 0; b = 0;
        rReg.lastIndex = 0; gReg.lastIndex = 0; bReg.lastIndex = 0;
    })
    return [parseInt(arr[0].replace(/[a-z, A-Z]/g, "").trim()), subsets];
}

function getPower(arr) {
    let rMax = 0, gMax = 0, bMax = 0;
    arr.forEach(e => {
        rMax = Math.max(rMax, e.r);
        gMax = Math.max(gMax, e.g);
        bMax = Math.max(bMax, e.b);
    });
    return rMax * gMax * bMax
}

function getInput(path) {
    return readFileSync(path).toString().split("\r\n");
}

function part1(input) {
    return getInput(input)
        .map(line => line.split(":"))
        .map(line => [line[0].replace("a-z, A-Z", ""), line[1].split(";")])
        .map(line => parseSubsets(line))
        .map(e => [e[0], e[1].map(x => x.isPossible(new Subset(12, 13, 14))).reduce((a, b) => a && b)])
        .filter(e => e[1])
        .reduce((a, b) => a + b[0], 0);
}

function part2(input) {
    return getInput(input)
        .map(line => line.split(":"))
        .map(line => [line[0].replace("a-z, A-Z", ""), line[1].split(";")])
        .map(line => parseSubsets(line))
        .map(e => e[1])
        .map(e => getPower(e))
        .reduce((a, b) => a + b, 0);
}

console.log(part1(FP_EXAMPLE));
console.log(part1(FP_ACTUAL));
console.log(part2(FP_EXAMPLE));
console.log(part2(FP_ACTUAL));
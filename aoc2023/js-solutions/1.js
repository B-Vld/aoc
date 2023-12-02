import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2023/inputs/1/1-example.txt';
const FP_ACTUAL = './aoc2023/inputs/1/1.txt';

function getInput(path) {
    return readFileSync(path).toString().split("\r\n");
}

function checkSubstring(str, a, b, i) {
    if (str.substring(a, b).includes('one')) {
        return 1;
    }
    if (str.substring(a, b).includes('two')) {
        return 2;
    }
    if (str.substring(a, b).includes('three')) {
        return 3;
    }
    if (str.substring(a, b).includes('four')) {
        return 4;
    }
    if (str.substring(a, b).includes('five')) {
        return 5;
    }
    if (str.substring(a, b).includes('six')) {
        return 6;
    }
    if (str.substring(a, b).includes('seven')) {
        return 7;
    }
    if (str.substring(a, b).includes('eight')) {
        return 8;
    }
    if (str.substring(a, b).includes('nine')) {
        return 9;
    }
    if (/\d+/g.test(str[i])) {
        return parseInt(str[i]);
    }
    return -1;
}

function gather(str) {
    let first = -1, second = -1
    for (let i = 0; i < str.length; i++) {
        if (first === -1) {
            first = checkSubstring(str, 0, i, i);
        } else {
            break;
        }
    }
    for (let i = str.length - 1; i >= 0; i--) {
        if (second === -1) {
            second = checkSubstring(str, i, str.length, i);
        } else {
            break;
        }
    }
    return (first * 10) + second;
}

function part1(input) {
    return getInput(input)
        .map(e => e.replace(/[a-z]/g, ""))
        .filter(e => e.length > 0)
        .map(e => e.length > 1 ? [e[0], e[e.length - 1]] : [e[0], e[0]])
        .map(e => (parseInt(e[0]) * 10) + parseInt(e[1]))
        .reduce((acc, cv) => acc + cv, 0);
}

function part2(input) {
    return getInput(input)
        .map(e => gather(e))
        .reduce((acc, cv) => acc + cv, 0);
}


console.log(part1(FP_EXAMPLE));
console.log(part1(FP_ACTUAL));
console.log(part2(FP_EXAMPLE));
console.log(part2(FP_ACTUAL));
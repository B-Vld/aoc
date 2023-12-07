import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2023/inputs/4/4-example.txt';
const FP_ACTUAL = './aoc2023/inputs/4/4.txt';

function toCard(line, idx, part2) {
    const setWin = new Set(), setOwn = new Set();
    let flag = true;
    line.forEach(e => {
        if (/[0-9]/.test(e) && flag) {
            setWin.add(parseInt(e));
        }
        if (/[0-9]/.test(e) && !flag) {
            setOwn.add(parseInt(e))
        }
        if (e === '|') {
            flag = false;
        }
    });
    return part2 ? {
        idx: idx + 1,
        match: [...setOwn].filter(e => setWin.has(e)).length,
        own: 1
    } :
        Math.floor(Math.pow(2, [...setOwn].filter(e => setWin.has(e)).length - 1));
}

function getInput(path) {
    return readFileSync(path).toString().split("\r\n");
}

function disperseCount(cards) {
    cards.forEach(card => {
        let ownNow = card.own;
        while (ownNow >= 1) {
            for (let i = card.idx; i < card.idx + card.match; i++) {
                cards[i].own++;
            }
            ownNow--;
        }
    })
    return cards;
}

function part1(input) {
    return getInput(input)
        .map(line => line.replace(/Card\s+\d+:/g, '').split(' '))
        .map((line, idx) => toCard(line, idx, false))
        .reduce((a, b) => a + b, 0);
}

function part2(input) {
    const cards = getInput(input)
        .map(line => line.replace(/Card\s+\d+:/g, '').split(' '))
        .map((line, idx) => toCard(line, idx, true));
    return disperseCount(cards)
        .reduce((a, b) => a + b.own, 0);
}

console.log(part1(FP_EXAMPLE));
console.log(part1(FP_ACTUAL));
console.log(part2(FP_EXAMPLE));
console.log(part2(FP_ACTUAL));
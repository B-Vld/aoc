import { readFileSync } from "fs";

const FP_EXAMPLE = './aoc2022/src/main/resources/23-example.txt';
const FP_ACTUAL = './aoc2022/src/main/resources/23.txt';

class Position {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
    equals(pos) {
        return this.x === pos.x && this.y === pos.y;
    }
    toString() {
        return `${this.x},${this.y}`
    }
    next(prio) {
        switch (prio) {
            case 'N': return new Position(this.x - 1, this.y);
            case 'S': return new Position(this.x + 1, this.y);
            case 'E': return new Position(this.x, this.y + 1);
            case 'W': return new Position(this.x, this.y - 1);
        }
    }
    north() {
        return [new Position(this.x - 1, this.y - 1), new Position(this.x - 1, this.y), new Position(this.x - 1, this.y + 1)];
    }
    south() {
        return [new Position(this.x + 1, this.y - 1), new Position(this.x + 1, this.y), new Position(this.x + 1, this.y + 1)];
    }
    east() {
        return [new Position(this.x + 1, this.y + 1), new Position(this.x, this.y + 1), new Position(this.x - 1, this.y + 1)];
    }
    west() {
        return [new Position(this.x + 1, this.y - 1), new Position(this.x, this.y - 1), new Position(this.x - 1, this.y - 1)];
    }
}

function strToPosition(str) {
    const strSplit = str.split(',');
    if (strSplit.length !== 2) {
        throw new Error(`Unable to map the string: ${str} to Position`);
    }
    return new Position(parseInt(strSplit[0]), parseInt(strSplit[1]));
}

function getInput(input) {
    let elfCount = 0;
    return new Map(readFileSync(input).toString().split('\r\n')
        .map(e => e.split(''))
        .map((line, lIdx) => {
            const results = [];
            line.forEach((ch, cIdx) => {
                if (ch === '#') {
                    const elf = [elfCount, new Position(lIdx, cIdx)];
                    elfCount++;
                    results.push(elf);
                }
            });
            return results;
        })
        .flatMap(e => e));
}

function toPosElfArrayMap(elfs) {
    const map = new Map();
    elfs.forEach((elfPos, elfIdx) => {
        const elfPosStr = elfPos.toString();
        if (map.has(elfPosStr)) {
            map.get(elfPosStr).push(elfIdx);
        } else {
            map.set(elfPosStr, []);
            map.get(elfPosStr).push(elfIdx);
        }
    });
    return map;
}

function scan(elf, elfPosMap, prio) {
    const around = [].concat(elf.north())
        .concat(elf.south())
        .concat(elf.east())
        .concat(elf.west())
        .map(pos => pos.toString());
    const otherElfs = new Set(elfPosMap.keys());
    const matchingPositions = new Set(around.filter(e => otherElfs.has(e)));
    if (matchingPositions.size > 0) {
        for (const p of prio) {
            if (p === 'N' && elf.north().filter(e => matchingPositions.has(e.toString())).length === 0) {
                return elf.next(p);
            }
            if (p === 'S' && elf.south().filter(e => matchingPositions.has(e.toString())).length === 0) {
                return elf.next(p);
            }
            if (p === 'E' && elf.east().filter(e => matchingPositions.has(e.toString())).length === 0) {
                return elf.next(p);
            }
            if (p === 'W' && elf.west().filter(e => matchingPositions.has(e.toString())).length === 0) {
                return elf.next(p);
            }
        }
        return new Position(elf.x, elf.y);
    } else {
        return new Position(elf.x, elf.y);
    }
}

function propose(elfs, prio) {
    let moved = false;
    const posElfArrMap = toPosElfArrayMap(elfs);
    const nextElfsPropositions = new Map(), nextElfs = new Map();
    elfs.forEach((elfPos, elfIdx) => {
        let scanRes = scan(elfPos, posElfArrMap, prio);
        if(moved === false) {
            moved = !scanRes.equals(elfPos);
        }
        nextElfsPropositions.set(elfIdx, scan(elfPos, posElfArrMap, prio));
    })
    const nextPosElfArrMap = toPosElfArrayMap(nextElfsPropositions);
    nextPosElfArrMap.forEach((elfArr, strPos) => {
        if (elfArr.length > 1) {
            elfArr.forEach((eIdx) => {
                nextElfs.set(eIdx, elfs.get(eIdx));
            });
        } else {
            nextElfs.set(elfArr[0], strToPosition(strPos));
        }
    })
    return [nextElfs, moved];
}


function rounds(elfs, rounds) {
    const prio = ['N', 'S', 'W', 'E'];
    let prevElfs = new Map(), roundCounter = 0;
    while (rounds > 0) {
        roundCounter++;
        prevElfs = new Map(elfs);
        const proposals = propose(elfs, prio);
        elfs = proposals[0];
        if (!proposals[1]) {
            return roundCounter;
        }
        rounds--;
        const next = prio.shift();
        prio.push(next);
    }
    return elfs;
}

function regionExtremities(elfMap) {
    const minHeight = Math.min(...[...elfMap.values()].map(e => e.x));
    const maxHeight = Math.max(...[...elfMap.values()].map(e => e.x));
    const minLen = Math.min(...[...elfMap.values()].map(e => e.y));
    const maxLen = Math.max(...[...elfMap.values()].map(e => e.y));
    return ((maxLen - minLen + 1) * (maxHeight - minHeight + 1)) - elfMap.size;
}


function part1(input) {
    console.log(regionExtremities(rounds(getInput(input), 10)));
}

function part2(input) {
    console.log(rounds(getInput(input), Infinity));
}

part1(FP_EXAMPLE);
part2(FP_EXAMPLE);
part1(FP_ACTUAL); //4138
part2(FP_ACTUAL); //1010
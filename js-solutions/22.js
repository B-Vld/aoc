import { readFileSync } from "fs";

const Direction = {
    Up: 'Up',
    Down: 'Down',
    Left: 'Left',
    Right: 'Right'
};

function determineDirection(currDirection, nextDirection) {
    switch (currDirection) {
        case Direction.Up: switch (nextDirection) {
            case Direction.Left: return Direction.Left;
            case Direction.Right: return Direction.Right;
        }
        case Direction.Down: switch (nextDirection) {
            case Direction.Left: return Direction.Right;
            case Direction.Right: return Direction.Left;
        }
        case Direction.Left: switch (nextDirection) {
            case Direction.Left: return Direction.Down;
            case Direction.Right: return Direction.Up;
        }
        case Direction.Right: switch (nextDirection) {
            case Direction.Left: return Direction.Up;
            case Direction.Right: return Direction.Down;
        }
    }
}

function parseDirection(c) {
    switch (c) {
        case 'L': return Direction.Left;
        case 'R': return Direction.Right;
    }
}

function readInput(inputPath) {
    const commands = readFileSync(inputPath)
        .toString()
        .split('\r\n');
    const labirinth = commands.splice(0, commands.indexOf(''));
    const maxLine = Math.max(...labirinth.map(e => e.length))
    let startingPosition = undefined;
    commands.shift();
    let matrix = [];
    for (let i = 0; i < labirinth.length; i++) {
        let line = [];
        for (let j = 0; j < maxLine; j++) {
            if (labirinth[i][j] === ' ' || labirinth[i][j] === undefined) {
                line.push('E');
            } else {
                if (startingPosition === undefined && labirinth[i][j] === '.') startingPosition = [i, j];
                line.push(labirinth[i][j])
            };
        }
        matrix.push(line);
    }
    let instructions = [];
    let c = "";
    for (let i = 0; i < commands[0].length; i++) {
        if (!isNaN(commands[0][i])) {
            c += commands[0][i];
        } else {
            instructions.push(parseInt(c));
            instructions.push(parseDirection(commands[0][i]));
            c = "";
        }
    }
    return [instructions, matrix, startingPosition];
}

function getFacing(dir) {
    switch (dir) {
        case Direction.Right: return 0;
        case Direction.Down: return 1;
        case Direction.Left: return 2;
        case Direction.Up: return 3;
    }
}

function readInstruction(row, col, currDir, lab, ins) {
    const labLineLen = lab[0].length;
    const labLen = lab.length;
    if (isNaN(ins)) {
        return [row, col, determineDirection(currDir, ins)];
    } else {
        switch (currDir) {
            case Direction.Up:
                while (ins !== 0) {
                    let nRow = row - 1;
                    if (nRow < 0) nRow = labLen - 1;
                    if (nRow === labLen) nRow = 0;
                    if (lab[nRow][col] === '.') row = nRow;
                    if (lab[nRow][col] === undefined) row = labLen - 1;
                    if (lab[nRow][col] === '#') ins = 1;
                    if (lab[nRow][col] === 'E') {
                        for (let i = labLen - 1; i >= 0; i--) {
                            if (lab[i][col] === '#') {
                                ins = 1;
                                break;
                            }
                            if (lab[i][col] === '.') {
                                row = i;
                                break;
                            }
                        }
                    }
                    ins--;
                }
                return [row, col, currDir];
            case Direction.Down:
                while (ins !== 0) {
                    let nRow = row + 1;
                    if (nRow < 0) nRow = labLen - 1;
                    if (nRow === labLen) nRow = 0;
                    if (lab[nRow][col] === '.') row = nRow;
                    if (lab[nRow][col] === undefined) row = 0;
                    if (lab[nRow][col] === '#') ins = 1;
                    if (lab[nRow][col] === 'E') {
                        for (let i = 0; i < labLen; i++) {
                            if (lab[i][col] === '#') {
                                ins = 1;
                                break;
                            }
                            if (lab[i][col] === '.') {
                                row = i;
                                break;
                            }
                        }
                    }
                    ins--;
                }
                return [row, col, currDir];
            case Direction.Left:
                while (ins !== 0) {
                    let nCol = col - 1;
                    if (nCol < 0) nCol = labLineLen - 1;
                    if (nCol === labLineLen) nCol = 0;
                    if (lab[row][nCol] === '.') col = nCol;
                    if (lab[row][nCol] === undefined) col = labLineLen - 1;
                    if (lab[row][nCol] === '#') ins = 1;
                    if (lab[row][nCol] === 'E') {
                        for (let i = labLineLen - 1; i >= 0; i--) {
                            if (lab[row][i] === '#') {
                                ins = 1;
                                break;
                            }
                            if (lab[row][i] === '.') {
                                col = i;
                                break;
                            }
                        }
                    }
                    ins--;
                }
                return [row, col, currDir];
            case Direction.Right:
                while (ins !== 0) {
                    let nCol = col + 1;
                    if (nCol < 0) nCol = labLineLen - 1;
                    if (nCol === labLineLen) nCol = 0;
                    if (lab[row][nCol] === '.') col = nCol;
                    if (lab[row][nCol] === undefined) col = 0;
                    if (lab[row][nCol] === '#') ins = 1;
                    if (lab[row][nCol] === 'E') {
                        for (let i = 0; i < labLineLen - 1; i++) {
                            if (lab[row][i] === '#') {
                                ins = 1;
                                break;
                            }
                            if (lab[row][i] === '.') {
                                col = i;
                                break;
                            }
                        }
                    }
                    ins--;
                }
                return [row, col, currDir];
            default: throw new Error("Invalid direction");
        }
    }
}

function solve1(lab, ins, sPos) {
    let [sRow, sCol, sDir] = [sPos[0], sPos[1], Direction.Right];
    let [fRow, fCol, fDir] = [0, 0, Direction.Right];
    while (ins.length !== 0) {
        [fRow, fCol, fDir] = readInstruction(sRow, sCol, sDir, lab, ins.shift());
        [sRow, sCol, sDir] = [fRow, fCol, fDir];
    }
    return [fRow, fCol, getFacing(fDir)];
}

function part1() {
    const [instructions, labirinth, startingPosition] = readInput('./src/input/22.txt');
    const [row, col, facing] = solve1(labirinth, instructions, startingPosition);
    console.log((1000 * (row + 1)) + (4 * (col + 1)) + facing);
}

function part2() {

}


part1(); // 88226
part2();
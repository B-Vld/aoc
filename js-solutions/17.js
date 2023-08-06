import { readFileSync } from "fs";

const FP_EXAMPLE = './src/input/17-example.txt';
const FP_ACTUAL = './src/input/17.txt';

const shape = ['Minus', 'Plus', 'FlippedL', 'Bar', 'Square'];

function getShapePositions(x, strShape) {
    const nX = x + 3, nY = 3, positions = [];
    switch (strShape) {
        case 'Minus': {
            for (let i = nY; i < 7; i++) {
                positions.push([nX, i]);
            }
            break;
        }
        case 'Plus': {
            positions.push([nX, nY + 1]);
            for (let i = nY; i < 6; i++) {
                positions.push([nX + 1, i]);
            }
            positions.push([nX + 2, nY + 1]);
            break;
        }
        case 'FlippedL': {
            for (let i = nY; i < 6; i++) {
                positions.push([nX, i]);
            }
            positions.push([nX + 1, nY + 2]);
            positions.push([nX + 2, nY + 2]);
            break;
        }
        case 'Bar': {
            for (let i = 0; i < 4; i++) {
                positions.push([nX + i, nY]);
            }
            break;
        }
        case 'Square': {
            positions.push([nX, nY]);
            positions.push([nX, nY + 1]);
            positions.push([nX + 1, nY]);
            positions.push([nX + 1, nY + 1]);
            break;
        }
    }
    return positions;
}

function getTop(positions) {
    return Math.max(...positions.map(pos => pos[0]));
}

function getYTop(positions, y) {
    return Math.max(...positions.filter(p => p[1] === y).map(pos => pos[0]));
}

function getAllYTop(positions) {
    const res = [];
    for (let i = 1; i < 8; i++) {
        res.push(getYTop(positions, i));
    }
    return res;
}

function lastNRowsAsString(positions, nRows) {
    const result = [], maxHeight = getTop(positions);
    if (maxHeight < nRows) {
        return result;
    } else {
        for (let i = 0; i < nRows; i++) {
            const currentRow = maxHeight - i;
            result.push(getRowAsString(positions, currentRow));
        }
    }
    return JSON.stringify(result);
}

function getRowAsString(positions, row) {
    let str = ['|', '.', '.', '.', '.', '.', '.', '.', '|'];
    positions.filter(pos => pos[0] === row).forEach(p => {
        str[p[1]] = "#";
    });
    return str;
}


function solve(input, nRocks, part2) {
    const allPos = [], allPosStringified = new Set(), states = new Map();
    let itDirections = 0, itShapeCount = 0, itShape = 0, maxHeight = 1, flag = false;
    for (let i = 1; i <= 7; i++) {
        allPos.push([0, i]);
    }
    allPos.forEach(pos => allPosStringified.add(JSON.stringify(pos)));
    while (itShapeCount !== nRocks) {
        const shapeMask = shape[itShape];
        let shapesPositions = getShapePositions(maxHeight, shapeMask), nextPositions = shapesPositions;
        while (!isColliding(allPosStringified, nextPositions)) {
            const direction = input[itDirections];
            shapesPositions = nextPositions;
            if (flag) {
                nextPositions = fall(shapesPositions);
            } else {
                nextPositions = move(shapesPositions, allPosStringified, direction);
                itDirections++;
                if (itDirections === input.length) {
                    itDirections = 0;
                }
            }
            flag = !flag;
        }
        shapesPositions.forEach(e => {
            allPos.push(e);
            allPosStringified.add(JSON.stringify(e));
        });
        if (part2 && itShapeCount >= 20) {
            /*
                I assumed there could be a repeating sequence if
                we 'photograph' the last 20 rows starting from the top
                Anything <16 gives a different result :/
            */
            const key = lastNRowsAsString(allPos, 20);
            if (states.has(key)) {
                const startingPoint = states.get(key);
                if (startingPoint.itShape === itShape && input[itDirections] === input[startingPoint.itDirections]) {
                    return [states, startingPoint, {
                        positions: allPos,
                        yHeights: getAllYTop(allPos),
                        itDirections: itDirections,
                        itShape: itShape,
                        shapeCount: itShapeCount
                    }];
                }
            }
            states.set(key, {
                positions: allPos,
                yHeights: getAllYTop(allPos),
                itDirections: itDirections,
                itShape: itShape,
                shapeCount: itShapeCount
            });
        }
        maxHeight = getTop(allPos) + 1;
        itShape++;
        itShapeCount++;
        flag = false;
        if (itShape === shape.length) {
            itShape = 0;
        }
    }
    return getTop(allPos);
}

function isColliding(allPosStringified, shapesPositions) {
    return shapesPositions.filter(e => allPosStringified.has(JSON.stringify(e))).length !== 0;
}

function getInput(path) {
    return readFileSync(path).toString();
}

function fall(shapesPositions) {
    return shapesPositions.map(pos => [pos[0] - 1, pos[1]]);
}

function move(shapesPositions, allPosStringified, direction) {
    let nextPositions = [];
    switch (direction) {
        case '>': {
            nextPositions = shapesPositions.map(pos => [pos[0], pos[1] + 1]);
            break;
        };
        case '<': {
            nextPositions = shapesPositions.map(pos => [pos[0], pos[1] - 1]);
            break;
        };
    }
    if (isOob(nextPositions) || isColliding(allPosStringified, nextPositions)) {
        return shapesPositions;
    }
    return nextPositions;
}

function isOob(nextPositions) {
    return nextPositions.map(pos => pos[1]).filter(pos => (pos < 1 || pos > 7)).length !== 0;
}

function part1(path) {
    console.log(solve(getInput(path), 2022, false));
}

function part2(path) {
    /*
        I assumed I'd find a cycle after 3000 pieces have fallen
        Hacky solution idk if it works for all inputs

        map - map of states
        first - starting point of the cycle
        second - ending point of the cycle
    */
    const [map, first, second] = solve(getInput(path), 3000, true);
    /*
        Frequency of the cycle i.e. cycle happening every `freqPieces` that have fallen
    */
    const freqPieces = second.shapeCount - first.shapeCount;
    /*
        maxHeightFirst - max height at beginning of the cycle
        maxHeightSecond - max height at the end of the cycle
    */
    const maxHeightFirst = Math.max(...first.yHeights), maxHeightSecond = Math.max(...second.yHeights);
    /*
        Height is increased by `freqHeight` each cycle
    */
    const freqHeight = maxHeightSecond - maxHeightFirst;
    /*
        You might start mid-cycle so until you find the starting point of the cycle
        a number of pieces have already fallen.
        You want to know the number of `remainingPieces` that have to fall
    */
    const remainingPieces = 1_000_000_000_000 - first.shapeCount;
    /*
        You want to know the number of cycles that could fit inside `remainingPieces`
    */
    const numOfCycles = Math.floor(remainingPieces / freqPieces);
    /*
        After the cycles finish you need to find the number of `remainingPiecesToFall`
    */
    const remainingPiecesToFall = remainingPieces - (numOfCycles * freqPieces);
    /*
        Find the max height after all the cycles
    */
    const maxHeightBeforeLastPieces = maxHeightFirst + (freqHeight * numOfCycles)
    /*
        Find the state in which a number of `idxOfRemaining` pieces have fallen
    */
    const idxOfRemaining = first.shapeCount + remainingPiecesToFall;

    let result = maxHeightBeforeLastPieces;
    for (const key of map.keys()) {
        const values = map.get(key);
        if (values.shapeCount === idxOfRemaining) {
            result += Math.max(...values.yHeights) - maxHeightFirst - 1;
            break;
        }
    }
    console.log(result);
}

part1(FP_ACTUAL);
// part1(FP_EXAMPLE);
part2(FP_ACTUAL);
// part2(FP_EXAMPLE);
import { readFileSync } from "fs";

const FP_EXAMPLE = './src/input/17-example.txt';
const FP_ACTUAL = './src/input/17.txt';

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

function solve(input, nRocks) {
    const allPos = [], allPosStringified = new Set(), shape = ['Minus', 'Plus', 'FlippedL', 'Bar', 'Square'];
    let itDirections = 0, itShape = 0, itShapeCount = 0, maxHeight = 1, flag = false;
    for (let i = 1; i <= 7; i++) {
        let pos = [0, i];
        allPos.push(pos);
        allPosStringified.add(JSON.stringify(pos));
    }
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
    console.log(solve(getInput(path), 2022));
}

function part2(path) {
}

part1(FP_ACTUAL);
// part1(FP_EXAMPLE);
// part2(FP_ACTUAL);
// part2(FP_EXAMPLE);
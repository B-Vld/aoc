const fs = require('fs');

const input = fs.readFileSync('./src/input/21.txt').toString()
    .split('\r\n')
    .map(line => {
        const arr = line.split(/:/);
        return { monkeyName: arr[0], monkeyJob: arr[1].trim() };
    });

function findMonkeyByName(arr, name) {
    return arr
        .find(e => e.monkeyName === name);
}

function part1(loneMonkeys) {
    let opMonkeys = input.filter(m => !containsNumbers(m.monkeyJob));
    let finalizedMonkeys = [...loneMonkeys];
    let i = 0;
    while (opMonkeys.length !== 0) {
        if (i === opMonkeys.length) {
            i = 0;
        }
        const monkey = opMonkeys[i];
        mName = monkey.monkeyName;
        mJob = monkey.monkeyJob;
        let arr = mJob.split(' ');
        const operation = arr[1];
        const foundMonkeyLeft = finalizedMonkeys.find(e => e.monkeyName === arr[0]);
        const foundMonkeyRight = finalizedMonkeys.find(e => e.monkeyName === arr[2]);
        if (foundMonkeyLeft !== undefined && foundMonkeyRight !== undefined) {
            let result = 0;
            switch (operation) {
                case '+': {
                    result = foundMonkeyLeft.monkeyJob + foundMonkeyRight.monkeyJob;
                    finalizedMonkeys.push({ monkeyName: mName, monkeyJob: result });
                    opMonkeys.splice(i, 1);
                    break;
                }
                case '-': {
                    result = foundMonkeyLeft.monkeyJob - foundMonkeyRight.monkeyJob;
                    finalizedMonkeys.push({ monkeyName: mName, monkeyJob: result });
                    opMonkeys.splice(i, 1);
                    break;
                }
                case '/': {
                    result = foundMonkeyLeft.monkeyJob / foundMonkeyRight.monkeyJob;
                    finalizedMonkeys.push({ monkeyName: mName, monkeyJob: result });
                    opMonkeys.splice(i, 1);
                    break;
                }
                case '*': {
                    result = foundMonkeyLeft.monkeyJob * foundMonkeyRight.monkeyJob;
                    finalizedMonkeys.push({ monkeyName: mName, monkeyJob: result });
                    opMonkeys.splice(i, 1);
                    break;
                }
            }
        } else {
            i++;
        }
    }

    return finalizedMonkeys;
}

function part2(finalizedMonkeys) {
    let monkeyTree = [];
    const firstLvl = input.find(e => e.monkeyJob.includes('humn'));
    const op = splitJobIntoMonkeyOperations(firstLvl.monkeyJob);
    monkeyTree.push({
        mName: firstLvl.monkeyName,
        mLeft: op.mLeft,
        operation: op.operation,
        mRight: op.mRight
    });
    let lastLvl = firstLvl;
    while (lastLvl.monkeyName !== 'root') {
        lastLvl = input.find(e => e.monkeyJob.includes(lastLvl.monkeyName));
        const op = splitJobIntoMonkeyOperations(lastLvl.monkeyJob);
        monkeyTree.push({
            mName: lastLvl.monkeyName,
            mLeft: op.mLeft,
            operation: op.operation,
            mRight: op.mRight
        });
    }
    const pairMonkey = findPairMonkey(monkeyTree, finalizedMonkeys);
    let currentHumnValue = findMonkeyByName(finalizedMonkeys, 'humn').monkeyJob;
    let finalResult = 0;
    monkeyTree.pop();

    let prevResult = 0;
    let addition = 10_000_000_000_000;

    while (finalResult != pairMonkey) {
        prevResult = finalResult;
        finalResult = solveOperationChain(monkeyTree, finalizedMonkeys, currentHumnValue);
        if (finalResult < pairMonkey) {
            finalResult = prevResult;
            currentHumnValue -= addition;
            addition /= 10;
        }
        currentHumnValue += addition;
    }

    return currentHumnValue - 10;
}

function findPairMonkey(monkeyTree, finalizedMonkeys) {
    const rootUnknown = findMonkeyByName(input, 'root');
    const rootJob = splitJobIntoMonkeyOperations(rootUnknown.monkeyJob);
    const rootLeft = rootJob.mLeft;
    const rootRight = rootJob.mRight;
    let equalMonkey = 0;
    if (monkeyTree[monkeyTree.length - 2].mName === rootLeft) {
        equalMonkey = findMonkeyByName(finalizedMonkeys, rootRight).monkeyJob;
    } else {
        equalMonkey = findMonkeyByName(finalizedMonkeys, rootLeft).monkeyJob;
    }
    return equalMonkey;
}


function solveOperationChain(monkeyTree, finalizedMonkeys, humnValue) {
    let mStack = [];
    let result = 0;
    const monkeyFirstOp = monkeyTree[0];
    const mLeft = monkeyFirstOp.mLeft;
    const mRight = monkeyFirstOp.mRight;
    const operation = monkeyFirstOp.operation
    if (mLeft === 'humn') {
        const valueRight = finalizedMonkeys.find(m => m.monkeyName === mRight).monkeyJob;
        const resultOfOperation = getValue(humnValue, valueRight, operation);
        result = resultOfOperation;
        mStack.push({ mName: monkeyFirstOp.mName, value: resultOfOperation });
    } else {
        const valueLeft = finalizedMonkeys.find(m => m.monkeyName === mLeft).monkeyJob;
        const resultOfOperation = getValue(valueLeft, humnValue, operation);
        result = resultOfOperation;
        mStack.push({ mName: monkeyFirstOp.mName, value: resultOfOperation });
    }
    for (let i = 1; i < monkeyTree.length; i++) {

        const prevValue = mStack.pop();
        const currMonkey = monkeyTree[i];
        const currOperation = currMonkey.operation;

        if (currMonkey.mLeft === prevValue.mName) {
            const valueRight = finalizedMonkeys.find(m => m.monkeyName === currMonkey.mRight).monkeyJob;
            const resultOfOperation = getValue(prevValue.value, valueRight, currOperation);
            result = resultOfOperation;
            mStack.push({ mName: currMonkey.mName, value: resultOfOperation });
        } else {
            const valueLeft = finalizedMonkeys.find(m => m.monkeyName === currMonkey.mLeft).monkeyJob;
            const resultOfOperation = getValue(valueLeft, prevValue.value, currOperation);
            result = resultOfOperation;
            mStack.push({ mName: currMonkey.mName, value: resultOfOperation });
        }
    }
    return result;
}

function getValue(mLeft, mRight, operation) {
    switch (operation) {
        case '+': {
            return mLeft + mRight;
        }
        case '-': {
            return mLeft - mRight;
        }
        case '/': {
            return Math.floor(mLeft / mRight);
        }
        case '*': {
            return mLeft * mRight;
        }
    }
}

function challenge() {
    let loneMonkeys = input.filter(m => containsNumbers(m.monkeyJob)).map(lm => {
        return { monkeyName: lm.monkeyName, monkeyJob: parseInt(lm.monkeyJob) };
    });

    const finalizedMonkeys = part1(loneMonkeys);
    console.log(findMonkeyByName(finalizedMonkeys, 'root').monkeyJob);

    console.log(part2(finalizedMonkeys));
}

function splitJobIntoMonkeyOperations(str) {
    let monkeyOperation = str.split(' ');
    return {
        mLeft: monkeyOperation[0],
        operation: monkeyOperation[1],
        mRight: monkeyOperation[2]
    };
}

function containsNumbers(str) {
    return /[0-9]/.test(str);
}


challenge();

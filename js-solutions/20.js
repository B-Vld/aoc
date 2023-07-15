import { readFileSync } from "fs";

const FP_EXAMPLE = './src/input/20-example.txt';
const FP_ACTUAL = './src/input/20.txt';

class CircularLinkedList {
    constructor(data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

function load(arr) {
    let all = [], sz = arr.length;
    for (let i = 0; i < sz; i++) {
        let e = arr[i];
        let curr = new CircularLinkedList(e);
        if (all.length !== 0) {
            let prev = all[i - 1];
            prev.next = curr;
            curr.prev = prev;
        }
        all.push(curr);
    }
    all[sz - 1].next = all[0];
    all[0].prev = all[sz - 1];
    return all;
}

function shuffle(arr, times) {
    const sz = arr.length - 1;
    for (let i = 0; i < times; i++) {
        arr.forEach((e) => {
            let steps = e.data % sz;
            if (steps !== 0) {
                e.prev.next = e.next;
                e.next.prev = e.prev;
                let nPrev = e.prev;
                while (steps !== 0) {
                    steps > 0 ? nPrev = nPrev.next : nPrev = nPrev.prev;
                    steps > 0 ? steps-- : steps++;
                }
                e.prev = nPrev;
                e.next = nPrev.next;
                e.prev.next = e;
                e.next.prev = e;
            }
        })
    }
}

function getInput(path) {
    return readFileSync(path).toString().split('\r\n').map(r => parseInt(r));
}

function getResult(arr) {
    let e0 = arr.filter((e) => e.data === 0)[0], i = 0, res = 0;
    while (i <= 3000) {
        if (i === 1000 || i === 2000 || i === 3000) {
            res += e0.data;
        }
        e0 = e0.next;
        i++;
    }
    return res;
}

function part1(path) {
    let input = load(getInput(path));
    shuffle(input, 1);
    console.log(getResult(input));
}

function part2(path) {
    const input = load(getInput(path).map(n => n *= 811589153));
    shuffle(input, 10);
    console.log(getResult(input));
}

part1(FP_ACTUAL);
// part1(FP_EXAMPLE);
part2(FP_ACTUAL);
// part2(FP_EXAMPLE);
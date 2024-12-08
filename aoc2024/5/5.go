package main

import (
	"bufio"
	"fmt"
	"os"
	"slices"
	"strconv"
	"strings"
)

func valid(rules map[int][]int, seq []int) bool {
	for i := 0; i < len(seq); i++ {
		key := seq[i]
		for j := i + 1; j < len(seq); j++ {
			if !slices.Contains(rules[key], seq[j]) {
				return false
			}
		}
	}
	return true
}

func part1(rules map[int][]int, seqArr [][]int) int {
	res := 0
	for _, seq := range seqArr {
		if valid(rules, seq) {
			res += seq[len(seq)/2]
		}
	}
	return res
}

func arrayWoElement(arr []int, idx int) []int {
	newSlice := append([]int{}, arr[:idx]...)
	newSlice = append(newSlice, arr[idx+1:]...)
	return newSlice
}

func reorder(rules map[int][]int, seq []int) []int {
	orderedSeq := []int{}
	copySeq := make([]int, len(seq))
	copy(copySeq, seq)
	for len(orderedSeq) < len(seq) {
		for i := 0; i < len(copySeq); i++ {
			num := copySeq[i]
			searchSeq := arrayWoElement(copySeq, i)
			if isMatchPos(rules, searchSeq, num) {
				orderedSeq = append(orderedSeq, num)
				nArr := arrayWoElement(copySeq, i)
				copySeq = nArr
			}
		}
	}
	return orderedSeq
}

func isMatchPos(rules map[int][]int, seq []int, element int) bool {
	for _, e := range seq {
		if !slices.Contains(rules[element], e) {
			return false
		}
	}
	return true
}

func part2(rules map[int][]int, seqArr [][]int) int {
	res := 0
	for _, seq := range seqArr {
		if !valid(rules, seq) {
			s := reorder(rules, seq)
			res += s[len(s)/2]
		}
	}
	return res
}

func readFile(filePath string) (map[int][]int, [][]int, error) {
	ruleMap := make(map[int][]int)
	arrs := [][]int{}
	file, err := os.Open(filePath)
	if err != nil {
		return nil, nil, fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	phase1 := true
	for scanner.Scan() {
		line := scanner.Bytes()
		if len(line) == 0 {
			phase1 = false
		} else if phase1 {
			l := strings.Split(string(line), "|")
			first, err := strconv.Atoi(l[0])
			if err != nil {
				return nil, nil, fmt.Errorf("failed to parse int: %w", err)
			}
			second, err := strconv.Atoi(l[1])
			if err != nil {
				return nil, nil, fmt.Errorf("failed to parse int: %w", err)
			}
			_, ok := ruleMap[first]
			if ok {
				ruleMap[first] = append(ruleMap[first], second)
			} else {
				a := []int{}
				a = append(a, second)
				ruleMap[first] = a
			}
		} else {
			l := strings.Split(string(line), ",")
			arr := []int{}
			for _, r := range l {
				num, err := strconv.Atoi(r)
				if err != nil {
					return nil, nil, fmt.Errorf("failed to parse int: %w", err)
				}
				arr = append(arr, num)
			}
			arrs = append(arrs, arr)
		}
	}
	if err := scanner.Err(); err != nil {
		return nil, nil, fmt.Errorf("error reading file: %w", err)
	}
	return ruleMap, arrs, nil
}

func main() {
	ruleMap, arrs, err := readFile("../input/5.txt")
	if err != nil {
		fmt.Printf("Error while reading the file: %v", err)
	}
	println(ruleMap)
	println(arrs)
	fmt.Println(part1(ruleMap, arrs))
	fmt.Println(part2(ruleMap, arrs))
}

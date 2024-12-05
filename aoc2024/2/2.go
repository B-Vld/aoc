package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"strconv"
	"strings"
)

func compareInt(a, b int) int {
	if a < b {
		return -1
	} else if a > b {
		return 1
	}
	return 0
}

func valid(first, second, cmp int) bool {
	diff := int(math.Abs(float64(first - second)))
	if diff > 3 {
		return false
	}
	nCmp := compareInt(first, second)
	if nCmp == 0 {
		return false
	} else {
		return nCmp == cmp
	}
}

func rmIdxCreateArr(arr []int) int {
	for i := 0; i < len(arr); i++ {
		newArr := []int{}
		p1 := arr[:i]
		p2 := arr[i+1:]
		newArr = append(newArr, p1...)
		newArr = append(newArr, p2...)
		if validatePart1(newArr) == 1 {
			return 1
		}
	}
	return 0
}

func part2(input [][]int) int {
	res := 0
	for _, arr := range input {
		res = res + validatePart2(arr)
	}
	return res
}

func validatePart2(arr []int) int {
	cmp := compareInt(arr[0], arr[1])
	for i := 1; i < len(arr); i++ {
		if !valid(arr[i-1], arr[i], cmp) {
			return rmIdxCreateArr(arr)
		}
	}
	return 1
}

func validatePart1(arr []int) int {
	cmp := compareInt(arr[0], arr[1])
	for i := 1; i < len(arr); i++ {
		if !valid(arr[i-1], arr[i], cmp) {
			return 0
		}
	}
	return 1
}

func part1(input [][]int) int {
	res := 0
	for _, arr := range input {
		res = res + validatePart1(arr)
	}
	return res
}

func readFileLines(filePath string) ([][]int, error) {
	l1 := [][]int{}
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := strings.Fields(scanner.Text())
		lineInt := []int{}
		for _, e := range line {
			i, err := strconv.Atoi(e)
			if err != nil {
				return nil, fmt.Errorf("failed to open file: %w", err)
			}
			lineInt = append(lineInt, i)
		}
		l1 = append(l1, lineInt)
	}
	if err := scanner.Err(); err != nil {
		return nil, fmt.Errorf("error reading file: %w", err)
	}
	return l1, nil
}

func main() {
	l1, err := readFileLines("../input/2.txt")
	if err != nil {
		fmt.Printf("Error while reading the file: %v", err)
	}
	fmt.Println(part1(l1))
	fmt.Println(part2(l1))
}

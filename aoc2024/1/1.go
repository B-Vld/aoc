package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
)

func part2(l1 []float64, l2 []float64) float64 {
	res := 0.0
	occurences := make(map[float64]float64)
	for _, num1 := range l1 {
		_, ok := occurences[num1]
		if !ok {
			for _, num2 := range l2 {
				if num1 == num2 {
					occurences[num1]++
				}
			}
		}
	}
	for _, n := range l1 {
		res = res + n*occurences[n]
	}
	return res
}

func part1(l1 []float64, l2 []float64) float64 {
	res := 0.0
	sort.Float64s(l1)
	sort.Float64s(l2)
	if len(l1) != len(l2) {
		panic("Arrays are not the same size")
	}
	for i := 0; i < len(l1); i++ {
		res = res + (math.Abs(l1[i] - l2[i]))
	}
	return res
}

func readFileLines(filePath string) ([]float64, []float64, error) {
	var l1 []float64
	var l2 []float64
	file, err := os.Open(filePath)
	if err != nil {
		return nil, nil, fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := strings.Fields(scanner.Text())
		num1, err := strconv.ParseFloat(line[0], 64)
		if err != nil {
			fmt.Printf("Error parsing float: %v", err)
		}
		l1 = append(l1, num1)
		num2, err := strconv.ParseFloat(line[1], 64)
		if err != nil {
			fmt.Printf("Error parsing float: %v", err)
		}
		l2 = append(l2, num2)
	}
	if err := scanner.Err(); err != nil {
		return nil, nil, fmt.Errorf("error reading file: %w", err)
	}
	return l1, l2, nil
}

func main() {
	l1, l2, err := readFileLines("../input/1.txt")
	if err != nil {
		fmt.Printf("Error while reading the file: %v", err)
	}
	fmt.Println(int64(part1(l1, l2)))
	fmt.Println(int64(part2(l1, l2)))
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func part2(input string) int {
	r, err := regexp.Compile(`mul\((\d+),(\d+)\)|do\(\)|don't\(\)`)
	if err != nil {
		fmt.Println("Error compiling regex:", err)
		return 0
	}
	matches := r.FindAllStringSubmatch(input, -1)
	res := 0
	do := true
	for _, match := range matches {
		if match[0] == "do()" {
			do = true
		} else if match[0] == "don't()" {
			do = false
		} else if do {
			a, err := strconv.Atoi(match[1])
			if err != nil {
				fmt.Printf("Error parsing int: %v", err)
			}
			b, err := strconv.Atoi(match[2])
			if err != nil {
				fmt.Printf("Error parsing int: %v", err)
			}
			res = res + (a * b)
		}
	}
	return res
}

func part1(input string) int {
	r, err := regexp.Compile(`mul\((\d+),(\d+)\)`)
	if err != nil {
		fmt.Println("Error compiling regex:", err)
		return 0
	}
	matches := r.FindAllStringSubmatch(input, -1)
	res := 0
	for _, match := range matches {
		a, err := strconv.Atoi(match[1])
		if err != nil {
			fmt.Printf("Error parsing int: %v", err)
		}
		b, err := strconv.Atoi(match[2])
		if err != nil {
			fmt.Printf("Error parsing int: %v", err)
		}
		res = res + (a * b)
	}
	return res
}

func readFile(filePath string) (string, error) {
	dat, err := os.ReadFile(filePath)
	if err != nil {
		return "", fmt.Errorf("failed to open file: %w", err)
	}
	return string(dat), err
}

func main() {
	str, err := readFile("../input/3.txt")
	if err != nil {
		fmt.Printf("Error while reading the file: %v", err)
	}
	fmt.Println(part1(str))
	fmt.Println(part2(str))
}

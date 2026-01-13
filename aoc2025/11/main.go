package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
	"time"
)

func input(scanner *bufio.Scanner) map[string][]string {
	nodes := map[string][]string{}
	for scanner.Scan() {
		line := scanner.Text()
		e := strings.Split(line, " ")
		paths := e[1:]
		nodes[strings.Split(e[0], ":")[0]] = paths
	}
	return nodes
}

func solve(nodes map[string][]string, cache map[string]int, start, end string) int {
	if _, ok := cache[start]; ok {
		return cache[start]
	}
	if start == end {
		return 1
	}
	res := 0
	neighbors := nodes[start]

	for _, neighbor := range neighbors {
		res += solve(nodes, cache, neighbor, end)
	}
	cache[start] = res

	return res
}

func main() {
	start := time.Now()
	file, err := os.Open("input.txt")
	if err != nil {
		panic(err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)

	in := input(scanner)

	fmt.Printf("Part 1: %d\n", solve(in, make(map[string]int), "you", "out"))
	p2 := 1
	p2 *= solve(in, make(map[string]int), "svr", "fft")
	p2 *= solve(in, make(map[string]int), "fft", "dac")
	p2 *= solve(in, make(map[string]int), "dac", "out")
	fmt.Printf("Part 2: %d\n", p2)

	elapsed := time.Since(start)
	log.Printf("Result time: %s", elapsed)
}

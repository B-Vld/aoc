package main

import (
	"bufio"
	"os"
)

func main() {
	file, err := os.Open("input.txt")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	scanner.Scan()
	first := scanner.Text()
	timelines := make([]int, len(first))
	for i, b := range first {
		if b == 'S' {
			timelines[i] = 1
		} else {
			timelines[i] = 0
		}
	}
	beams := 0

	for scanner.Scan() {
		line := scanner.Text()
		for i, b := range line {
			if b == '^' {
				if timelines[i] > 0 {
					beams++
				}
				timelines[i-1] += timelines[i]
				timelines[i+1] += timelines[i]
				timelines[i] = 0
			}
		}
	}

	sumTimelines := 0
	for _, t := range timelines {
		sumTimelines += t
	}

	println(beams)
	println(sumTimelines)
}

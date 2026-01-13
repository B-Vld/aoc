package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"regexp"
	"strconv"
	"strings"
	"time"
)

func input(scanner *bufio.Scanner) int {
	htagCount, res := 0, 0
	shapeArea := []int{}

	heightWidthRegex, _ := regexp.Compile(`^[.#]+$`)
	mulLineRegex, _ := regexp.Compile(`^(\d+)x(\d+):+`)

	for scanner.Scan() {

		line := scanner.Text()

		if "" == line {
			shapeArea = append(shapeArea, htagCount)
			htagCount = 0
		}

		if heightWidthRegex.MatchString(line) {
			htagCount += strings.Count(line, "#")
		}

		if mulLineRegex.MatchString(line) {

			spl := strings.Split(line, ":")

			a, _ := strconv.Atoi(strings.Split(spl[0], "x")[0])
			b, _ := strconv.Atoi(strings.Split(spl[0], "x")[1])
			area := a * b

			idxs := strings.Split(strings.TrimLeft(spl[1], " \t"), " ")

			allShapes := 0
			for i, idx := range idxs {
				conv, _ := strconv.Atoi(idx)
				allShapes += conv * shapeArea[i]
			}

			if allShapes <= area {
				res++
			}
		}
	}
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

	fmt.Println(input(scanner))

	elapsed := time.Since(start)
	log.Printf("Result time: %s", elapsed)
}

package main

import (
	"bufio"
	"errors"
	"fmt"
	"os"
)

type Pos struct {
	x, y, seen int
}

func findGuard(input []string) (Pos, error) {
	guard := "^"[0]
	for i := 0; i < len(input); i++ {
		line := input[i]
		for j := 0; j < len(line); j++ {
			if line[j] == guard {
				return Pos{i, j, 1}, nil
			}
		}
	}
	return Pos{}, errors.New("can't find guard")
}

func oob(x, y, h, w int) bool {
	return y < 0 || y > h-1 || x < 0 || x > w-1
}

func determineNextPos(input []string, road, wall, g byte, pos Pos, directions []Pos, dirIdx *int) (Pos, bool) {
	direction := directions[*dirIdx]
	nextPos := Pos{pos.x + direction.x, pos.y + direction.y, direction.seen}
	if oob(nextPos.x, nextPos.y, len(input), len(input[0])) {
		return Pos{}, true
	} else if input[nextPos.x][nextPos.y] == road || input[nextPos.x][nextPos.y] == g {
		return nextPos, false
	} else if input[nextPos.x][nextPos.y] == wall {
		for input[nextPos.x][nextPos.y] == wall {
			*dirIdx++
			if *dirIdx > len(directions)-1 {
				*dirIdx = 0
			}
			direction := directions[*dirIdx]
			nextPos = Pos{pos.x + direction.x, pos.y + direction.y, direction.seen}
		}
		return nextPos, false
	} else {
		return Pos{}, false
	}
}

func nextGuardPos(input []string, guard Pos, directions []Pos, dirIdx *int) (Pos, bool) {
	road := "."[0]
	wall := "#"[0]
	g := "^"[0]
	return determineNextPos(input, road, wall, g, guard, directions, dirIdx)
}

func hasPos(arr []Pos, p Pos) bool {
	for i := 0; i < len(arr); i++ {
		if arr[i].x == p.x && arr[i].y == p.y {
			arr[i].seen++
			return true
		}
	}
	return false
}

func seenCount(seen []Pos, count int) bool {
	for _, pos := range seen {
		if pos.seen > count {
			return true
		}
	}
	return false
}

func copy2dSlice(input []string, p Pos, b byte) []string {
	result := make([]string, len(input))
	copy(result, input)

	line := result[p.x]

	lineBytes := []byte(line)
	lineBytes[p.y] = b

	result[p.x] = string(lineBytes)

	return result
}

func part2(input []string) int {
	res := 0
	guard, err := findGuard(input)
	if err != nil {
		fmt.Errorf("Guard not found: %w", err)
	}
	guardMovement := part1(input)
	positions := []Pos{}
	for i, p := range guardMovement {
		if p.x == guard.x && p.y == guard.y {
			positions = append(guardMovement[:i], guardMovement[i+1:]...)
			break
		}
	}
	for _, pos := range positions {
		newInput := copy2dSlice(input, pos, "#"[0])
		if len(part1(newInput)) == 0 {
			res++
			println(res)
		}
	}
	return res
}

func part1(input []string) []Pos {
	seen := []Pos{}
	guard, err := findGuard(input)
	directions := []Pos{
		{-1, 0, 0},
		{0, 1, 0},
		{1, 0, 0},
		{0, -1, 0},
	}
	seen = append(seen, guard)
	dirIdx := 0
	if err != nil {
		fmt.Errorf("error caught: %w", err)
	}
	for {
		nextPos, done := nextGuardPos(input, guard, directions, &dirIdx)
		if done {
			break
		}
		if !hasPos(seen, nextPos) {
			seen = append(seen, nextPos)
		}
		if seenCount(seen, 3) {
			// cycle detected
			return []Pos{}
		}
		guard = nextPos
	}
	return seen
}

func readFile(filePath string) ([]string, error) {
	l1 := []string{}
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to open file: %w", err)
	}
	defer file.Close()
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Bytes()
		l1 = append(l1, string(line))
	}
	if err := scanner.Err(); err != nil {
		return nil, fmt.Errorf("error reading file: %w", err)
	}
	return l1, nil
}

func main() {
	str, err := readFile("../input/6.txt")
	if err != nil {
		fmt.Printf("error while reading the file: %v", err)
	}
	fmt.Println(len(part1(str)))
	fmt.Println(part2(str))
}

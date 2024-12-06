package main

import (
	"bufio"
	"bytes"
	"fmt"
	"os"
)

func oob(x, y, h, w int) bool {
	return y < 0 || y > h-1 || x < 0 || x > w-1
}

func checkAllDirections(directions [][]int, input []string, x, y int, word []byte) int {
	res := 0
	pos := [2]int{x, y}
	wLen := len(word)
	for i := 0; i < len(directions); i++ {
		maybeMatch := []byte{word[0]}
		direction := directions[i]
		for j := 1; j < wLen; j++ {
			nPos := [2]int{pos[0] + (j * direction[0]), pos[1] + (j * direction[1])}
			if oob(nPos[0], nPos[1], len(input), len(input[0])) {
				break
			} else {
				maybeMatch = append(maybeMatch, input[nPos[0]][nPos[1]])
			}
		}
		if bytes.Equal(word, maybeMatch) {
			res++
		}
	}
	return res
}

func checkAllDirections2(directions [][]int, input []string, x, y int) int {
	pos := [2]int{x, y}
	patternCoords := [][]int{}
	for i := 0; i < len(directions); i++ {
		direction := directions[i]
		nPos := [2]int{pos[0] + direction[0], pos[1] + direction[1]}
		if oob(nPos[0], nPos[1], len(input), len(input[0])) {
			return 0
		} else {
			patternCoords = append(patternCoords, nPos[:])
		}
	}
	m := byte(77)
	s := byte(83)

	upleft := byte(input[patternCoords[0][0]][patternCoords[0][1]])
	downright := byte(input[patternCoords[3][0]][patternCoords[3][1]])

	downleft := byte(input[patternCoords[1][0]][patternCoords[1][1]])
	upright := byte(input[patternCoords[2][0]][patternCoords[2][1]])

	if ((upleft == m && downright == s) || (upleft == s && downright == m)) &&
		((downleft == m && upright == s) || (downleft == s && upright == m)) {
		return 1
	}
	return 0
}

func part2(input []string) int {
	directions := [][]int{
		{-1, -1},
		{1, -1},
		{-1, 1},
		{1, 1},
	}
	found := 0
	for i, line := range input {
		for j := 0; j < len(line); j++ {
			c := input[i][j]
			if c == byte(65) {
				found = found + checkAllDirections2(directions, input, i, j)
			}
		}
	}
	return found
}

func part1(input []string) int {
	directions := [][]int{
		{-1, -1},
		{-1, 0},
		{-1, 1},
		{0, -1},
		{0, 1},
		{1, -1},
		{1, 0},
		{1, 1},
	}
	word := []byte("XMAS")
	found := 0
	for i, line := range input {
		for j := 0; j < len(line); j++ {
			c := input[i][j]
			if c == word[0] {
				found += checkAllDirections(directions, input, i, j, word)
			}
		}
	}
	return found
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
	str, err := readFile("../input/4.txt")
	if err != nil {
		fmt.Printf("Error while reading the file: %v", err)
	}
	fmt.Println(part1(str))
	fmt.Println(part2(str))
}

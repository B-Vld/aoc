package main

import (
	"bufio"
	"fmt"
	"log"
	"math"
	"os"
	"sort"
	"strconv"
	"strings"
	"time"
)

type Point3D struct {
	x float64
	y float64
	z float64
}

type Bond struct {
	dist float64
	a    int
	b    int
}

func bfs(visited []bool, mat [][]int, start int) int {
	q := []int{}
	q = append(q, start)
	count := 0
	for i := 0; i < len(q); i++ {
		curr := q[i]
		if !visited[curr] {
			visited[curr] = true
			count++
			for j, e := range mat[curr] {
				if e == 1 {
					q = append(q, j)
				}
			}
		}
	}
	return count
}

func findStronglyConnectedGraphs(mat [][]int) int {
	N := len(mat)
	visited := make([]bool, N)
	nodes := []int{}
	for i := range N {
		if !visited[i] {
			nodes = append(nodes, bfs(visited, mat, i))
		}
	}
	sort.Slice(nodes, func(i, j int) bool {
		return nodes[i] > nodes[j]
	})
	return nodes[0] * nodes[1] * nodes[2]
}

func buildSpanningForest(points []Point3D) (int, int) {
	N := len(points)
	matrix := make([][]int, N)
	for i := range matrix {
		matrix[i] = make([]int, N)
	}
	bonds := []Bond{}
	for i := 0; i < N-1; i++ {
		for j := i + 1; j < N; j++ {
			a := points[i]
			b := points[j]
			dist := euclideanDistance(a, b)
			bond := Bond{
				dist: dist,
				a:    i,
				b:    j,
			}
			bonds = append(bonds, bond)
		}
	}
	sort.Slice(bonds, func(i, j int) bool {
		return bonds[i].dist < bonds[j].dist
	})

	for i := range 1000 {
		bond := bonds[i]
		matrix[bond.a][bond.b] = 1
		matrix[bond.b][bond.a] = 1
	}
	p1 := findStronglyConnectedGraphs(matrix)

	visited := map[int]struct{}{}
	bCount := 0
	for _, b := range bonds {
		if len(visited) == N {
			break
		}

		visited[b.a] = struct{}{}
		visited[b.b] = struct{}{}

		bCount++
	}
	first, second := points[bonds[bCount-1].a], points[bonds[bCount-1].b]
	return p1, int(first.x) * int(second.x)
}

func euclideanDistance(a Point3D, b Point3D) float64 {
	return math.Sqrt(math.Pow(a.x-b.x, 2) + math.Pow(a.y-b.y, 2) + math.Pow(a.z-b.z, 2))
}

func getPoints(scanner *bufio.Scanner) []Point3D {
	points := []Point3D{}
	for scanner.Scan() {
		line := scanner.Text()

		fields := strings.Split(line, ",")
		row := make([]float64, len(fields))

		for i, f := range fields {
			v, err := strconv.ParseFloat(f, 64)
			if err != nil {
				panic(err)
			}
			row[i] = v
		}
		point := Point3D{
			x: row[0],
			y: row[1],
			z: row[2],
		}
		points = append(points, point)
	}
	return points
}

func main() {
	start := time.Now()
	file, err := os.Open("input.txt")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	points := getPoints(scanner)
	p1, p2 := buildSpanningForest(points)
	fmt.Printf("%d\n%d", p1, p2)
	elapsed := time.Since(start)
	log.Printf("Result time: %s", elapsed)
}

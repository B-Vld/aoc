package main

import (
	"bufio"
	"fmt"
	"log"
	"math"
	"os"
	"slices"
	"sort"
	"strconv"
	"strings"
	"time"
)

type Coord struct {
	x int
	y int
}

type Rectangle struct {
	minX, maxX, minY, maxY, area int
}

func absInt(x int) int {
	if x < 0 {
		return -x
	}
	return x
}

func input(scanner *bufio.Scanner) []Coord {
	in := []Coord{}
	for scanner.Scan() {
		line := scanner.Text()
		numsStr := strings.Split(line, ",")
		col, err := strconv.Atoi(numsStr[0])
		if err != nil {
			panic(err)
		}
		row, err := strconv.Atoi(numsStr[1])
		if err != nil {
			panic(err)
		}
		coord := Coord{
			x: row,
			y: col,
		}
		in = append(in, coord)
	}
	return in
}

func part1(coords []Coord) (int, []Rectangle) {
	var maxArea int = 0
	rectangles := []Rectangle{}
	for i := 0; i < len(coords)-1; i++ {
		for j := i + 1; j < len(coords); j++ {
			L := absInt(coords[i].x-coords[j].x) + 1
			l := absInt(coords[i].y-coords[j].y) + 1
			area := L * l
			rectangles = append(rectangles, Rectangle{
				minX: min(coords[i].x, coords[j].x),
				maxX: max(coords[i].x, coords[j].x),
				minY: min(coords[i].y, coords[j].y),
				maxY: max(coords[i].y, coords[j].y),
				area: area,
			})
			maxArea = max(maxArea, area)
		}
	}
	sort.Slice(rectangles, func(i, j int) bool {
		return rectangles[i].area > rectangles[j].area
	})
	return maxArea, rectangles
}

func uniqueXYs(coords []Coord) ([]int, []int) {
	setx := map[int]struct{}{}
	sety := map[int]struct{}{}
	xs := []int{}
	ys := []int{}
	for _, c := range coords {
		for j := -1; j < 2; j++ {
			if _, ok := setx[c.x+j]; !ok {
				xs = append(xs, c.x+j)
				setx[c.x+j] = struct{}{}
			}
			if _, ok := sety[c.y+j]; !ok {
				ys = append(ys, c.y+j)
				sety[c.y+j] = struct{}{}
			}
		}
	}
	sort.Ints(xs)
	sort.Ints(ys)
	return xs, ys
}

func compressCoords(oldCoords []Coord, xs, ys []int) []Coord {
	nCoords := []Coord{}
	for _, c := range oldCoords {
		nc := Coord{
			x: slices.Index(xs, c.x),
			y: slices.Index(ys, c.y),
		}
		nCoords = append(nCoords, nc)
	}
	return nCoords
}

func indexMap(vals []int) map[int]int {
	m := make(map[int]int, len(vals))
	for i, v := range vals {
		m[v] = i
	}
	return m
}

func mapOldRectToCompressed(r Rectangle, xIdx, yIdx map[int]int) (Rectangle, bool) {
	cMinX, ok := xIdx[r.minX]
	if !ok {
		return Rectangle{}, false
	}
	cMaxX, ok := xIdx[r.maxX]
	if !ok {
		return Rectangle{}, false
	}
	cMinY, ok := yIdx[r.minY]
	if !ok {
		return Rectangle{}, false
	}
	cMaxY, ok := yIdx[r.maxY]
	if !ok {
		return Rectangle{}, false
	}

	return Rectangle{
		minX: cMinX, maxX: cMaxX,
		minY: cMinY, maxY: cMaxY,
	}, true
}

func formPolygonEdge(rowMap map[int]map[int]struct{}, a, b Coord) {
	var start int = 0
	var end int = 0
	if a.x == b.x {
		start = min(a.y, b.y)
		end = max(a.y, b.y)
		for j := start; j <= end; j++ {
			if _, ok := rowMap[a.x]; !ok {
				rowMap[a.x] = make(map[int]struct{})
			}
			rowMap[a.x][j] = struct{}{}
		}
	}
	if a.y == b.y {
		start = min(a.x, b.x)
		end = max(a.x, b.x)
		for j := start; j <= end; j++ {
			if _, ok := rowMap[j]; !ok {
				rowMap[j] = make(map[int]struct{})
			}
			rowMap[j][a.y] = struct{}{}
		}
	}
}

func polygonEdgeCoords(coords []Coord) map[int]map[int]struct{} {
	rowMap := map[int]map[int]struct{}{}
	for i := 1; i <= len(coords); i++ {
		formPolygonEdge(rowMap, coords[i%len(coords)], coords[(i-1)%len(coords)])
	}
	return rowMap
}

func toCoordsSlice(rmap map[int]map[int]struct{}) []Coord {
	res := []Coord{}
	for k1, v := range rmap {
		for k2 := range v {
			res = append(res, Coord{x: k1, y: k2})
		}
	}
	return res
}

func minMax(m map[int]map[int]struct{}) (int, int, int, int) {
	xmin, xmax, ymin, ymax := math.MaxInt, 0, math.MaxInt, 0
	for k1, v := range m {
		xmin = min(xmin, k1)
		xmax = max(xmax, k1)
		for k2 := range v {
			ymin = min(ymin, k2)
			ymax = max(ymax, k2)
		}
	}
	return xmin, xmax, ymin, ymax
}

func part2(coords []Coord, rectangles []Rectangle) int {
	xs, ys := uniqueXYs(coords)
	xIdx := indexMap(xs)
	yIdx := indexMap(ys)

	compressedCoords := compressCoords(coords, xs, ys)
	cpcMap := polygonEdgeCoords(compressedCoords)
	outside := fillOutside(cpcMap)

	return maxInsideArea(xIdx, yIdx, rectangles, outside)

}

func maxInsideArea(xIdx, yIdx map[int]int, rectangles []Rectangle, outside map[int]map[int]struct{}) int {
	for _, r := range rectangles {

		cr, ok := mapOldRectToCompressed(r, xIdx, yIdx)
		if !ok {
			continue
		}
		if isValidRectangle(cr, outside) {
			return r.area
		}
	}
	return 0
}

func isValidRectangle(r Rectangle, outside map[int]map[int]struct{}) bool {
	for i := r.minX; i <= r.maxX; i++ {
		for j := r.minY; j <= r.maxY; j++ {
			if _, ok := outside[i][j]; ok {
				return false
			}
		}
	}
	return true
}

func fillOutside(compressedPolygon map[int]map[int]struct{}) map[int]map[int]struct{} {
	minX, maxX, minY, maxY := minMax(compressedPolygon)
	ep := []Coord{
		{minX, minY},
		{minX, maxY},
		{maxX, minY},
		{maxX, maxY},
	}
	outsideMap := map[int]map[int]struct{}{}
	for k := range compressedPolygon {
		outsideMap[k] = make(map[int]struct{})
	}
	for _, startCorner := range ep {
		bfsOut(minX, maxX, minY, maxY, startCorner, outsideMap, compressedPolygon)
	}
	return outsideMap
}

func bfsOut(minX, maxX, minY, maxY int, startCorner Coord, outside, polygon map[int]map[int]struct{}) {
	q := []Coord{}
	q = append(q, startCorner)
	moves := []Coord{
		{-1, 0},
		{1, 0},
		{0, -1},
		{0, 1},
	}
	for i := 0; i < len(q); i++ {
		curr := q[i]
		if isInBounds(curr, maxX, minX, maxY, minY) {
			// not part of the polygon
			if _, ok := polygon[curr.x][curr.y]; !ok {
				// not visited / part of outside points
				if _, ok := outside[curr.x][curr.y]; !ok {
					outside[curr.x][curr.y] = struct{}{}
					for _, mov := range moves {
						q = append(q, Coord{curr.x + mov.x, curr.y + mov.y})
					}
				}

			}

		}
	}
}

func isInBounds(c Coord, maxX, minX, maxY, minY int) bool {
	return c.x >= minX &&
		c.x <= maxX &&
		c.y >= minY &&
		c.y <= maxY
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

	p1res, rectangles := part1(in)
	fmt.Println(p1res)
	fmt.Println(part2(in, rectangles))

	elapsed := time.Since(start)
	log.Printf("Result time: %s", elapsed)
}

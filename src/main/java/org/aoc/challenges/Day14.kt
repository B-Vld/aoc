package org.aoc.challenges

import lombok.extern.slf4j.Slf4j
import org.aoc.Challenge
import org.aoc.annotations.Day
import org.aoc.enums.AdvanceSand
import org.aoc.enums.SandEvent
import org.aoc.structures.dao.Position
import org.aoc.utils.Input
import java.lang.Integer.max
import kotlin.math.min

@Slf4j
@Day(day = 14)
class Day14 : Challenge {
    override fun firstChallenge(fileName: String?) {
        val positions = mutableSetOf<Position>()
        Input.allLinesDefaultDelimiter(fileName)
                .map { str -> str.split(" -> ") }
                .forEach { line -> processLine(positions, line) }
        val initialPositions = positions.size
        while (fallingSand(positions, 1000) == SandEvent.CONTINUE)
        println("Day 14 first challenge : ${positions.size - initialPositions}")
    }

    override fun secondChallenge(fileName: String?) {
        val positions = mutableSetOf<Position>()
        Input.allLinesDefaultDelimiter(fileName)
                .map { str -> str.split(" -> ") }
                .forEach { line -> processLine(positions, line) }
        val initialPositions = positions.size
        val maxX = positions.maxOf { it.x } + 1
        while (fallingSand(positions, -1, maxX) == SandEvent.CONTINUE)
        println("Day 14 second challenge : ${positions.size - initialPositions}")
    }

    private fun fallingSand(walls: MutableSet<Position>, stopX: Int, maxX: Int = 0): SandEvent {
        var part2 = false

        if(maxX != 0) part2 = true

        var sand = if(part2)
            Position.of(0, 500)
        else
            Position.of(0, 500)

        var nextPosition = decidePosition(sand, walls)

        while(nextPosition != AdvanceSand.STAY) {
            sand = nextPosition.apply(sand)
            nextPosition = decidePosition(sand, walls)
            if(part2) {
                if(sand.x == maxX) {
                    walls.add(sand)
                    return SandEvent.CONTINUE
                }
            } else {
                if(sand.x == stopX ) {
                    return SandEvent.END
                }
            }
        }
        walls.add(sand)
        if(part2 && sand.equals(Position.of(0, 500))) {
            return SandEvent.END
        }
        return SandEvent.CONTINUE
    }

    private fun decidePosition(sand: Position, walls: Set<Position>): AdvanceSand {
        if(!walls.contains(AdvanceSand.DOWN.apply(sand))) {
            return AdvanceSand.DOWN
        }
        if(!walls.contains(AdvanceSand.DOWN_LEFT.apply(sand))) {
            return AdvanceSand.DOWN_LEFT
        }
        if(!walls.contains(AdvanceSand.DOWN_RIGHT.apply(sand))) {
            return AdvanceSand.DOWN_RIGHT
        }
        return AdvanceSand.STAY
    }

    private fun processLine(p: MutableCollection<Position>, l: List<String>) {
        for (i in 0..l.size - 2) {
            val p1Str = l[i].split(",")
            val p2Str = l[i + 1].split(",")
            val row1 = p1Str[0].toInt()
            val row2 = p2Str[0].toInt()
            val col1 = p1Str[1].toInt()
            val col2 = p2Str[1].toInt()
            if (row1 == row2) {
                for (j in min(col1, col2)..max(col1, col2)) {
                    p.add(Position.of(j, row1))
                }
            }
            if (col1 == col2) {
                for (j in min(row1, row2)..max(row1, row2)) {
                    p.add(Position.of(col1, j))
                }
            }
        }
    }
}
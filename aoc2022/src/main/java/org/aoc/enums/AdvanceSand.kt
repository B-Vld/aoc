package org.aoc.enums

import org.aoc.structures.dao.Position
import java.util.function.Function

enum class AdvanceSand : Function<Position, Position> {
    DOWN {
        override fun apply(t: Position): Position {
            return Position.of(t.x + 1, t.y)
        }
    },
    DOWN_LEFT {
        override fun apply(t: Position): Position {
            return Position.of(t.x + 1, t.y - 1)
        }
    },
    DOWN_RIGHT {
        override fun apply(t: Position): Position {
            return Position.of(t.x + 1, t.y + 1)
        }
    },
    STAY {
        override fun apply(t: Position): Position {
            return t
        }
    };
}
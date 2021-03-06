package advent_of_code_2021.day8.part2

import advent_of_code_2021.shared.parseInput


fun main() {
    println(resolve("day8/part2/input.txt"))
}

typealias SegmentSet = Set<Char>

private fun parseSegments(inputSource: String): Sequence<Pair<List<String>, List<String>>> {
    return parseInput(inputSource)
        .map { line -> line.split(" | ", limit = 2) }
        .map { segments -> segments.map { it.split(" ") } }
        .map { it.zipWithNext().single() }
}

internal fun resolve(inputSource: String): Int {
    return parseSegments(inputSource)
        .map { pair -> pair.fmap { it.toHashSet() }}
        .map { (wires, segments) -> inferNumbers(segments.asSequence(), inferWires(wires.asSequence())) }
        .sum()
}

fun inferNumbers(segmentsSets: Sequence<SegmentSet>, wires: Map<SegmentSet, Char>): Int {
    return segmentsSets
        .map { set -> wires[set]!! }
        .joinToString("")
        .toInt()
}

/**
 *  dddd
 * e    a
 * e    a
 *  ffff
 * g    b
 * g    b
 *  cccc
 *
 * bcdef    5
 * acdfg    2
 * abcdf    3
 * abcdef   9
 * bcdefg   6
 * abcdeg   0
 * abcdefg  8
 * abd      7
 * abef     4
 * ab       1
 */
internal fun inferWires(wires: Sequence<SegmentSet>): Map<SegmentSet, Char> {

    val wireCount: (Int) -> (SegmentSet) -> Boolean = { count -> { it.size == count } }

    // Single segment count
    val c1 = wires.find(wireCount(2))!!
    val c7 = wires.find(wireCount(3))!!
    val c4 = wires.find(wireCount(4))!!
    val c8 = wires.find(wireCount(7))!!

    // Five segments
    val fiveWires = wires.filter(wireCount(5))
    val c3 = fiveWires
        .first { wireCount(2)(it.intersect(c1)) }
    val c5 = fiveWires
        .filter { it != c3 }
        .first { wireCount(2)(it.minus(c4)) }
    val c2 = fiveWires
        .filter { it != c3 }
        .first { wireCount(3)(it.minus(c4)) }

    // Six segments
    val sixWires = wires.filter(wireCount(6))
    val c6 = sixWires
        .first { wireCount(1)(it.intersect(c1)) }
    val c9 = sixWires
        .filter { it != c6 }
        .first { wireCount(2)(it.minus(c4)) }
    val c0 = sixWires
        .filter { it != c6 }
        .first { wireCount(3)(it.minus(c4)) }

    return mapOf(
        c0 to '0',
        c1 to '1',
        c2 to '2',
        c3 to '3',
        c4 to '4',
        c5 to '5',
        c6 to '6',
        c7 to '7',
        c8 to '8',
        c9 to '9',
    )
}

fun <T, R> Pair<List<T>, List<T>>.fmap(f: (T) -> R): Pair<List<R>, List<R>> =
    Pair(this.first.map(f), this.second.map(f))

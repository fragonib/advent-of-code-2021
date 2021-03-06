package advent_of_code_2021.day10.part2

import advent_of_code_2021.day10.part1.Error
import advent_of_code_2021.day10.part1.parseLine
import advent_of_code_2021.shared.parseInput


fun main() {
    val result = resolve("day10/part2/input.txt")
    println(result)
}

internal fun resolve(inputSource: String): Int {

    val missingTailLines = parseInput(inputSource)
        .map { parseLine(it.toList()) }
        .map { (either, _) -> either.swap().orNull() }
        .filter { it is Error.Incomplete }

    val scores = missingTailLines
        .map { (it as Error.Incomplete).missing }
        .map { score(it) }
        .sorted()
        .toList()

    return scores[scores.size / 2]
}

fun score(missingChars: Array<Char>): Int {
    return missingChars.fold(0) { acc, new -> acc * 5 + score(new) }
}

fun score(char: Char): Int {
    val scoreTable = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )
    return scoreTable[char] ?: throw IllegalArgumentException("Illegal char [$char]")
}
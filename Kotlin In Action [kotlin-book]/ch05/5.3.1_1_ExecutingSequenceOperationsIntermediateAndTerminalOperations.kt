package ch05.ex3_1_1_ExecutingSequenceOperationsIntermediateAndTerminalOperations

fun main(args: Array<String>) {
    listOf(1, 2, 3, 4).asSequence()
            .map { print("map($it) "); it * it }
            .filter { print("filter($it) "); it % 2 == 0 }
}

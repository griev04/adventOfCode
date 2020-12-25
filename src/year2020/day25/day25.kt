package year2020.day25

import common.TextFileParser

fun main() {
    val givenPublicKeys = TextFileParser.parseLines("src/year2020/day25/input.txt") { it.toLong() }

    println("Part 1")
    val encryptionKey = EncryptionSystem(givenPublicKeys).getEncryptionKey()
    println(encryptionKey)
}

class EncryptionSystem(private val publicKeys: List<Long>) {
    private var secretLoops: List<Int>
    private var encryptionKey: Long

    init {
        secretLoops = determineLoops()
        encryptionKey = computeEncryptionKey()
    }

    fun getEncryptionKey(): Long {
        return encryptionKey
    }

    private fun determineLoops(): List<Int> {
        return publicKeys.map { computeLoopsToGetKey(it) }
    }

    private fun computeLoopsToGetKey(publicKey: Long): Int {
        var trialKey = 1L
        var loop = 0
        while (trialKey != publicKey) {
            trialKey = loop(trialKey)
            loop++
        }
        return loop
    }

    private fun computeEncryptionKey(): Long {
        val encryptionKeys = publicKeys.withIndex().map {
            val publicKey = it.value
            val othersSecretLoops = secretLoops[(it.index + 1) % 2]
            calculatePublicKey(othersSecretLoops, publicKey)
        }
        if (encryptionKeys[0] == encryptionKeys[1]) {
            return encryptionKeys[0]
        }
        return -1L
    }

    private fun calculatePublicKey(loops: Int, subject: Long): Long {
        var publicKey = 1L
        repeat(loops) {
            publicKey = loop(publicKey, subject)
        }
        return publicKey
    }

    private fun loop(current: Long, subject: Long = BASE_SUBJECT): Long {
        return (current * subject) % DIVISOR
    }

    companion object {
        const val DIVISOR = 20201227L
        const val BASE_SUBJECT = 7L
    }
}

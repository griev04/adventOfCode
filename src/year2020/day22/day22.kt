package year2020.day22

import common.TextFileParser

fun main() {
    val decks = TextFileParser.parseGroupedData("src/year2020/day22/input.txt") { parseDeck(it) }
    val game = CombatGame(decks)

    println("Part 1")
    val result1 = game.play().getFinalScore()
    println(result1)

    println("Part 2")
    val result2 = game.playRecursive().getFinalScore()
    println(result2)
}

class CombatGame(private var decks: List<Deck>) {
    private var winner: Int = -1

    private fun initDecks() {
        decks = decks.map { it.reset() }.sortedBy { it.player }
    }

    fun playRecursive(): CombatGame {
        initDecks()
        winner = playRecursiveMatch()
        return this
    }

    fun play(): CombatGame {
        initDecks()
        winner = playMatch()
        return this
    }

    fun getFinalScore(): Long {
        return decks[winner].getScore()
    }

    private fun playRecursiveMatch(matchDecks: List<Deck> = this.decks): Int {
        val seenDecks = mutableSetOf<List<Deck>>()

        while (matchDecks.none { it.isEmpty() }) {
            if (alreadySeenDecks(matchDecks, seenDecks)) return 0
            seenDecks.add(matchDecks)
            val drawnCards = matchDecks.map { it.draw() }

            val winner = if (playersHaveEnoughCards(matchDecks, drawnCards)) {
                val reducedDecks = cutDecks(matchDecks, drawnCards)
                playRecursiveMatch(reducedDecks)
            } else {
                drawnCards.indexOf(drawnCards.max())
            }
            val sortedCards = sortCards(drawnCards, winner)
            matchDecks[winner].insertCards(sortedCards)
        }
        return matchDecks.indexOfFirst { !it.isEmpty() }
    }

    private fun sortCards(drawnCards: List<Int>, winner: Int): List<Int> {
        val winningCard = drawnCards[winner]
        val losingCard = drawnCards.find { it != winningCard }!!
        return listOf(winningCard, losingCard)
    }

    private fun cutDecks(matchDecks: List<Deck>, drawnCards: List<Int>): List<Deck> {
        val reducedDecks = matchDecks.map { it.copyOf() }
        reducedDecks.forEachIndexed { index, deck -> deck.cutDeckTo(drawnCards[index]) }
        return reducedDecks
    }

    private fun playersHaveEnoughCards(matchDecks: List<Deck>, drawnCards: List<Int>): Boolean {
        val enoughCards = matchDecks.mapIndexed { index, deck -> deck.hasEnoughCards(drawnCards[index]) }
        return enoughCards.all { it }
    }

    private fun alreadySeenDecks(matchDecks: List<Deck>, matchPreviousCards: Set<List<Deck>>): Boolean {
        return matchPreviousCards.contains(matchDecks)
    }

    private fun playMatch(): Int {
        while (decks.none { it.isEmpty() }) {
            val drawnCards = decks.map { it.draw() }
            drawnCards.max()?.let { maxCard ->
                val winner = drawnCards.indexOf(maxCard)
                val sortedCards = sortCards(drawnCards, winner)
                decks[winner].insertCards(sortedCards)
            }
        }
        return decks.indexOfFirst { !it.isEmpty() }
    }
}

class Deck(val player: Int, private val initialCards: List<Int>) {
    val cards = initialCards.toMutableList()

    fun draw(): Int {
        return cards.removeAt(0)
    }

    fun insertCards(items: List<Int>) {
        cards.addAll(items)
    }

    fun isEmpty(): Boolean {
        return cards.isEmpty()
    }

    fun hasEnoughCards(n: Int): Boolean {
        return cards.size >= n
    }

    fun getScore(): Long {
        return cards.reversed().foldIndexed(0L) { index, acc, card ->
            acc + (index + 1) * card
        }
    }

    fun copyOf(): Deck {
        return Deck(player, cards)
    }

    fun cutDeckTo(n: Int) {
        while (cards.size > n) {
            cards.removeAt(n)
        }
    }

    fun reset(): Deck {
        cards.clear()
        cards.addAll(initialCards)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is Deck) {
            return this.cards == other.cards
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return cards.hashCode()
    }
}

fun parseDeck(group: List<String>): Deck {
    val player = group[1].take(1).toInt()
    val cards = group.subList(2, group.size).map { it.toInt() }
    return Deck(player, cards)
}

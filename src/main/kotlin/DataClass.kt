import kotlin.random.Random

data class LetterKey(val uyir: String, val mei: String)

data class QuestionState(
    var tamilLetters: Map<LetterKey, String>,
    var letterState: LetterState,
    var timerState: TimerState,
    var showAnswer: Boolean
)

data class TimerState(
    var isLive: Boolean = false,
    var isPaused: Boolean = false,
    var time: Long = 0,
    var count: Int = 0)

data class LetterState(
    override var index: Int,
    override var tamilLetters: Map<LetterKey, String>,
    override var history: MutableList<Int>,
    override var answers: MutableSet<LetterKey>,
    val letterKeys: List<LetterKey>
) : HistoryState {
    constructor(tamilLetters: Map<LetterKey, String>) : this(
        nextIndex(0, tamilLetters.size),
        tamilLetters,
        mutableListOf(),
        mutableSetOf(),
        tamilLetters.keys.toList())
    fun getCurrent(): LetterKey = letterKeys[index]
    fun goNext(): Int {
        answers.add(getCurrent())
        goNext(letterKeys.size)
        return answers.size
    }
    fun goPrevious() = goPrevious(letterKeys.size)
    fun getAnswer(): String = tamilLetters[getCurrent()]!!
}

interface HistoryState {
    var index: Int
    var tamilLetters: Map<LetterKey, String>
    var history: MutableList<Int>
    var answers: MutableSet<LetterKey>
    fun goNext(maxIndex: Int) {
        if (history.isEmpty()) {
            history = generateRandomList(maxIndex)
            history.remove(index)
            history.add(index)
        }
        val nextIndex = history.removeFirst()
        history.add(nextIndex)
        println("${this::class} Current: $index to New: $nextIndex of Total: $maxIndex")
        index = nextIndex
    }
    fun goPrevious(maxIndex: Int) {
        if (history.isEmpty()) {
            history = generateRandomList(maxIndex)
            history.remove(index)
            history.add(index)
        }
        var nextIndex = history.removeLast()
        history.add(0, nextIndex)
        nextIndex = history.last()
        println("${this::class} Current: $index to New: $nextIndex of Total: $maxIndex")
        index = nextIndex
    }
    fun clearAnswers() = answers.clear()
}

fun generateRandomList(maxIndex: Int): MutableList<Int> {
    var count = maxIndex - 1
    val randomList = generateSequence { (count--).takeIf { it >= 0 } }.toMutableList()
    randomList.shuffle()
    return randomList
}

fun nextIndex(currentIndex: Int, maxIndex: Int): Int {
    var newIndex: Int
    do {
        newIndex = Random.nextInt(maxIndex)
    } while (newIndex == currentIndex && maxIndex != 1)
    println("Current: $currentIndex to New: $newIndex of Total: $maxIndex")
    return newIndex
}

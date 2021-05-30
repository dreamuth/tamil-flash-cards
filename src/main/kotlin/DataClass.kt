import org.w3c.dom.Audio
import kotlin.random.Random

external interface SoundResponse {
    val word: String
    val phonetics: Array<Phonetic>
    val meanings: Array<Any>
}
external interface Phonetic {
    val text: String?
    val audio: String?
}
data class LetterKey(val uyir: String, val mei: String)

data class QuestionState(
    var isTamil: Boolean,
    var tamilLetters: Map<LetterKey, String>,
    var sightWords: MutableMap<EnglishLevel, List<String>>,
    var letterState: LetterStateTamil,
    var sightWordsState: SightWordsState,
    var selectedEnglishLevel: EnglishLevel,
    var sightWordsAudios: Map<String, Audio>,
    var timerState: TimerState,
    var showAnswer: Boolean
)

data class TimerState(
    var isLive: Boolean = false,
    var isPaused: Boolean = false,
    var time: Long = 0,
    var total: Int = 0,
    var count: Int = 0) {
    fun isCompleted():Boolean {
        return count == total
    }
}

enum class EnglishLevel(val displayValue: String, val filename: String) {
    LEVEL_I("Level I", "level1"),
    LEVEL_II("Level II", "level2"),
    LEVEL_III("Level III", "level3"),
    LEVEL_IV("Level IV", "level4"),
    LEVEL_V("Level V", "level5"),
    LEVEL_VI("Level VI", "level6");

    companion object {
        fun fromDisplayValue(displayValue: String): EnglishLevel {
            return values().first { it.displayValue == displayValue }
        }
        fun fromFilename(filename: String): EnglishLevel {
            return values().first { it.filename == filename }
        }
    }
}

data class LetterStateTamil(
    override var index: Int,
    override var tamilLetters: Map<LetterKey, String>,
    override var history: MutableList<Int>,
    override var answers: MutableSet<LetterKey>,
    val letterKeys: List<LetterKey>
) : TamilHistoryState {
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

interface TamilHistoryState {
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

data class SightWordsState(
    override var index: Int,
    override var words: List<String>,
    override var history: MutableList<Int>,
    override var answers: MutableSet<String>
) : HistoryState {
    constructor(words: List<String>) : this(
        nextIndex(0, words.size),
        words,
        mutableListOf(),
        mutableSetOf())
    fun getCurrent(): String = words[index]
    fun goNext(): Int {
        answers.add(getCurrent())
        goNext(words.size)
        return answers.size
    }
    fun goPrevious() = goPrevious(words.size)
    fun getAnswer(): String = getCurrent()
}

interface HistoryState {
    var index: Int
    var words: List<String>
    var history: MutableList<Int>
    var answers: MutableSet<String>
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

import org.w3c.dom.Audio

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
data class TamilLetter(val uyir: String, val mei: String, val uyirMei: String, val help: String)

data class QuestionState(
    var cardType: CardType,
    var sightWords: MutableMap<EnglishLevel, List<String>>,
    var tamilState: TamilState,
    var englishState: EnglishState,
    var selectedTamilLevel: TamilLevel,
    var selectedEnglishLevel: EnglishLevel,
    var sightWordsAudios: Map<String, Audio>,
    var timerState: TimerState,
    var showAnswer: Boolean,
)

data class TamilQuestionState(
    var tamilState: TamilState,
    var selectedLevel: TamilLevel,
)

data class TimerState(var isLive: Boolean = true, var isPaused: Boolean = false, var time: Long = 0)

enum class CardType(val displayValue: String) {
    TAMIL("தமிழ் பயிற்சி"),
    ENGLISH("English Practice")
}

enum class TamilLevel(val displayValue: String) {
    LEVEL_I("நிலை I"),
    LEVEL_II("நிலை II");

    companion object {
        fun fromDisplayValue(displayValue: String): TamilLevel {
            return values().first { it.displayValue == displayValue }
        }
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

val helpLetters = listOf("்", "ா", "ி", "ீ", "ு", "ூ", "ெ", "ே", "ை", "ொ", "ோ", "ௌ")
val uyirLetters = listOf("அ", "ஆ", "இ", "ஈ", "உ", "ஊ", "எ", "ஏ", "ஐ", "ஒ", "ஓ", "ஔ")
val meiLetters =
    listOf("க்", "ங்", "ச்", "ஞ்", "ட்", "ண்", "த்", "ந்", "ப்", "ம்", "ய்", "ர்", "ல்", "வ்", "ழ்", "ள்", "ற்", "ன்")
val uyirMeiLetters = listOf(
    "க", "கா", "கி", "கீ", "கு", "கூ", "கெ", "கே", "கை", "கொ", "கோ", "கௌ",
    "ங", "ஙா", "ஙி", "ஙீ", "ஙு", "ஙூ", "ஙெ", "ஙே", "ஙை", "ஙொ", "ஙோ", "ஙௌ",
    "ச", "சா", "சி", "சீ", "சு", "சூ", "செ", "சே", "சை", "சொ", "சோ", "சௌ",
    "ஞ", "ஞா", "ஞி", "ஞீ", "ஞு", "ஞூ", "ஞெ", "ஞே", "ஞை", "ஞொ", "ஞோ", "ஞௌ",
    "ட", "டா", "டி", "டீ", "டு", "டூ", "டெ", "டே", "டை", "டொ", "டோ", "டௌ",
    "ண", "ணா", "ணி", "ணீ", "ணு", "ணூ", "ணெ", "ணே", "ணை", "ணொ", "ணோ", "ணௌ",
    "த", "தா", "தி", "தீ", "து", "தூ", "தெ", "தே", "தை", "தொ", "தோ", "தௌ",
    "ந", "நா", "நி", "நீ", "நு", "நூ", "நெ", "நே", "நை", "நொ", "நோ", "நௌ",
    "ப", "பா", "பி", "பீ", "பு", "பூ", "பெ", "பே", "பை", "பொ", "போ", "பௌ",
    "ம", "மா", "மி", "மீ", "மு", "மூ", "மெ", "மே", "மை", "மொ", "மோ", "மௌ",
    "ய", "யா", "யி", "யீ", "யு", "யூ", "யெ", "யே", "யை", "யொ", "யோ", "யௌ",
    "ர", "ரா", "ரி", "ரீ", "ரு", "ரூ", "ரெ", "ரே", "ரை", "ரொ", "ரோ", "ரௌ",
    "ல", "லா", "லி", "லீ", "லு", "லூ", "லெ", "லே", "லை", "லொ", "லோ", "லௌ",
    "வ", "வா", "வி", "வீ", "வு", "வூ", "வெ", "வே", "வை", "வொ", "வோ", "வௌ",
    "ழ", "ழா", "ழி", "ழீ", "ழு", "ழூ", "ழெ", "ழே", "ழை", "ழொ", "ழோ", "ழௌ",
    "ள", "ளா", "ளி", "ளீ", "ளு", "ளூ", "ளெ", "ளே", "ளை", "ளொ", "ளோ", "ளௌ",
    "ற", "றா", "றி", "றீ", "று", "றூ", "றெ", "றே", "றை", "றொ", "றோ", "றௌ",
    "ன", "னா", "னி", "னீ", "னு", "னூ", "னெ", "னே", "னை", "னொ", "னோ", "னௌ"
)
val helpLettersMap = uyirLetters.zip(helpLetters).toMap()
val uyirMeiLettersMap = getUyirMeiLettersMap()

fun getUyirMeiLettersMap(): Map<LetterKey, String> {
    var uyirMeiIndex = 0
    val result = mutableMapOf<LetterKey, String>()
    for (meiIndex in meiLetters.indices) {
        for (uyirIndex in uyirLetters.indices) {
            result[LetterKey(uyirLetters[uyirIndex], meiLetters[meiIndex])] = uyirMeiLetters[uyirMeiIndex++]
        }
    }
    return result
}

fun getTamilLettersList(): List<TamilLetter> {
    return getUyirMeiLettersMap().map { TamilLetter(it.key.uyir, it.key.mei, it.value, helpLettersMap[it.key.uyir]!!) }
        .toList()
}

data class TamilState(
    override var index: Int,
    override var history: List<Int>,
    override var points: MutableMap<String, Int>,
    override var factor: Int,
    var tamilLetters: List<TamilLetter>
) : HistoryState {
    constructor() : this(
        0,
        generateRandomList(getTamilLettersList().size),
        mutableMapOf<String, Int>(),
        3,
        getTamilLettersList()
    )

    fun getQuestion(): TamilLetter = tamilLetters[getCurrentIndex()]
    fun addPoints(currentPoints: Int, override: Boolean = false) {
        super.addPoints(getAnswer(), currentPoints, override)
    }
    fun getAnswer(): String = tamilLetters[getCurrentIndex()].uyirMei
    fun getUyirMeiForUyir(): List<String> {
        return tamilLetters.filter { it.mei == getQuestion().mei }.map { it.uyirMei }
    }
    override fun goNext() {
        addPoints(getAnswer(), 3)
        super.goNext()
    }
}

data class EnglishState(
    override var index: Int,
    override var history: List<Int>,
    override var points: MutableMap<String, Int>,
    override var factor: Int,
    var words: List<String>
) : HistoryState {
    constructor(words: List<String>) : this(0, generateRandomList(words.size), mutableMapOf<String, Int>(), 1, words)

    fun getQuestion(): String = words[getCurrentIndex()]
    private fun getAnswer(): String = words[getCurrentIndex()]
    override fun goNext() {
        addPoints(getAnswer())
        super.goNext()
    }
}

interface HistoryState {
    var index: Int
    var history: List<Int>
    var points: MutableMap<String, Int>
    var factor: Int
    fun hasNext(): Boolean {
        return index != (history.size - 1)
    }

    fun hasPrevious(): Boolean {
        return index != 0
    }

    fun isPlayed(key: String): Boolean {
        return points.contains(key)
    }

    fun getCurrentIndex(): Int {
        return history[index]
    }

    fun goNext() {
        if (hasNext()) ++index
    }

    fun goPrevious() {
        if (hasPrevious()) --index
    }

    fun getPoints(): Int {
        return points.values.sum()
    }

    fun addPoints(key: String, currentPoints: Int = 1, override: Boolean = false) {
        if (points.contains(key)) {
            if (override) {
                points[key] = currentPoints
            }
        } else {
            points[key] = currentPoints
        }
    }

    fun attemptedPoints(): Int {
        return points.count() * factor
    }

    fun maxPoints(): Int {
        return history.size * factor
    }

    fun isCompleted(): Boolean {
        return points.size == history.size
    }
}


fun generateRandomList(maxIndex: Int): MutableList<Int> {
    var count = maxIndex - 1
    val randomList = generateSequence { (count--).takeIf { it >= 0 } }.toMutableList()
    randomList.shuffle()
    return randomList
}

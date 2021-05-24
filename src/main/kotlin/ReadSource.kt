fun readSource(sourceTxt: String): MutableMap<LetterKey, String> {
    val result = mutableMapOf<LetterKey, String>()
    val lines = sourceTxt.lines()
    val uyirLetters = lines[0].split(",")
    val meiLetters = lines[1].split(",")
    val uyirMeiLetters = mutableListOf<String>()
    for (i in 2..19) {
        uyirMeiLetters.addAll(lines[i].split(","))
    }
    var uyirMeiIndex = 0
    for (meiIndex in meiLetters.indices) {
        for (uyirIndex in uyirLetters.indices) {
            result[LetterKey(uyirLetters[uyirIndex], meiLetters[meiIndex])] = uyirMeiLetters[uyirMeiIndex++]
        }
    }
    return result
}

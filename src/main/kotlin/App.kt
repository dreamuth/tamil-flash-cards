import components.buttonGroup
import english.sightWordsPage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.id
import kotlinx.html.js.onChangeFunction
import kotlinx.html.tabIndex
import org.w3c.dom.Audio
import org.w3c.dom.HTMLInputElement
import pages.headerPage
import pages.statusPage
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledH5
import styled.styledImg
import styled.styledInput
import styled.styledLabel
import tamil.playtime
import tamil.tamilLettersPage
import tamil.tamilLevelDropDown

suspend fun fetchSightWords(): MutableMap<EnglishLevel, List<String>> {
    println("version: 2021-06-05.1")
    val prefix = if (window.location.toString().contains("dreamuth.github.io/")) "/tamil-flash-cards" else ""
    val result = mutableMapOf<EnglishLevel, List<String>>()
    for (i in 1..8) {
        val sourceUrl = "$prefix/private/english-sight-words/level$i.txt"
        val sourceData = window.fetch(sourceUrl).await().text().await()
        result[EnglishLevel.fromFilename("level$i")] = sourceData.lines().filter { it.isNotBlank() }
    }
    return result
}

suspend fun fetchSoundUrls(): Map<String, String> {
    val prefix = if (window.location.toString().contains("dreamuth.github.io/")) "/tamil-flash-cards" else ""
    val sourceUrl = "$prefix/private/english-sight-words/sounds.csv"
    val sourceData = window.fetch(sourceUrl).await().text().await()
    return sourceData.lines().filter { it.isNotBlank() }
        .associate { val pair = it.split(","); pair[0] to pair[1] }
}

external interface AppState : RState {
    var loaded: Boolean
    var questionState: QuestionState
}

val mainScope = MainScope()

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        mainScope.launch {
            val sightWordsSource = fetchSightWords()
            val newEnglishState = EnglishState(sightWordsSource[EnglishLevel.LEVEL_I]!!)
            val soundUrls = fetchSoundUrls()
            val url = soundUrls[newEnglishState.getQuestion()]
            val audio = if (url != null) Audio(url) else null
            setState {
                questionState = QuestionState(
                    cardType = CardType.TAMIL,
                    showTimer = false,
                    playAudioOnNext = true,
                    sightWords = sightWordsSource,
                    selectedTamilLevel = TamilLevel.LEVEL_I,
                    selectedEnglishLevel = EnglishLevel.LEVEL_I,
                    showAnswer = false,
                    englishState = newEnglishState,
                    timerState = TimerState(),
                    sightWordsAudio = audio,
                    sounds = soundUrls,
                    tamilState = TamilState(),
                    selectedTimerValue = TimerValues.MINS_10,
                )
                loaded = true
                window.setInterval(timerHandler(), 1000)
            }
        }
    }

    private fun timerHandler(): () -> Unit = {
        if (state.questionState.timerState.isLive) {
            if (state.questionState.cardType == CardType.ENGLISH && !state.questionState.englishState.isCompleted()) {
                setState {
                    questionState.timerState.time++
                }
            } else if (state.questionState.cardType == CardType.TAMIL && !state.questionState.tamilState.isCompleted()) {
                if (state.questionState.timerState.time > 0) {
                    setState {
                        questionState.timerState.time--
                    }
                }
            }
        }
    }

    private fun fetchFirstAudio(word: String): Audio? {
        val url = state.questionState.sounds[word]
        return if (url != null) Audio(url) else null
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("container-sm p-0 h-100")
                backgroundColor = Color("#F0F2F5").withAlpha(0.5)
            }
            headerPage {
                title = if (state.loaded) state.questionState.cardType.displayValue else CardType.TAMIL.displayValue
            }
            styledDiv {
                css {
                    classes = mutableListOf("container-fluid m-0 p-0 justify-content-center")
                }
                if (state.loaded) {
                    styledDiv {
                        css {
                            classes = mutableListOf("d-flex")
                        }
                        buttonGroup {
                            selected = state.questionState.cardType.title
                            allValues = CardType.values().map { it.title }.toList()
                            onButtonClick = {
                                val selectedCard = CardType.fromTitle(it)
                                setState {
                                    questionState.showAnswer = false
                                    state.questionState.cardType = selectedCard
                                    state.questionState.timerState =
                                        TimerState(isLive = selectedCard == CardType.ENGLISH)
                                }
                            }
                        }
                        styledDiv {
                            css {
                                classes = mutableListOf("p-2")
                            }
                            styledButton {
                                css {
                                    classes = mutableListOf("btn btn-outline-primary")
                                    fontSize = 20.px
                                }
                                styledImg {
                                    css {
                                        width = 30.px
                                        height = 30.px
                                    }
                                    attrs.src = "svg/settings.svg"
                                }
                                attrs {
                                    type = ButtonType.button
                                    attributes["data-bs-toggle"] = "offcanvas"
                                    attributes["data-bs-target"] = "#settingsMenu"
                                }
                            }
                            styledDiv {
                                css {
                                    classes = mutableListOf("offcanvas offcanvas-start")
                                }
                                attrs {
                                    id = "settingsMenu"
                                    tabIndex = "-1"
                                }
                                styledDiv {
                                    css {
                                        classes = mutableListOf("offcanvas-header")
                                    }
                                    styledH5 {
                                        css {
                                            classes = mutableListOf("offcanvas-title")
                                        }
                                        attrs {
                                            id = "settingsMenuLabel"
                                        }
                                        +"Settings"
                                    }
                                    styledButton {
                                        css {
                                            classes = mutableListOf("btn-close text-reset")
                                        }
                                        attrs {
                                            type = ButtonType.button
                                            attributes["data-bs-dismiss"] = "offcanvas"
                                        }
                                    }
                                }
                                styledDiv {
                                    css {
                                        classes = mutableListOf("offcanvas-body")
                                    }
                                    styledDiv {
                                        css {
                                            classes = mutableListOf("form-check form-switch")
                                        }
                                        styledInput {
                                            css {
                                                classes = mutableListOf("form-check-input")
                                            }
                                            attrs {
                                                checked = state.questionState.showTimer
                                                type = InputType.checkBox
                                                id = "flexSwitchTimer"
                                                onChangeFunction = { event ->
                                                    val target = event.target as HTMLInputElement
                                                    setState {
                                                        questionState.showTimer = target.checked
                                                    }
                                                }
                                            }
                                        }
                                        styledLabel {
                                            css {
                                                classes = mutableListOf("form-check-label")
                                            }
                                            attrs {
                                                htmlFor = "flexSwitchTimer"
                                            }
                                            +"Enable Timer"
                                        }
                                    }
                                    styledDiv {
                                        css {
                                            classes = mutableListOf("form-check form-switch")
                                        }
                                        styledInput {
                                            css {
                                                classes = mutableListOf("form-check-input")
                                            }
                                            attrs {
                                                checked = state.questionState.playAudioOnNext
                                                type = InputType.checkBox
                                                id = "flexSwitchPlayAudioOnNext"
                                                onChangeFunction = { event ->
                                                    val target = event.target as HTMLInputElement
                                                    setState {
                                                        questionState.playAudioOnNext = target.checked
                                                    }
                                                }
                                            }
                                        }
                                        styledLabel {
                                            css {
                                                classes = mutableListOf("form-check-label")
                                            }
                                            attrs {
                                                htmlFor = "flexSwitchPlayAudioOnNext"
                                            }
                                            +"Play audio on next button click"
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (state.questionState.cardType == CardType.TAMIL) {
                        if (state.questionState.timerState.isLive) {
                            tamilLettersPage {
                                questionState = state.questionState
                                onShowAnswerClick = { points ->
                                    setState {
                                        questionState.showAnswer = !questionState.showAnswer
                                        questionState.tamilState.addPoints(points)
                                    }
                                }
                                onNextClick = {
                                    setState {
                                        questionState.showAnswer = false
                                        questionState.tamilState.goNext()
                                    }
                                }
                                onPreviousClick = {
                                    setState {
                                        questionState.showAnswer = false
                                        questionState.tamilState.goPrevious()
                                    }
                                }
                                onLevelChangeClick = { tamilLevel ->
                                    if (questionState.selectedTamilLevel != tamilLevel) {
                                        setState {
                                            questionState.selectedTamilLevel = tamilLevel
                                            questionState.tamilState = TamilState()
                                            questionState.timerState = TimerState()
                                            questionState.showAnswer = false
                                        }
                                    }
                                }
                                onReloadClick = {
                                    setState {
                                        questionState.tamilState = TamilState()
                                        questionState.timerState = TimerState()
                                    }
                                }
                                onNextLevelClick = {
                                    val nextLevel = when (state.questionState.selectedTamilLevel) {
                                        TamilLevel.LEVEL_I -> TamilLevel.LEVEL_II
                                        TamilLevel.LEVEL_II -> TamilLevel.LEVEL_III
                                        TamilLevel.LEVEL_III -> TamilLevel.LEVEL_III
                                    }
                                    onLevelChangeClick(nextLevel)
                                }
                            }
                        } else {
                            tamilLevelDropDown {
                                displayValue = state.questionState.selectedTamilLevel.displayValue
                                onLevelChangeClick = { tamilLevel ->
                                    if (state.questionState.selectedTamilLevel != tamilLevel) {
                                        setState {
                                            questionState.selectedTamilLevel = tamilLevel
                                            questionState.tamilState = TamilState()
                                            questionState.timerState = TimerState()
                                            questionState.showAnswer = false
                                        }
                                    }
                                }
                            }
                            playtime {
                                questionState = state.questionState
                                onSelectedTimerValueChange = {
                                    setState {
                                        questionState.selectedTimerValue = it
                                    }
                                }
                                onStart = {
                                    setState {
                                        questionState.timerState =
                                            TimerState(isLive = true, time = questionState.selectedTimerValue.value)
                                    }
                                }
                            }
                        }
                    } else {
                        sightWordsPage {
                            questionState = state.questionState
                            onLevelChangeClick = { englishLevel ->
                                if (questionState.selectedEnglishLevel != englishLevel) {
                                    setState {
                                        questionState.selectedEnglishLevel = englishLevel
                                        questionState.englishState =
                                            EnglishState(questionState.sightWords[englishLevel]!!)
                                        questionState.timerState = TimerState(isLive = true)
                                    }
                                    mainScope.launch {
                                        val audio = fetchFirstAudio(questionState.englishState.getQuestion())
                                        setState {
                                            questionState.sightWordsAudio = audio
                                        }
                                    }
                                }
                            }
                            onBackClick = {
                                setState {
                                    questionState.englishState.goPrevious()
                                }
                                mainScope.launch {
                                    val audio = fetchFirstAudio(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudio = audio
                                    }
                                }
                            }
                            onNextClick = {
                                setState {
                                    questionState.englishState.goNext()
                                }
                                mainScope.launch {
                                    val audio = fetchFirstAudio(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudio = audio
                                    }
                                }
                            }
                            onAudioClick = {
                                state.questionState.sightWordsAudio?.let { audio ->
                                    if (audio.currentTime.equals(0.0) || audio.ended) {
                                        audio.play()
                                    }
                                }
                            }
                            onReloadClick = {
                                setState {
                                    questionState.englishState =
                                        EnglishState(questionState.sightWords[questionState.selectedEnglishLevel]!!)
                                    questionState.timerState = TimerState(isLive = true)
                                }
                                mainScope.launch {
                                    val audio = fetchFirstAudio(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudio = audio
                                    }
                                }
                            }
                            onNextLevelClick = {
                                val nextLevel = when (state.questionState.selectedEnglishLevel) {
                                    EnglishLevel.LEVEL_I -> EnglishLevel.LEVEL_II
                                    EnglishLevel.LEVEL_II -> EnglishLevel.LEVEL_III
                                    EnglishLevel.LEVEL_III -> EnglishLevel.LEVEL_IV
                                    EnglishLevel.LEVEL_IV -> EnglishLevel.LEVEL_V
                                    EnglishLevel.LEVEL_V -> EnglishLevel.LEVEL_VI
                                    EnglishLevel.LEVEL_VI -> EnglishLevel.LEVEL_VII
                                    EnglishLevel.LEVEL_VII -> EnglishLevel.LEVEL_VIII
                                    EnglishLevel.LEVEL_VIII -> EnglishLevel.LEVEL_VIII
                                }
                                onLevelChangeClick(nextLevel)
                            }
                        }
                    }
                }
            }
            styledDiv {
                css {
                    classes = mutableListOf("fixed-bottom w-100")
                }
                styledDiv {
                    css {
                        classes = mutableListOf("container-sm p-0")
                    }
                    if (state.loaded) {
                        val currentTime = state.questionState.timerState.time
                        val currentPoints =
                            if (state.questionState.cardType == CardType.TAMIL) {
                                state.questionState.tamilState.getPoints()
                            } else {
                                state.questionState.englishState.getPoints()
                            }
                        val currentTotalPoints =
                            if (state.questionState.cardType == CardType.TAMIL) {
                                state.questionState.tamilState.getAttemptedPoints()
                            } else {
                                state.questionState.englishState.getMaxPoints()
                            }
                        statusPage {
                            showTime = state.questionState.showTimer
                            time = "Time: ${currentTime / 60 % 60} : ${currentTime % 60}"
                            points = currentPoints
                            totalPoints = currentTotalPoints
                        }
                    }
                }
            }
        }
    }
}

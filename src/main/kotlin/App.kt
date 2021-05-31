import english.sightWordsPage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.fontSize
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Audio
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
import tamil.tamilLettersPage

suspend fun fetchSightWords(): MutableMap<EnglishLevel, List<String>> {
    println("version: 2021-05-31.1")
    val prefix = if (window.location.toString().contains("dreamuth.github.io/")) "/tamil-flash-cards" else ""
    val result = mutableMapOf<EnglishLevel, List<String>>()
    for (i in 1..6) {
        val sourceUrl = "$prefix/private/english-sight-words/level$i.txt"
        val sourceData = window.fetch(sourceUrl).await().text().await()
        result[EnglishLevel.fromFilename("level$i")] = sourceData.lines().filter { it.isNotBlank() }
    }
    return result
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
            val soundUrls = fetchSoundUrls(newEnglishState.getQuestion())
            setState {
                questionState = QuestionState(
                    cardType = CardType.TAMIL,
                    sightWords = sightWordsSource,
                    selectedTamilLevel = TamilLevel.LEVEL_I,
                    selectedEnglishLevel = EnglishLevel.LEVEL_I,
                    showAnswer = false,
                    englishState = newEnglishState,
                    timerState = TimerState(),
                    sightWordsAudios = soundUrls.associateWith { Audio(it) },
                    tamilState = TamilState()
                )
                loaded = true
                window.setInterval(timerHandler(), 1000)
            }
        }
    }

    private fun timerHandler(): () -> Unit = {
        if (state.questionState.timerState.isLive
            && !state.questionState.timerState.isPaused
            && ((state.questionState.cardType == CardType.TAMIL && !state.questionState.tamilState.isCompleted())
                || (state.questionState.cardType == CardType.ENGLISH && !state.questionState.englishState.isCompleted()))
        ) {
            setState {
                questionState.timerState.time++
            }
        }
    }

    private suspend fun fetchSoundUrls(word: String): List<String> {
        val sourceUrl = "https://api.dictionaryapi.dev/api/v2/entries/en_US/$word"
        val sourceData = window.fetch(sourceUrl).await().json().await().unsafeCast<Array<SoundResponse>>()
        val values = sourceData.firstOrNull()?.phonetics?.map { it.audio }
        return values?.filterNotNull() ?: listOf()
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
                            classes = mutableListOf("btn-group p-2 w-100")
                        }
                        val tamilStyle = if (state.questionState.cardType == CardType.TAMIL) "active" else ""
                        val englishStyle = if (state.questionState.cardType == CardType.ENGLISH) "active" else ""
                        styledButton {
                            css {
                                classes = mutableListOf("btn btn-outline-primary $tamilStyle")
                                fontSize = 20.px
                            }
                            attrs {
                                onClickFunction = {
                                    setState {
                                        questionState.showAnswer = false
                                        state.questionState.cardType = CardType.TAMIL
                                        state.questionState.timerState = TimerState()
                                    }
                                }
                            }
                            +"தமிழ்"
                        }
                        styledButton {
                            css {
                                classes = mutableListOf("btn btn-outline-primary $englishStyle")
                                fontSize = 20.px
                            }
                            attrs {
                                onClickFunction = {
                                    setState {
                                        questionState.showAnswer = false
                                        state.questionState.cardType = CardType.ENGLISH
                                        state.questionState.timerState = TimerState()
                                    }
                                }
                            }
                            +"English"
                        }
                    }
                    if (state.questionState.cardType == CardType.TAMIL) {
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
                                        questionState.timerState = TimerState()
                                    }
                                    mainScope.launch {
                                        val fetchSoundUrls = fetchSoundUrls(questionState.englishState.getQuestion())
                                        setState {
                                            questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
                                        }
                                    }
                                }
                            }
                            onBackClick = {
                                setState {
                                    questionState.englishState.goPrevious()
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
                                    }
                                }
                            }
                            onNextClick = {
                                setState {
                                    questionState.englishState.goNext()
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
                                    }
                                }
                            }
                            onAudioClick = { name ->
                                state.questionState.sightWordsAudios.forEach {
                                    if (it.key != name) {
                                        it.value.pause()
                                        it.value.currentTime = 0.0
                                    }
                                }
                                val audio = state.questionState.sightWordsAudios[name]
                                audio?.let {
                                    if (it.currentTime.equals(0.0) || it.ended) {
                                        it.play()
                                    }
                                }
                            }
                            onReloadClick = {
                                setState {
                                    questionState.englishState =
                                        EnglishState(questionState.sightWords[questionState.selectedEnglishLevel]!!)
                                    questionState.timerState = TimerState()
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.englishState.getQuestion())
                                    setState {
                                        questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
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
                                    EnglishLevel.LEVEL_VI -> EnglishLevel.LEVEL_VI
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
                                state.questionState.tamilState.attemptedPoints()
                            } else {
                                state.questionState.englishState.maxPoints()
                            }
                        statusPage {
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

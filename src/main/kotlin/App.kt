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
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledButton
import styled.styledDiv

suspend fun fetchSource(): MutableMap<LetterKey, String> {
    val prefix = if (window.location.toString().contains("dreamuth.github.io/")) "/tamil-flash-cards" else ""
    val sourceUrl = "$prefix/private/tamilLetters.txt"
    val sourceData = window.fetch(sourceUrl).await().text().await()
    val tamilLetters = readSource(sourceData)
    println("version: 2021-05-30.1")
    return tamilLetters
}

suspend fun fetchSightWords(): MutableMap<EnglishLevel, List<String>> {
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
            val receivedLetters = fetchSource()
            val sightWordsSource = fetchSightWords()
            val newSightWordsState = SightWordsState(sightWordsSource[EnglishLevel.LEVEL_I]!!)
            val soundUrls = fetchSoundUrls(newSightWordsState.getCurrent())
            setState {
                questionState = QuestionState(
                    isTamil = false,
                    tamilLetters = receivedLetters,
                    sightWords = sightWordsSource,
                    selectedEnglishLevel = EnglishLevel.LEVEL_I,
                    letterState = LetterStateTamil(receivedLetters),
                    timerState = TimerState(isLive = true),
                    showAnswer = false,
                    sightWordsState = newSightWordsState,
                    sightWordsAudios = soundUrls.associateWith { Audio(it) }
                )
                loaded = true
                window.setInterval(timerHandler(), 1000)
            }
        }
    }

    private fun timerHandler(): () -> Unit = {
        if (state.questionState.timerState.isLive
            && !state.questionState.timerState.isPaused
        ) {
            setState {
                questionState.timerState.time++
            }
        }
    }

    private suspend fun fetchSoundUrls(word: String): List<String> {
        val sourceUrl = "https://api.dictionaryapi.dev/api/v2/entries/en_US/$word"
        val sourceData = window.fetch(sourceUrl).await().json().await() as Array<SoundResponse>
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
                title = if (state.loaded && !state.questionState.isTamil) "English Practice" else "தமிழ் பயிற்சி"
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
                        val tamilStyle = if (state.questionState.isTamil) "active" else ""
                        val englishStyle = if (state.questionState.isTamil) "" else "active"
                        styledButton {
                            css {
                                classes = mutableListOf("btn btn-outline-primary $tamilStyle")
                                fontSize = 20.px
                            }
                            attrs {
                                onClickFunction = {
                                    setState {
                                        questionState.showAnswer = false
                                        state.questionState.isTamil = true
                                        state.questionState.timerState = TimerState(isLive = true)
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
                                        state.questionState.isTamil = false
                                        state.questionState.timerState = TimerState(isLive = true)
                                    }
                                }
                            }
                            +"English"
                        }
                    }
                    if (state.questionState.isTamil) {
                        tamilLettersPage {
                            questionState = state.questionState
                            onShowAnswerClick = {
                                setState {
                                    questionState.showAnswer = !questionState.showAnswer
                                }
                            }
                            onNextClick = {
                                setState {
                                    questionState.showAnswer = false
                                    questionState.timerState.count = questionState.letterState.goNext()
                                }
                            }
                            onPreviousClick = {
                                setState {
                                    questionState.showAnswer = false
                                    questionState.letterState.goPrevious()
                                }
                            }
                        }
                    } else {
                        sightWordsPage {
                            questionState = state.questionState
                            onLevelChangeClick = { englishLevel ->
                                setState {
                                    if (questionState.selectedEnglishLevel != englishLevel) {
                                        questionState.selectedEnglishLevel = englishLevel
                                        questionState.sightWordsState =
                                            SightWordsState(questionState.sightWords[englishLevel]!!)
                                        questionState.timerState = TimerState(isLive = true)
                                    }
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.sightWordsState.getCurrent())
                                    setState {
                                        questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
                                    }
                                }
                            }
                            onBackClick = {
                                setState {
                                    questionState.sightWordsState.goPrevious()
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.sightWordsState.getCurrent())
                                    setState {
                                        questionState.sightWordsAudios = fetchSoundUrls.associateWith { Audio(it) }
                                    }
                                }
                            }
                            onNextClick = {
                                setState {
                                    questionState.timerState.count = questionState.sightWordsState.goNext()
                                }
                                mainScope.launch {
                                    val fetchSoundUrls = fetchSoundUrls(questionState.sightWordsState.getCurrent())
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
                    val time = if (state.loaded) state.questionState.timerState.time else 0
                    val points = if (state.loaded) state.questionState.timerState.count else 0
                    statusPage {
                        left = "Time: ${time / 60 % 60} : ${time % 60}"
                        right = "Points: $points"
                    }
                }
            }
        }
    }
}

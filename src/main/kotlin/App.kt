import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.html.js.onClickFunction
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
    println("version: 2021-05-25.1")
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

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val receivedLetters = fetchSource()
            val sightWordsSource = fetchSightWords()
            setState {
                questionState = QuestionState(
                    isTamil = true,
                    tamilLetters = receivedLetters,
                    sightWords = sightWordsSource,
                    selectedEnglishLevel = EnglishLevel.LEVEL_I,
                    letterState = LetterStateTamil(receivedLetters),
                    timerState = TimerState(isLive = true),
                    showAnswer = false,
                    sightWordsState = SightWordsState(sightWordsSource[EnglishLevel.LEVEL_I]!!)
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

    override fun RBuilder.render() {
        styledDiv {
            css {
                classes = mutableListOf("container-fluid m-0 p-0")
                height = 100.pct
            }
            styledDiv {
                css {
                    classes = mutableListOf("alert alert-primary text-center mb-0 rounded-0 fw-bold")
                }
                val displayValue =
                    if (state.loaded && !state.questionState.isTamil) "English Practice" else "தமிழ் பயிற்சி"
                +displayValue
            }
            styledDiv {
                css {
                    classes = mutableListOf("container-fluid m-0 p-0")
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
                            }
                            onNextClick = {
                                setState {
                                    questionState.timerState.count = questionState.sightWordsState.goNext()
                                }
                            }
                        }
                    }
                    statusPage {
                        questionState = state.questionState
                    }
                }
            }
        }
    }
}

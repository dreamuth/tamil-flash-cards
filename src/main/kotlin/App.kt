import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.css.height
import kotlinx.css.pct
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.setState
import styled.css
import styled.styledDiv

suspend fun fetchSource(): MutableMap<LetterKey, String> {
    val prefix = if (window.location.toString().contains("dreamuth.github.io/")) "/tamil-flash-cards" else ""
    val sourceUrl = "$prefix/private/tamilLetters.txt"
    val sourceData = window.fetch(sourceUrl).await().text().await()
    val tamilLetters = readSource(sourceData)
    println("version: 2021-05-24.3")
    return tamilLetters
}

external interface AppState : RState {
    var loaded: Boolean
    var tamilLetters: MutableMap<LetterKey, String>
    var questionState: QuestionState
}

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        val mainScope = MainScope()
        mainScope.launch {
            val receivedLetters = fetchSource()
            setState {
                tamilLetters = receivedLetters
                questionState = QuestionState(receivedLetters, LetterState(receivedLetters), TimerState(isLive = true), false)
                loaded = true
                window.setInterval(timerHandler(), 1000)
            }
        }
    }

    private fun timerHandler(): () -> Unit = {
        if (state.questionState.timerState.isLive
            && !state.questionState.timerState.isPaused) {
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
                    classes = mutableListOf("alert alert-primary text-center mb-0 rounded-0")
                }
                +"தமிழ் பயிற்சி"
            }
            styledDiv {
                css {
                    classes = mutableListOf("container-fluid m-0 p-0")
                }
                if (state.loaded) {
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
                    statusPage {
                        questionState = state.questionState
                    }
                }
            }
        }
    }
}
